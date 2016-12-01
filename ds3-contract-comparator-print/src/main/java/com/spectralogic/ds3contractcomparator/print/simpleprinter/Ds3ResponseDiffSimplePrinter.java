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

import com.spectralogic.ds3autogen.api.models.apispec.Ds3ResponseCode;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3ResponseType;
import com.spectralogic.ds3contractcomparator.print.utils.WriterHelper;
import org.jetbrains.annotations.Nullable;

import static com.spectralogic.ds3autogen.utils.NormalizingContractNamesUtil.removePath;
import static com.spectralogic.ds3contractcomparator.print.utils.SimplePrinterUtils.printAddedLine;
import static com.spectralogic.ds3contractcomparator.print.utils.SimplePrinterUtils.printDeletedLine;
import static com.spectralogic.ds3contractcomparator.print.utils.SimplePrinterUtils.printModifiedLine;

/**
 * Prints the difference in a {@link Ds3ResponseCode} between two versions of a contract.
 * This assumes that a {@link Ds3ResponseCode} contains exactly one {@link Ds3ResponseType}
 * because this should already be verified during the parsing of the contract.
 */
final class Ds3ResponseDiffSimplePrinter {

    private static final int INDENT = 2;

    //todo test
    /**
     * Prints the changes in a {@link Ds3ResponseCode} between two versions of a contract.
     * @param oldCode The {@link Ds3ResponseCode} in the older version of the contract if exists, else null.
     * @param newCode The {@link Ds3ResponseCode} in the newer version of the contract if exists, else null.
     */
    static void printResponseCodeDiff(
            @Nullable final Ds3ResponseCode oldCode,
            @Nullable final Ds3ResponseCode newCode,
            final WriterHelper writer) {
        if (oldCode == null && newCode == null) {
            throw new IllegalArgumentException("Cannot print difference between two null Ds3ResponseCodes");
        }
        if (oldCode == null) {
            printAddedCode(newCode, writer);
            return;
        }
        if (newCode == null) {
            printDeletedCode(oldCode, writer);
            return;
        }
        printModifiedCode(oldCode, newCode, writer);
    }

    //todo test
    /**
     * Prints a {@link Ds3ResponseCode} that exists in both versions of the contract, but has been modified
     */
    private static void printModifiedCode(final Ds3ResponseCode oldCode, final Ds3ResponseCode newCode, final WriterHelper writer) {
        final Ds3ResponseType oldResponseType = oldCode.getDs3ResponseTypes().get(0);
        final Ds3ResponseType newResponseType = newCode.getDs3ResponseTypes().get(0);
        printModifiedLine("ResponseCode:", oldCode.getCode(), newCode.getCode(), INDENT, writer);
        printModifiedLine("Type:", removePath(oldResponseType.getType()), removePath(newResponseType.getType()), INDENT + 1, writer);
        printModifiedLine("ComponentType:", removePath(oldResponseType.getComponentType()), removePath(newResponseType.getComponentType()), INDENT + 1, writer);
    }

    //todo test
    /**
     * Prints a {@link Ds3ResponseCode} that exists in the older contract but not in the newer contract
     */
    private static void printDeletedCode(final Ds3ResponseCode oldCode, final WriterHelper writer) {
        final Ds3ResponseType oldResponseType = oldCode.getDs3ResponseTypes().get(0);
        printModifiedLine("ResponseCode:", Integer.toString(oldCode.getCode()), "N/A", INDENT, writer);
        printDeletedLine("Type:", removePath(oldResponseType.getType()), INDENT + 1, writer);
        printDeletedLine("ComponentType:", removePath(oldResponseType.getComponentType()), INDENT + 1, writer);
    }

    //todo test
    /**
     * Prints a {@link Ds3ResponseCode} that exists in the newer contract but not in the older contract
     */
    private static void printAddedCode(final Ds3ResponseCode newCode, final WriterHelper writer) {
        final Ds3ResponseType newResponseType = newCode.getDs3ResponseTypes().get(0);
        if (newResponseType == null) {
            throw new IllegalArgumentException("Ds3ResponseCode does not have an associated Ds3ResponseType: " + newCode.getCode());
        }
        printModifiedLine("ResponseCode:", "N/A", Integer.toString(newCode.getCode()), INDENT, writer);
        printAddedLine("Type:", removePath(newResponseType.getType()), INDENT + 1, writer);
        printAddedLine("ComponentType:", removePath(newResponseType.getComponentType()), INDENT + 1, writer);
    }
}
