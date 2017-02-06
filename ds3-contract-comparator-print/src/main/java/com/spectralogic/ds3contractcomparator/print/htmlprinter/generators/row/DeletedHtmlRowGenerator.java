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
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.DeletedRow;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.rows.NoChangeRow;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.rows.Row;

import java.lang.reflect.Field;
import java.util.Optional;

import static com.spectralogic.ds3contractcomparator.print.utils.HtmlRowGeneratorUtils.getListPropertyFromObject;
import static com.spectralogic.ds3contractcomparator.print.utils.HtmlRowGeneratorUtils.getPropertyValue;
import static com.spectralogic.ds3contractcomparator.print.utils.HtmlRowGeneratorUtils.toFieldIndent;

/**
 * Utilities for generating the {@link ImmutableList} of {@link Row} which
 * represent deleted Ds3 Objects using reflection.
 */
public final class DeletedHtmlRowGenerator {

    private DeletedHtmlRowGenerator() {
        //pass
    }

    /**
     * Recursively traverse an object using reflection and construct the deleted
     * rows that represent the object.
     */
    public static <T> ImmutableList<Row> createDeletedRows(final T object, final int indent) {
        final Field[] fields = object.getClass().getDeclaredFields();
        final ImmutableList.Builder<Row> builder = ImmutableList.builder();

        for (final Field field : fields) {
            final String property = field.getName();
            final Optional<String> value = getPropertyValue(object, property);
            final int fieldIndent = toFieldIndent(indent, object, field);
            if (value.isPresent()) {
                if (field.getType() == ImmutableList.class) {
                    builder.add(new NoChangeRow(fieldIndent, property, ""));

                    final ImmutableList<?> objList = getListPropertyFromObject(field, object);
                    objList.forEach(obj -> builder.addAll(createDeletedRows(obj, fieldIndent + 1)));
                } else {
                    builder.add(new DeletedRow(fieldIndent, property, value.get()));
                }
            }
        }
        return builder.build();
    }
}
