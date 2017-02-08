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
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.RowAttributes;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.rows.AddedRow;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.rows.Row;

import java.util.function.Function;

import static com.spectralogic.ds3contractcomparator.print.htmlprinter.generators.row.HtmlRowGenerator.createRows;

/**
 * Utilities for generating the {@link ImmutableList} of {@link Row} which
 * represent added Ds3 Objects using reflection.
 */
public final class AddedHtmlRowGenerator {

    private AddedHtmlRowGenerator() {
        //pass
    }

    /**
     * Recursively traverse an object using reflection and construct the added
     * rows that represent the object.
     */
    public static <T> ImmutableList<Row> createAddedRows(final T object, final int indent) {
        final Function<RowAttributes, Row> toAddedRowFunc = (rowAttributes ->
                new AddedRow(rowAttributes.getIndent(), rowAttributes.getProperty(), rowAttributes.getValue()));

        return createRows(object, indent, toAddedRowFunc);
    }
}
