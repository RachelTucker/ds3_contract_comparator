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

    private static final int LABEL_WIDTH = 20;
    private static final int COLUMN_WIDTH = 50;
    private static final int INDENT = 2;

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

    private static void printModifiedCode(final Ds3ResponseCode oldCode, final Ds3ResponseCode newCode, final WriterHelper writer) {
        final Ds3ResponseType oldResponseType = oldCode.getDs3ResponseTypes().get(0);
        final Ds3ResponseType newResponseType = newCode.getDs3ResponseTypes().get(0);
        printModifiedLine("Code:", oldCode.getCode(), newCode.getCode(), LABEL_WIDTH, COLUMN_WIDTH, INDENT, writer);
        printModifiedLine("Type:", removePath(oldResponseType.getType()), removePath(newResponseType.getType()), LABEL_WIDTH, COLUMN_WIDTH, INDENT + 1, writer);
        printModifiedLine("ComponentType:", removePath(oldResponseType.getComponentType()), removePath(newResponseType.getComponentType()), LABEL_WIDTH, COLUMN_WIDTH, INDENT + 1, writer);
    }

    private static void printDeletedCode(final Ds3ResponseCode oldCode, final WriterHelper writer) {
        final Ds3ResponseType oldResponseType = oldCode.getDs3ResponseTypes().get(0);
        printModifiedLine("Code:", Integer.toString(oldCode.getCode()), "N/A", LABEL_WIDTH, COLUMN_WIDTH, INDENT, writer);
        printDeletedLine("Type:", removePath(oldResponseType.getType()), LABEL_WIDTH, COLUMN_WIDTH, INDENT + 1, writer);
        printDeletedLine("ComponentType:", removePath(oldResponseType.getComponentType()), LABEL_WIDTH, COLUMN_WIDTH, INDENT + 1, writer);
    }

    private static void printAddedCode(final Ds3ResponseCode newCode, final WriterHelper writer) {
        final Ds3ResponseType newResponseType = newCode.getDs3ResponseTypes().get(0);
        printModifiedLine("Code:", "N/A", Integer.toString(newCode.getCode()), LABEL_WIDTH, COLUMN_WIDTH, INDENT, writer);
        printAddedLine("Type:", removePath(newResponseType.getType()), LABEL_WIDTH, COLUMN_WIDTH, INDENT + 1, writer);
        printAddedLine("ComponentType:", removePath(newResponseType.getComponentType()), LABEL_WIDTH, COLUMN_WIDTH, INDENT + 1, writer);
    }
}
