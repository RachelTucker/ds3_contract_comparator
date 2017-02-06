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

package com.spectralogic.ds3contractcomparator.print.utils;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.rows.Row;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static com.spectralogic.ds3autogen.utils.NormalizingContractNamesUtil.removePath;

/**
 * Utilities for generating the {@link ImmutableList} of {@link Row} which
 * represent Ds3 Objects using reflection.
 */
public final class HtmlRowGeneratorUtils {

    private static final Logger LOG = LoggerFactory.getLogger(HtmlRowGeneratorUtils.class);

    private HtmlRowGeneratorUtils() {
        //pass
    }

    /**
     * Retrieves the specified property of type {@link ImmutableList}
     */
    public static <T, N> ImmutableList<N> getListPropertyFromObject(final Field field, final T object) {
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
            LOG.warn("Could not retrieve list element {} from object of class {}: {}",
                    field.getName(),
                    object.getClass().toString(),
                    e.getMessage());

            return ImmutableList.of();
        }
    }

    /**
     * Retrieves the unique property from the Ds3 object
     */
    public static <T> String getUniqueProperty(final T object) {
        if (object == null) {
            throw new IllegalArgumentException("Cannot get unique property from null object");
        }
        //Most Ds3 modules have a name parameter
        if (PropertyUtils.isReadable(object, "name")) {
            return "name";
        }
        //Used for Ds3ResponseCode
        if (PropertyUtils.isReadable(object, "code")) {
            return "code";
        }
        //Used for Ds3ResponseType
        if (PropertyUtils.isReadable(object, "type")) {
            return "type";
        }
        //Unknown unique property
        LOG.warn("Unknown unique property for class {}", object.getClass().toString());
        return "";
    }

    /**
     * Retrieves the indentation that should be used with the specified field. If the
     * field is the unique property of an object, then the indent remains the same,
     * else, if it is non-unique, then the indent is increased by one.
     */
    public static <T> int toFieldIndent(final int curIndent, final T object, final Field field) {
        final String uniqueProperty = getUniqueProperty(object);
        return field.getName().equals(uniqueProperty) ? curIndent : curIndent + 1;
    }

    /**
     * Gets the property value from the specified object. If the property is "name" or "type", then
     * any path will be removed. If the property does not exist within the object, than an empty
     * optional is returned.
     */
    public static <T> Optional<String> getPropertyValue(final T object, final String property) {
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
            LOG.warn("Could not retrieve property {} from class {}: {}",
                    property,
                    object.getClass().toString(),
                    e.getMessage());

            return Optional.empty();
        }
    }
}
