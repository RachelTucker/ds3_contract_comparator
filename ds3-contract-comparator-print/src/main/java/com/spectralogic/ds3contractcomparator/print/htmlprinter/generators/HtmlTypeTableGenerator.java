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

import com.spectralogic.ds3autogen.api.models.apispec.Ds3Type;
import com.spectralogic.ds3contractcomparator.models.type.*;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.Table;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.rows.RowConstants;

import static com.spectralogic.ds3autogen.utils.NormalizingContractNamesUtil.removePath;
import static com.spectralogic.ds3contractcomparator.print.htmlprinter.generators.row.AddedHtmlRowGenerator.createAddedRows;
import static com.spectralogic.ds3contractcomparator.print.htmlprinter.generators.row.DeletedHtmlRowGenerator.createDeletedRows;
import static com.spectralogic.ds3contractcomparator.print.htmlprinter.generators.row.ModifiedHtmlRowGenerator.createModifiedRows;

/**
 * Generates the {@link Table} that represents the HTML representation of a {@link Ds3TypeDiff}.
 */
public final class HtmlTypeTableGenerator {

    private HtmlTypeTableGenerator() {
        //pass
    }

    /**
     * Converts a {@link AbstractDs3TypeDiff} into a {@link Table} if it is
     * either an {@link AddedDs3TypeDiff}, {@link DeletedDs3TypeDiff}, or
     * a {@link ModifiedDs3TypeDiff}. Else an exception is thrown.
     */
    public static Table toTypeTable(final AbstractDs3TypeDiff typeDiff) {
        if (typeDiff instanceof AddedDs3TypeDiff) {
            return toAddedTypeTable(typeDiff);
        }
        if (typeDiff instanceof DeletedDs3TypeDiff) {
            return toDeletedTypeTable(typeDiff);
        }
        if (typeDiff instanceof ModifiedDs3TypeDiff) {
            return toModifiedTypeTable(typeDiff);
        }
        throw new IllegalArgumentException("Cannot convert AbstractDs3TypeDiff of class: " + typeDiff.getClass());
    }

    /**
     * Converts a {@link AddedDs3TypeDiff} into a {@link Table} representing
     * the {@link Ds3Type} that was added.
     */
    static Table toAddedTypeTable(final AbstractDs3TypeDiff typDiff) {
        if (!(typDiff instanceof AddedDs3TypeDiff)) {
            throw new IllegalArgumentException("Only converts classes of AddedDs3TypeDiff to Table, does not support: " + typDiff.getClass());
        }
        final Ds3Type added = typDiff.getNewDs3Type();
        final String name = removePath(added.getName());
        return new Table(name, name, createAddedRows(added, RowConstants.STARTING_INDENT));
    }

    /**
     * Converts a {@link DeletedDs3TypeDiff} into a {@link Table} representing
     * the {@link Ds3Type} that was removed.
     */
    static Table toDeletedTypeTable(final AbstractDs3TypeDiff typeDiff) {
        if (!(typeDiff instanceof DeletedDs3TypeDiff)) {
            throw new IllegalArgumentException("Only converts classes of DeletedDs3TypeDiff to Table, does not support: " + typeDiff.getClass());
        }
        final Ds3Type deleted = typeDiff.getOldDs3Type();
        final String name = removePath(deleted.getName());
        return new Table(name, name, createDeletedRows(deleted, RowConstants.STARTING_INDENT));
    }

    /**
     * Converts a {@link ModifiedDs3TypeDiff} into a {@link Table} representing
     * the changes between versions of the {@link Ds3Type}.
     */
    static Table toModifiedTypeTable(final AbstractDs3TypeDiff typeDiff) {
        if (!(typeDiff instanceof ModifiedDs3TypeDiff)) {
            throw new IllegalArgumentException("Only converts classes of ModifiedDs3TypeDiff to Table, does not support: " + typeDiff.getClass());
        }

        final String name = removePath(typeDiff.getNewDs3Type().getName());
        return new Table(
                name,
                name,
                createModifiedRows(typeDiff.getOldDs3Type(), typeDiff.getNewDs3Type(), RowConstants.STARTING_INDENT));
    }
}
