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

    //todo test
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

    //TODO test
    /**
     * Prints a {@link Ds3Property} that exists in the newer contract but not in the older contract
     */
    private static void printAddedEnumConstant(final Ds3Property newProperty, final WriterHelper writer) {
        printModifiedLine("PropertyName:", "N/A", newProperty.getName(), INDENT, writer);
        printAddedLine("Value:", newProperty.getValue(), INDENT + 1, writer);
        printAddedLine("ValueType:", removePath(newProperty.getValueType()), INDENT + 1, writer);
    }

    //TODO test
    /**
     * Prints a {@link Ds3Property} that exists in the older contract but not in the newer contract
     */
    private static void printDeletedEnumConstant(final Ds3Property oldProperty, final WriterHelper writer) {
        printModifiedLine("PropertyName:", oldProperty.getName(), "N/A", INDENT, writer);
        printDeletedLine("Value:", oldProperty.getValue(), INDENT + 1, writer);
        printDeletedLine("ValueType:", removePath(oldProperty.getValueType()), INDENT + 1, writer);
    }

    //TODO test
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
