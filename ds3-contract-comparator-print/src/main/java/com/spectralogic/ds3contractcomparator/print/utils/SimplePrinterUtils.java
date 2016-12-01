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

package com.spectralogic.ds3contractcomparator.print.utils;

import com.spectralogic.ds3contractcomparator.print.simpleprinter.Ds3SpecDiffSimplePrinter;
import org.apache.commons.lang3.StringUtils;

import static com.spectralogic.ds3autogen.utils.ConverterUtil.isEmpty;

/**
 * Contains util functions used to format output for {@link Ds3SpecDiffSimplePrinter}
 */
public final class SimplePrinterUtils {

    //TODO test and document
    public static void printDeletedLine(
            final String label,
            final int oldVal,
            final int labelWidth,
            final int columnWidth,
            final int indent,
            final WriterHelper writer) {
        printDeletedLine(
                label,
                Integer.toString(oldVal),
                labelWidth,
                columnWidth,
                indent,
                writer);
    }

    //TODO test and document
    public static void printDeletedLine(
            final String label,
            final boolean oldVal,
            final int labelWidth,
            final int columnWidth,
            final int indent,
            final WriterHelper writer) {
        printDeletedLine(
                label,
                Boolean.toString(oldVal),
                labelWidth,
                columnWidth,
                indent,
                writer);
    }

    //TODO test and document
    public static void printDeletedLine(
            final String label,
            final Enum<?> oldVal,
            final int labelWidth,
            final int columnWidth,
            final int indent,
            final WriterHelper writer) {
        printDeletedLine(
                label,
                oldVal != null ? oldVal.toString() : "",
                labelWidth,
                columnWidth,
                indent,
                writer);
    }

    //TODO test and document
    public static void printDeletedLine(
            final String label,
            final String oldVal,
            final int labelWidth,
            final int columnWidth,
            final int indent,
            final WriterHelper writer) {
        if (isEmpty(oldVal)) {
            //Don't print an empty value
            return;
        }
        final String printFormat = getPrintStringFormat(labelWidth, columnWidth, indent);
        writer.append(String.format(printFormat, label, oldVal, "N/A"));
    }

    //TODO test and document
    public static void printAddedLine(
            final String label,
            final int newVal,
            final int labelWidth,
            final int columnWidth,
            final int indent,
            final WriterHelper writer) {
        printAddedLine(
                label,
                Integer.toString(newVal),
                labelWidth,
                columnWidth,
                indent,
                writer);
    }

    //TODO test and document
    public static void printAddedLine(
            final String label,
            final boolean newVal,
            final int labelWidth,
            final int columnWidth,
            final int indent,
            final WriterHelper writer) {
        printAddedLine(
                label,
                Boolean.toString(newVal),
                labelWidth,
                columnWidth,
                indent,
                writer);
    }

    //TODO test and document
    public static void printAddedLine(
            final String label,
            final Enum<?> newVal,
            final int labelWidth,
            final int columnWidth,
            final int indent,
            final WriterHelper writer) {
        printAddedLine(
                label,
                newVal != null ? newVal.toString() : "",
                labelWidth,
                columnWidth,
                indent,
                writer);
    }

    //TODO test and document
    public static void printAddedLine(
            final String label,
            final String newVal,
            final int labelWidth,
            final int columnWidth,
            final int indent,
            final WriterHelper writer) {
        if (isEmpty(newVal)) {
            //Don't print an empty value
            return;
        }
        final String printFormat = getPrintStringFormat(labelWidth, columnWidth, indent);
        writer.append(String.format(printFormat, label, "N/A", newVal));
    }

    //TODO test and document
    public static void printModifiedLine(
            final String label,
            final int oldVal,
            final int newVal,
            final int labelWidth,
            final int columnWidth,
            final int indent,
            final WriterHelper writer) {
        printModifiedLine(
                label,
                Integer.toString(oldVal),
                Integer.toString(newVal),
                labelWidth,
                columnWidth,
                indent,
                writer);
    }

    //TODO test and document
    public static void printModifiedLine(
            final String label,
            final boolean oldVal,
            final boolean newVal,
            final int labelWidth,
            final int columnWidth,
            final int indent,
            final WriterHelper writer) {
        printModifiedLine(
                label,
                Boolean.toString(oldVal),
                Boolean.toString(newVal),
                labelWidth,
                columnWidth,
                indent,
                writer);
    }

    //TODO test and document
    public static void printModifiedLine(
            final String label,
            final Enum<?> oldVal,
            final Enum<?> newVal,
            final int labelWidth,
            final int columnWidth,
            final int indent,
            final WriterHelper writer) {
        printModifiedLine(
                label,
                oldVal != null ? oldVal.toString() : "",
                newVal != null ? newVal.toString() : "",
                labelWidth,
                columnWidth,
                indent,
                writer);
    }

    //TODO test and document
    public static void printModifiedLine(
            final String label,
            final String oldVal,
            final String newVal,
            final int labelWidth,
            final int columnWidth,
            final int indent,
            final WriterHelper writer) {
        if (isEmpty(oldVal) && isEmpty(newVal)) {
            //Don't print an empty value
            return;
        }
        final String printFormat = getPrintStringFormat(labelWidth, columnWidth, indent);
        if (oldVal.equals(newVal)) {
            writer.append(String.format(printFormat, label, oldVal, newVal));
            return;
        }
        //Print value with arrow emphasizing change
        writer.append(String.format(
                printFormat,
                label,
                StringUtils.rightPad(oldVal, columnWidth - 1, '-') + ">",
                newVal));

    }

    //TODO test and document
    private static String getPrintStringFormat(final int labelWidth, final int columnWidth, final int indent) {
        return indent(indent) + "%-" + labelWidth + "s %-" + columnWidth + "s %-" + columnWidth + "s\n";
    }

    private static final String INDENT = "  ";

    //TODO test and document
    public static String indent(final int indent) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            builder.append(INDENT);
        }
        return builder.toString();
    }
}
