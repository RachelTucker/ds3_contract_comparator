/*
 * ******************************************************************************
 *   Copyright 2016 Spectra Logic Corporation. All Rights Reserved.
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

import com.spectralogic.ds3autogen.api.models.apispec.Ds3Param;
import com.spectralogic.ds3contractcomparator.print.utils.WriterHelper;
import org.jetbrains.annotations.Nullable;

import static com.spectralogic.ds3autogen.utils.NormalizingContractNamesUtil.removePath;
import static com.spectralogic.ds3contractcomparator.print.utils.SimplePrinterUtils.*;

/**
 * Prints the difference in a {@link Ds3Param} between two versions of a contract.
 */
final class Ds3ParamDiffSimplePrinter {

    private static final int INDENT = 2;

    //todo test
    /**
     * Prints the changes in a {@link Ds3Param} between two versions of a contract
     */
    static void printParamDiff(
            @Nullable final Ds3Param oldParam,
            @Nullable final Ds3Param newParam,
            final WriterHelper writer) {
        if (oldParam == null && newParam == null) {
            throw new IllegalArgumentException("Cannot print difference between two null Ds3Params");
        }
        if (oldParam == null) {
            printAddedParam(newParam, writer);
            return;
        }
        if (newParam == null) {
            printDeletedParam(oldParam, writer);
            return;
        }
        printModifiedParam(oldParam, newParam, writer);
    }

    //TODO test
    /**
     * Prints a {@link Ds3Param} that exists in the newer contract but not in the older contract
     */
    private static void printAddedParam(final Ds3Param newParam, final WriterHelper writer) {
        printModifiedLine("ParamName:", "N/A", newParam.getName(), INDENT, writer);
        printAddedLine("Type:", removePath(newParam.getType()), INDENT + 1, writer);
        printAddedLine("Nullable:", newParam.getNullable(), INDENT + 1, writer);
    }

    //TODO test
    /**
     * Prints a {@link Ds3Param} that exists in the older contract but not in the newer contract
     */
    private static void printDeletedParam(final Ds3Param oldParam, final WriterHelper writer) {
        printModifiedLine("ParamName:", oldParam.getName(), "N/A", INDENT, writer);
        printDeletedLine("Type:", removePath(oldParam.getType()), INDENT + 1, writer);
        printDeletedLine("Nullable:", oldParam.getNullable(), INDENT + 1, writer);
    }

    //TODO test
    /**
     * Prints a {@link Ds3Param} that was modified between the contract versions
     */
    private static void printModifiedParam(final Ds3Param oldParam, final Ds3Param newParam, final WriterHelper writer) {
        printModifiedLine("ParamName:", oldParam.getName(), newParam.getName(), INDENT, writer);
        printModifiedLine("Type:", removePath(oldParam.getType()), removePath(newParam.getType()), INDENT + 1, writer);
        printModifiedLine("Nullable:", oldParam.getNullable(), newParam.getNullable(), INDENT + 1, writer);
    }
}
