/*
 * ******************************************************************************
 *   Copyright 2016-2017 Spectra Logic Corporation. All Rights Reserved.
 *   Licensed under the Apache License, Version 2.0 (the "License"). You may not use
 *   this file except in compliance with the License. A copy of the License is located at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   or in the "license" file accompanying this file.
 *   This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 *   CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 * ****************************************************************************
 */

package com.spectralogic.ds3contractcomparator.print.htmlprinter.generators.row;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.rows.ModifiedRow;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.rows.NoChangeRow;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.rows.Row;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.rows.RowConstants;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static com.spectralogic.ds3autogen.utils.ConverterUtil.hasContent;
import static com.spectralogic.ds3autogen.utils.ConverterUtil.isEmpty;
import static com.spectralogic.ds3contractcomparator.print.utils.HtmlRowGeneratorUtils.*;

/**
 * Utilities for generating the {@link ImmutableList} of {@link Row} which
 * represent modified Ds3 Objects using reflection.
 */
public final class ModifiedHtmlRowGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(ModifiedHtmlRowGenerator.class);

    private ModifiedHtmlRowGenerator() {
        //pass
    }

    /**
     * Recursively traverses two objects using reflection and constructs the modified
     * rows that represent the changes and differences between the objects.
     */
    public static <T> ImmutableList<Row> createModifiedRows(
            final T oldObject,
            final T newObject,
            final int indent) {
        if (oldObject == null && newObject == null) {
            return ImmutableList.of();
        }
        final Field[] fields = getFields(oldObject, newObject);
        final ImmutableList.Builder<Row> builder = ImmutableList.builder();

        for (final Field field : fields) {
            final String property = field.getName();

            final Optional<String> oldValue = getPropertyValue(oldObject, property);
            final Optional<String> newValue = getPropertyValue(newObject, property);

            if (oldValue.isPresent() || newValue.isPresent()) {
                final int fieldIndent = toModifiedFieldIndent(indent, oldObject, newObject, field);
                if (field.getType() == ImmutableList.class) {
                    //Field is a list, recursively print each element in the list
                    builder.add(new NoChangeRow(fieldIndent, property, ""));

                    final ImmutableList<Object> oldObjList = getListPropertyFromObject(field, oldObject);
                    final ImmutableList<Object> newObjList = getListPropertyFromObject(field, newObject);

                    if (hasContent(oldObjList) || hasContent(newObjList)) {
                        final String uniqueProperty = getPropertyNameFromList(oldObjList, newObjList);

                        final ImmutableSet<String> parameterUnion = toPropertyUnion(oldObjList, newObjList, uniqueProperty);
                        final ImmutableMap<String, Object> oldMap = toPropertyMap(oldObjList, uniqueProperty);
                        final ImmutableMap<String, Object> newMap = toPropertyMap(newObjList, uniqueProperty);

                        parameterUnion.forEach(param -> builder.addAll(createModifiedRows(
                                oldMap.get(param),
                                newMap.get(param),
                                fieldIndent + 1)));
                    }
                } else if (oldValue.isPresent() && newValue.isPresent() && oldValue.get().equals(newValue.get())) {
                    //Element is the same in both contracts
                    builder.add(new NoChangeRow(fieldIndent, property, oldValue.get()));
                } else {
                    //Element is different between old and new contracts
                    builder.add(new ModifiedRow(
                            fieldIndent,
                            property,
                            oldValue.orElse(RowConstants.NA),
                            newValue.orElse(RowConstants.NA)));
                }
            }
        }
        return builder.build();
    }

    /**
     * Determines the level of indent to use for this field. All properties that are
     * not the unique property should be indented one additional space to the unique
     * property level. This ensures that lists of elements are visually distinct.
     */
    static <T> int toModifiedFieldIndent(
            final int curIndent,
            @Nullable final T oldObject,
            @Nullable final T newObject,
            final Field field) {
        if (oldObject == null && newObject == null) {
            throw  new IllegalArgumentException("Cannot determine field indent when objects are null");
        }
        return oldObject == null ? toFieldIndent(curIndent, newObject, field) : toFieldIndent(curIndent, oldObject, field);
    }

    /**
     * Retrieves the fields from an instance of a class. Either
     * {@code oldObject} or {@code newObject} can be null, but not both.
     * @param oldObject The old contract's version of the object. May be
     *                  null if object does not exist in the old contract.
     * @param newObject The new contract's version of the object. May be
     *                  null if object does not exist in the new contract.
     */
    static <T> Field[] getFields(@Nullable final T oldObject, @Nullable final T newObject) {
        if (oldObject == null && newObject == null) {
            throw new IllegalArgumentException("Both parameters cannot be null");
        }
        if (oldObject == null) {
            return newObject.getClass().getDeclaredFields();
        }
        return oldObject.getClass().getDeclaredFields();
    }

    /**
     * Determines if the specified field is readable within the class.
     * Either {@code oldObject} or {@code newObject} can be null, but
     * not both.
     * @param oldObject The old contract's version of the object. May be
     *                  null if object does not exist in the old contract.
     * @param newObject The new contract's version of the object. May be
     *                  null if object does not exist in the new contract.
     */
    static <T> boolean isReadable(final T oldObject, final T newObject, final String property) {
        if (oldObject == null && newObject == null) {
            throw new IllegalArgumentException("Both parameters cannot be null");
        }
        if (oldObject == null) {
            return PropertyUtils.isReadable(newObject, property);
        }
        return PropertyUtils.isReadable(oldObject, property);
    }

    /**
     * Retrieves the name of the unique property of the class within the lists {@code T}.
     * Either {@code oldObjectList} or {@code newObjectList} can be null or empty, but
     * there must exist at least one instance of {@code T} within at least one list.
     * @param oldObjectList List of objects from the old contract
     * @param newObjectList List of objects from the new contract
     */
    static <T> String getPropertyNameFromList(final ImmutableList<T> oldObjectList, final ImmutableList<T> newObjectList) {
        final T oldObject = hasContent(oldObjectList) ? oldObjectList.get(0) : null;
        final T newObject = hasContent(newObjectList) ? newObjectList.get(0) : null;
        return getPropertyName(oldObject, newObject);
    }

    /**
     * Retrieves the name of the unique property which is used to determine
     * if two instances of a class are representing the same contract
     * element. Either {@code oldObject} or {@code newObject} can be null,
     * but not both.
     * @param oldObject The old contract's version of the object. May be
     *                  null if object does not exist in the old contract.
     * @param newObject The new contract's version of the object. May be
     *                  null if object does not exist in the new contract.
     */
    static <T> String getPropertyName(final T oldObject, final T newObject) {
        return oldObject == null ? getUniqueProperty(newObject) : getUniqueProperty(oldObject);
    }

    /**
     * Creates a union of all values for the specified property in both lists of objects
     */
    static <T> ImmutableSet<String> toPropertyUnion(
            final ImmutableList<T> oldList,
            final ImmutableList<T> newList,
            final String property) {
        final ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        builder.addAll(getPropertyValues(oldList, property));
        builder.addAll(getPropertyValues(newList, property));
        return builder.build();
    }

    /**
     * Gets the set of property values from a list of objects
     */
    static <T> ImmutableSet<String> getPropertyValues(final ImmutableList<T> list, final String property) {
        if (isEmpty(list)) {
            return ImmutableSet.of();
        }
        final ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        list.forEach(o -> {
            try {
                builder.add(BeanUtils.getProperty(o, property));
            } catch (final IllegalAccessException|InvocationTargetException |NoSuchMethodException e) {
                LOG.warn("Cannot create property union of objects from class {} using property {}: {}",
                        o.getClass().toString(),
                        property,
                        e.getMessage());
            }
        });

        return builder.build();
    }

    /**
     * Creates a map between the specified property in string form, and the object containing the property.
     */
    static <T> ImmutableMap<String, T> toPropertyMap(final ImmutableList<T> objects, final String property) {
        if (isEmpty(objects)) {
            return ImmutableMap.of();
        }
        final ImmutableMap.Builder<String, T> builder = ImmutableMap.builder();

        objects.forEach(o -> {
            try {
                builder.put(BeanUtils.getProperty(o, property), o);
            } catch (final IllegalAccessException|InvocationTargetException|NoSuchMethodException e) {
                LOG.warn("Cannot create property map of object from class {} using property {}: {}",
                        o.getClass().toString(),
                        property,
                        e.getMessage());
            }
        });
        return builder.build();
    }
}
