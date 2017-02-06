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

import com.spectralogic.ds3autogen.api.models.apispec.Ds3Request;
import com.spectralogic.ds3autogen.api.models.enums.Classification;
import com.spectralogic.ds3contractcomparator.models.request.*;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.Table;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.rows.RowConstants;

import static com.spectralogic.ds3contractcomparator.print.htmlprinter.generators.row.AddedHtmlRowGenerator.createAddedRows;
import static com.spectralogic.ds3contractcomparator.print.htmlprinter.generators.row.DeletedHtmlRowGenerator.createDeletedRows;
import static com.spectralogic.ds3contractcomparator.print.htmlprinter.generators.row.ModifiedHtmlRowGenerator.createModifiedRows;
import static com.spectralogic.ds3contractcomparator.print.utils.HtmlPrinterUtils.toRequestAnchor;
import static com.spectralogic.ds3contractcomparator.print.utils.HtmlPrinterUtils.toRequestTitle;

/**
 * Generates the {@link Table} that represents the HTML representation of a {@link Ds3RequestDiff}.
 */
public final class HtmlRequestTableGenerator {

    /**
     * Converts a {@link AbstractDs3RequestDiff} into a {@link Table} if it is
     * either a {@link AddedDs3RequestDiff}, {@link DeletedDs3RequestDiff}, or
     * a {@link ModifiedDs3RequestDiff}. Else an exception is thrown.
     */
    public static Table toRequestTable(final AbstractDs3RequestDiff requestDiff) {
        if (requestDiff instanceof AddedDs3RequestDiff) {
            return toAddedRequestTable(requestDiff);
        }
        if (requestDiff instanceof DeletedDs3RequestDiff) {
            return toDeletedRequestTable(requestDiff);
        }
        if (requestDiff instanceof ModifiedDs3RequestDiff) {
            return toModifiedRequestTable(requestDiff);
        }
        throw new IllegalArgumentException("Cannot convert AbstractDs3RequestDiff of class: " + requestDiff.getClass());
    }

    /**
     * Converts a {@link AddedDs3RequestDiff} into a {@link Table} representing
     * the {@link Ds3Request} that was added.
     */
    static Table toAddedRequestTable(final AbstractDs3RequestDiff requestDiff) {
        if (!(requestDiff instanceof AddedDs3RequestDiff)) {
            throw new IllegalArgumentException("Only converts classes of AddedDs3RequestDiff to Table, does not support: " + requestDiff.getClass());
        }
        final Ds3Request added = requestDiff.getNewDs3Request();

        return new Table(
                toRequestTitle(added.getName(), added.getClassification()),
                toRequestAnchor(added.getName(), added.getClassification()),
                createAddedRows(added, RowConstants.STARTING_INDENT));
    }

    /**
     * Converts a {@link DeletedDs3RequestDiff} into a {@link Table} representing
     * the {@link Ds3Request} that was removed.
     */
    static Table toDeletedRequestTable(final AbstractDs3RequestDiff requestDiff) {
        if (!(requestDiff instanceof DeletedDs3RequestDiff)) {
            throw new IllegalArgumentException("Only converts classes of DeletedDs3RequestDiff to Table, does not support: " + requestDiff.getClass());
        }
        final Ds3Request deleted = requestDiff.getOldDs3Request();
        return new Table(
                toRequestTitle(deleted.getName(), deleted.getClassification()),
                toRequestAnchor(deleted.getName(), deleted.getClassification()),
                createDeletedRows(deleted, RowConstants.STARTING_INDENT));
    }

    /**
     * Converts a {@link ModifiedDs3RequestDiff} into a {@link Table} representing
     * the changes between versions of the {@link Ds3Request}.
     */
    static Table toModifiedRequestTable(final AbstractDs3RequestDiff requestDiff) {
        if (!(requestDiff instanceof ModifiedDs3RequestDiff)) {
            throw new IllegalArgumentException("Only converts classes of ModifiedDs3RequestDiff to Table, does not support: " + requestDiff.getClass());
        }

        final String name = requestDiff.getNewDs3Request().getName();
        final Classification classification = requestDiff.getNewDs3Request().getClassification();
        return new Table(
                toRequestTitle(name, classification),
                toRequestAnchor(name, classification),
                createModifiedRows(requestDiff.getOldDs3Request(), requestDiff.getNewDs3Request(), RowConstants.STARTING_INDENT));
    }
}
