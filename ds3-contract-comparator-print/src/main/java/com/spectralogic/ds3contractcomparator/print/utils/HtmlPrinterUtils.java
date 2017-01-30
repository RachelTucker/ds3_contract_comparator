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

import com.spectralogic.ds3autogen.api.models.enums.Classification;
import com.spectralogic.ds3contractcomparator.models.Ds3ApiSpecDiff;
import com.spectralogic.ds3contractcomparator.models.request.AddedDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.request.DeletedDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.request.ModifiedDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.type.AddedDs3TypeDiff;
import com.spectralogic.ds3contractcomparator.models.type.DeletedDs3TypeDiff;
import com.spectralogic.ds3contractcomparator.models.type.ModifiedDs3TypeDiff;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.Ds3SpecDiffHtmlPrinter;

import static com.spectralogic.ds3autogen.utils.ConverterUtil.hasContent;
import static com.spectralogic.ds3autogen.utils.ConverterUtil.isEmpty;
import static com.spectralogic.ds3autogen.utils.NormalizingContractNamesUtil.removePath;

/**
 * Contains util functions used to format output for {@link Ds3SpecDiffHtmlPrinter}
 */
public final class HtmlPrinterUtils {

    private HtmlPrinterUtils() {
        //pass
    }

    public static void printIndex(final Ds3ApiSpecDiff specDiff, final WriterHelper writer) {
        //print modified commands
        writer.append("<h3>Index</h3>\n" +
                "<table>\n" +
                "<tr><th>Modified Commands</th><tr>\n");

        specDiff.getRequests().stream()
                .filter(request -> request instanceof ModifiedDs3RequestDiff)
                .forEach(requestDiff -> printIndexEntryRequest(
                        requestDiff.getOldDs3Request().getName(),
                        requestDiff.getOldDs3Request().getClassification(),
                        writer));

        //print deleted commands
        writer.append("<tr><th>Deleted Commands</th><tr>\n");

        specDiff.getRequests().stream()
                .filter(request -> request instanceof DeletedDs3RequestDiff)
                .forEach(requestDiff -> printIndexEntryRequest(
                        requestDiff.getOldDs3Request().getName(),
                        requestDiff.getOldDs3Request().getClassification(),
                        writer));

        //print added commands
        writer.append("<tr><th>Added Commands</th><tr>\n");

        specDiff.getRequests().stream()
                .filter(request -> request instanceof AddedDs3RequestDiff)
                .forEach(requestDiff -> printIndexEntryRequest(
                        requestDiff.getNewDs3Request().getName(),
                        requestDiff.getNewDs3Request().getClassification(),
                        writer));

        //print modified types
        writer.append("<tr><th>Modified Types</th><tr>\n");

        specDiff.getTypes().stream()
                .filter(type -> type instanceof ModifiedDs3TypeDiff)
                .forEach(typeDiff -> printIndexEntryType(
                        typeDiff.getNewDs3Type().getName(),
                        writer));

        //print deleted types
        writer.append("<tr><th>Deleted Types</th><tr>\n");

        specDiff.getTypes().stream()
                .filter(type -> type instanceof DeletedDs3TypeDiff)
                .forEach(typeDiff -> printIndexEntryType(
                        typeDiff.getOldDs3Type().getName(),
                        writer));

        //print added types
        writer.append("<tr><th>Added Types</th><tr>\n");

        specDiff.getTypes().stream()
                .filter(type -> type instanceof AddedDs3TypeDiff)
                .forEach(typeDiff -> printIndexEntryType(
                        typeDiff.getNewDs3Type().getName(),
                        writer));

        //close table
        writer.append("</table>\n");
    }

    private static void printIndexEntryType(final String typeName, final WriterHelper writer) {
        final String name = removePath(typeName);
        writer.append("<tr>\n" +
                "  <td><a href=\"#" + name + "\">" + name + "</a></td>\n" +
                "</tr>\n");
    }

    private static void printIndexEntryRequest(
            final String requestName,
            final Classification classification,
            final WriterHelper writer) {
        final String name = removePath(requestName);
        writer.append("<tr>\n" +
                "  <td><a href=\"#" + createRequestAnchor(name, classification) + "\">" + name + " (" + classification.toString() + ")</a></td>\n" +
                "</tr>\n");
    }

    /**
     * Creates the HTML anchor for a request used in creating internal links
     */
    public static String createRequestAnchor(final String requestName, final Classification classification) {
        return requestName + classification.toString();
    }

    final static String TABLE_ENTRY = "<tr>\n" +
            "  <td style=\"padding-left:%dem;\">%s</td>\n" +
            "  <td style=\"padding-left:%dem;%s\">%s</td>\n" +
            "  <td style=\"padding-left:%dem;%s\">%s</td>\n" +
            "</tr>\n";

    final static String ADDED_COLOR = "background-color:lightgreen;";
    final static String DELETED_COLOR = "background-color:tomato;";
    final static String NO_COLOR = "";
    final static String NA = "N/A";

    //TODO test
    /**
     * Creates the HTML for a table row containing an added value
     */
    public static String createAddedEntry(final String label, final boolean value, final int indent) {
        return createAddedEntry(
                label,
                Boolean.toString(value),
                indent);
    }

    //TODO test
    /**
     * Creates the HTML for a table row containing an added value
     */
    public static String createAddedEntry(final String label, final Enum<?> value, final int indent) {
        return createAddedEntry(
                label,
                value != null ? value.toString() : "",
                indent);
    }

    /**
     * Creates the HTML for a table row containing an added value
     * @param label The element label of the item that was added
     * @param value The value of the element that was added
     * @param indent The indentation level of the element
     */
    public static String createAddedEntry(final String label, final String value, final int indent) {
        if (isEmpty(value)) {
            //Don't print an empty value
            return "";
        }
        return String.format(TABLE_ENTRY,
                indent, label,
                indent, NO_COLOR, NA,
                indent, ADDED_COLOR, value);
    }

    //TODO test
    /**
     * Creates the HTML for a table row containing a deleted value
     */
    public static String createDeletedEntry(final String label, final boolean value, final int indent) {
        return createDeletedEntry(
                label,
                Boolean.toString(value),
                indent);
    }

    //TODO test
    /**
     * Creates the HTML for a table row containing a deleted value
     */
    public static String createDeletedEntry(final String label, final Enum<?> value, final int indent) {
        return createDeletedEntry(
                label,
                value != null ? value.toString() : "",
                indent);
    }

    /**
     * Creates the HTML for a table row containing a deleted value
     * @param label The element label of the item hat was deleted
     * @param value The value of the element that was deleted
     * @param indent The indentation level of the element
     */
    public static String createDeletedEntry(final String label, final String value, final int indent) {
        if (isEmpty(value)) {
            //Don't print an empty value
            return "";
        }
        return String.format(TABLE_ENTRY,
                indent, label,
                indent, DELETED_COLOR, value,
                indent, NO_COLOR, NA);
    }

    //TODO test
    public static String createModifiedEntry(
            final String label,
            final boolean oldValue,
            final boolean newValue,
            final int indent) {
        return createModifiedEntry(
                label,
                Boolean.toString(oldValue),
                Boolean.toString(newValue),
                indent);
    }

    //TODO test
    public static String createModifiedEntry(
            final String label,
            final Enum<?> oldValue,
            final Enum<?> newValue,
            final int indent) {
        return createModifiedEntry(
                label,
                oldValue != null ? oldValue.toString() : "",
                newValue != null ? newValue.toString() : "",
                indent);
    }

    public static String createModifiedEntry(
            final String label,
            final String oldValue,
            final String newValue,
            final int indent) {
        if (isEmpty(oldValue) && isEmpty(newValue)) {
            //Don't print an empty value
            return "";
        }
        if (oldValue.equals(newValue)) { //todo test
            //Nothing changed, print without color highlights
            return createNoChangeEntry(label, oldValue, indent);
        }
        return String.format(TABLE_ENTRY,
                indent, label,
                indent, DELETED_COLOR, hasContent(oldValue) ? oldValue : NA,
                indent, ADDED_COLOR, hasContent(newValue) ? newValue : NA);
    }

    //TODO test
    public static String createNoChangeEntry(
            final String label,
            final String value,
            final int indent) {
        if (isEmpty(value)) {
            //Don't print an empty value
            return "";
        }
        return String.format(TABLE_ENTRY,
                indent, label,
                indent, NO_COLOR, value,
                indent, NO_COLOR, value);
    }
}
