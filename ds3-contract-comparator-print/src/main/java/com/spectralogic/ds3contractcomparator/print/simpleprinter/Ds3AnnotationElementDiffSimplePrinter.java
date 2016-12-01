package com.spectralogic.ds3contractcomparator.print.simpleprinter;

import com.spectralogic.ds3autogen.api.models.apispec.Ds3AnnotationElement;
import com.spectralogic.ds3contractcomparator.print.utils.WriterHelper;
import org.jetbrains.annotations.Nullable;

import static com.spectralogic.ds3autogen.utils.NormalizingContractNamesUtil.removePath;
import static com.spectralogic.ds3contractcomparator.print.utils.SimplePrinterUtils.printAddedLine;
import static com.spectralogic.ds3contractcomparator.print.utils.SimplePrinterUtils.printDeletedLine;
import static com.spectralogic.ds3contractcomparator.print.utils.SimplePrinterUtils.printModifiedLine;

/**
 * Prints the difference in a {@link Ds3AnnotationElement} between two versions of a contract
 */
final class Ds3AnnotationElementDiffSimplePrinter {

    private static final int LABEL_WIDTH = 20;
    private static final int COLUMN_WIDTH = 50;
    private static final int INDENT = 6;

    //todo test
    static void printAnnotationElementDiff(
            @Nullable final Ds3AnnotationElement oldElement,
            @Nullable final Ds3AnnotationElement newElement,
            final WriterHelper writer) {
        if (oldElement == null && newElement == null) {
            throw new IllegalArgumentException("Cannot print difference between two null Ds3Elements");
        }
        if (oldElement == null) {
            printAddedAnnotationElement(newElement, writer);
            return;
        }
        if (newElement == null) {
            printDeletedAnnotationElement(oldElement, writer);
            return;
        }
        printModifiedAnnotationElement(oldElement, newElement, writer);
    }

    //TODO test
    /**
     * Prints a {@link Ds3AnnotationElement} that exists in the newer contract but not in the older contract
     */
    private static void printAddedAnnotationElement(final Ds3AnnotationElement newElement, final WriterHelper writer) {
        printModifiedLine("Name:", "N/A", removePath(newElement.getName()), LABEL_WIDTH, COLUMN_WIDTH, INDENT, writer);
        printAddedLine("Value:", removePath(newElement.getValue()), LABEL_WIDTH, COLUMN_WIDTH, INDENT + 1, writer);
        printAddedLine("ValueType:", removePath(newElement.getValueType()), LABEL_WIDTH, COLUMN_WIDTH, INDENT + 1, writer);
    }

    //TODO test
    /**
     * Prints a {@link Ds3AnnotationElement} that exists in the older contract but not in the newer contract
     */
    private static void printDeletedAnnotationElement(final Ds3AnnotationElement oldElement, final WriterHelper writer) {
        printModifiedLine("Name:", removePath(oldElement.getName()), "N/A", LABEL_WIDTH, COLUMN_WIDTH, INDENT, writer);
        printDeletedLine("Value:", removePath(oldElement.getValue()), LABEL_WIDTH, COLUMN_WIDTH, INDENT + 1, writer);
        printDeletedLine("ValueType:", removePath(oldElement.getValueType()), LABEL_WIDTH, COLUMN_WIDTH, INDENT + 1, writer);
    }

    //TODO test
    /**
     * Prints a {@link Ds3AnnotationElement} that was modified between contract versions
     */
    private static void printModifiedAnnotationElement(
            final Ds3AnnotationElement oldElement,
            final Ds3AnnotationElement newElement,
            final WriterHelper writer) {
        printModifiedLine("Name:", removePath(oldElement.getName()), removePath(newElement.getName()), LABEL_WIDTH, COLUMN_WIDTH, INDENT, writer);
        printModifiedLine("Value:", removePath(oldElement.getValue()), removePath(newElement.getValue()), LABEL_WIDTH, COLUMN_WIDTH, INDENT + 1, writer);
        printModifiedLine("ValueType:", removePath(oldElement.getValueType()), removePath(newElement.getValueType()), LABEL_WIDTH, COLUMN_WIDTH, INDENT + 1, writer);
    }
}
