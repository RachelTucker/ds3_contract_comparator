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

package com.spectralogic.ds3contractcomparator.print.simpleprinter;

import com.spectralogic.ds3autogen.api.models.apispec.Ds3Property;
import com.spectralogic.ds3contractcomparator.print.utils.WriterHelper;
import org.jetbrains.annotations.Nullable;

import static com.spectralogic.ds3autogen.utils.NormalizingContractNamesUtil.removePath;
import static com.spectralogic.ds3contractcomparator.print.utils.SimplePrinterUtils.printAddedLine;
import static com.spectralogic.ds3contractcomparator.print.utils.SimplePrinterUtils.printDeletedLine;
import static com.spectralogic.ds3contractcomparator.print.utils.SimplePrinterUtils.printModifiedLine;

/**
 * Prints the difference in a {@link Ds3Property} between two versions of a contract
 */
class Ds3PropertyDiffSimplePrinter {

    private static final int INDENT = 4;

    /**
     * Prints the difference in a {@link Ds3Property} between two versions of a contract
     */
    static void printPropertyDiff(
            @Nullable Ds3Property oldProperty,
            @Nullable Ds3Property newProperty,
            final WriterHelper writer) {
        if (oldProperty == null && newProperty == null) {
            throw new IllegalArgumentException("Cannot print difference between two null Ds3Elements");
        }
        if (oldProperty == null) {
            printAddedEnumConstant(newProperty, writer);
            return;
        }
        if (newProperty == null) {
            printDeletedEnumConstant(oldProperty, writer);
            return;
        }
        printModifiedEnumConstant(oldProperty, newProperty, writer);
    }

    /**
     * Prints a {@link Ds3Property} that exists in the newer contract but not in the older contract
     */
    private static void printAddedEnumConstant(final Ds3Property newProperty, final WriterHelper writer) {
        printModifiedLine("PropertyName:", "N/A", newProperty.getName(), INDENT, writer);
        printAddedLine("Value:", newProperty.getValue(), INDENT + 1, writer);
        printAddedLine("ValueType:", removePath(newProperty.getValueType()), INDENT + 1, writer);
    }

    /**
     * Prints a {@link Ds3Property} that exists in the older contract but not in the newer contract
     */
    private static void printDeletedEnumConstant(final Ds3Property oldProperty, final WriterHelper writer) {
        printModifiedLine("PropertyName:", oldProperty.getName(), "N/A", INDENT, writer);
        printDeletedLine("Value:", oldProperty.getValue(), INDENT + 1, writer);
        printDeletedLine("ValueType:", removePath(oldProperty.getValueType()), INDENT + 1, writer);
    }

    /**
     * Prints a {@link Ds3Property} that was modified between contract versions
     */
    private static void printModifiedEnumConstant(
            final Ds3Property oldProperty,
            final Ds3Property newProperty,
            final WriterHelper writer) {
        printModifiedLine("PropertyName:", oldProperty.getName(), newProperty.getName(), INDENT, writer);
        printModifiedLine("Value:", oldProperty.getValue(), newProperty.getValue(), INDENT + 1, writer);
        printModifiedLine("ValueType:", removePath(oldProperty.getValueType()), removePath(newProperty.getValueType()), INDENT + 1, writer);
    }
}
