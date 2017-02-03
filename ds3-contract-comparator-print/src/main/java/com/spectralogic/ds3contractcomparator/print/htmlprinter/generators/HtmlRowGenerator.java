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

package com.spectralogic.ds3contractcomparator.print.htmlprinter.generators;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.DeletedRow;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.rows.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static com.spectralogic.ds3autogen.utils.ConverterUtil.hasContent;
import static com.spectralogic.ds3autogen.utils.ConverterUtil.isEmpty;
import static com.spectralogic.ds3autogen.utils.NormalizingContractNamesUtil.removePath;

/**
 * Generates the {@link ImmutableList} of {@link Row} of {@link Object} using reflection.
 */
public final class HtmlRowGenerator {

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

    //TODO test
    public static <T> ImmutableList<Row> createModifiedRows(
            final T oldObject,
            final T newObject,
            final int indent) {
        if (oldObject == null && newObject == null) {
            return ImmutableList.of();
        }
        final Field[] fields = getFields(oldObject, newObject);
        final ImmutableList.Builder<Row> builder = ImmutableList.builder();

        int curIndent = indent; //todo cleanup (place in separate function and change to final)
        if (isReadable(oldObject, newObject, "name")) {
            ++curIndent;
        }

        for (final Field field : fields) {
            final String property = field.getName();

            final Optional<String> oldValue = getPropertyValue(oldObject, property);
            final Optional<String> newValue = getPropertyValue(newObject, property);

            final int fieldIndent = toFieldIndent(curIndent, property);
            if (oldValue.isPresent() || newValue.isPresent()) {
                if (field.getType() == ImmutableList.class) {
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
                } else {
                    if (!oldValue.isPresent() || !newValue.isPresent() || !oldValue.get().equals(newValue.get())) {
                        builder.add(new ModifiedRow(
                                fieldIndent,
                                property,
                                oldValue.orElse(RowConstants.NA),
                                newValue.orElse(RowConstants.NA)));
                    } else {
                        builder.add(new NoChangeRow(fieldIndent, property, oldValue.get()));
                    }
                }
            }
        }
        return builder.build();
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
        //Most Ds3 types have a name
        if (isReadable(oldObject, newObject, "name")) {
            return "name";
        }
        //Used for Ds3ResponseCode
        if (isReadable(oldObject, newObject, "code")) {
            return "code";
        }
        //Used for Ds3ResponseType
        if (isReadable(oldObject, newObject, "type")) {
            return "type";
        }
        //Unknown unique property
        return "";
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
                e.printStackTrace(); //todo fix
            }
        });
        return builder.build();
    }

    /**
     * Creates a union of all values for the specified property in both lists of objects
     */
    static <T> ImmutableSet<String> toPropertyUnion(
            final ImmutableList<T> oldList,
            final ImmutableList<T> newList,
            final String property) {
        final ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        if (hasContent(oldList)) {
            oldList.forEach(o -> {
                try {
                    builder.add(BeanUtils.getProperty(o, property));
                } catch (final IllegalAccessException|InvocationTargetException|NoSuchMethodException e) {
                    e.printStackTrace(); //todo fix
                }
            });
        }

        if (hasContent(newList)) {
            newList.forEach(o -> {
                try {
                    builder.add(BeanUtils.getProperty(o, property));
                } catch (final IllegalAccessException|InvocationTargetException|NoSuchMethodException e) {
                    e.printStackTrace(); //todo fix
                }
            });
        }

        return builder.build();
    }

    //TODO test
    public static <T> ImmutableList<Row> createDeletedRows(final T object, final int indent) {
        final Field[] fields = object.getClass().getDeclaredFields();
        final ImmutableList.Builder<Row> builder = ImmutableList.builder();

        int curIndent = indent;
        if (PropertyUtils.isReadable(object, "name")) {
            ++curIndent;
        }

        for (final Field field : fields) {
            final String property = field.getName();
            final Optional<String> value = getPropertyValue(object, property);
            final int fieldIndent = toFieldIndent(curIndent, property);
            if (value.isPresent()) {
                if (field.getType() == ImmutableList.class) {
                    builder.add(new NoChangeRow(fieldIndent, property, ""));

                    final ImmutableList<?> objList = getListPropertyFromObject(field, object);
                    objList.forEach(obj -> builder.addAll(createDeletedRows(obj, fieldIndent)));
                } else {
                    builder.add(new DeletedRow(fieldIndent, property, value.get()));
                }
            }
        }
        return builder.build();
    }

    //TODO test
    public static <T> ImmutableList<Row> createAddedRows(final T object, final int indent) {
        final Field[] fields = object.getClass().getDeclaredFields();
        final ImmutableList.Builder<Row> builder = ImmutableList.builder();

        int curIndent = indent;
        if (PropertyUtils.isReadable(object, "name")) {
            ++curIndent;
        }

        for (final Field field : fields) {
            final String property = field.getName();
            final Optional<String> value = getPropertyValue(object, property);
            final int fieldIndent = toFieldIndent(curIndent, property);
            if (value.isPresent()) {
                if (field.getType() == ImmutableList.class) {
                    builder.add(new NoChangeRow(fieldIndent, property, ""));

                    final ImmutableList<?> objList = getListPropertyFromObject(field, object);
                    objList.forEach(obj -> builder.addAll(createAddedRows(obj, fieldIndent)));
                } else {
                    builder.add(new AddedRow(fieldIndent, property, value.get()));
                }
            }
        }
        return builder.build();
    }

    //TODO test
    /**
     * Retrieves the specified property of type {@link ImmutableList}
     */
    private static <T, N> ImmutableList<N> getListPropertyFromObject(final Field field, final T object) {
        if (object == null) {
            return ImmutableList.of();
        }
        try {
            field.setAccessible(true);
            final Object objectField = field.get(object);
            if (objectField == null) {
                return ImmutableList.of();
            }
            if (objectField instanceof ImmutableList) {
                return (ImmutableList<N>) objectField;
            }
            throw new IllegalArgumentException("Object should be of type ImmutableList, but was: " + object.getClass().toString());
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e); //todo improve
        }
    }

    //TODO test
    private static int toFieldIndent(final int indent, final String property) {
        if (property.equalsIgnoreCase("name")) {
            return indent;
        }
        return indent + 1;
    }

    //TODO test
    /**
     * Gets the property value from the specified object. If the property is "name" or "type", then
     * any path will be removed. If the property does not exist within the object, than an empty
     * optional is returned.
     */
    private static <T> Optional<String> getPropertyValue(final T object, final String property) {
        if (object == null) {
            return Optional.empty();
        }
        try {
            final String value = BeanUtils.getSimpleProperty(object, property);
            if (value == null) {
                return Optional.empty();
            }
            switch (property.toLowerCase()) {
                case "name":
                case "type":
                    if (value.contains(".")) {
                        return Optional.of(removePath(value));
                    }
                    return Optional.of(value);
                default:
                    return Optional.of(value);
            }

        } catch (final IllegalAccessException|NoSuchMethodException|InvocationTargetException e) {
            e.printStackTrace(); //todo improve
            return Optional.empty();
        }
    }
}
