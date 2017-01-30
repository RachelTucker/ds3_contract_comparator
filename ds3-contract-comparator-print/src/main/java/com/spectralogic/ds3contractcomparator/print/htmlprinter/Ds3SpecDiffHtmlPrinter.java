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

package com.spectralogic.ds3contractcomparator.print.htmlprinter;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Request;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Type;
import com.spectralogic.ds3contractcomparator.models.Ds3ApiSpecDiff;
import com.spectralogic.ds3contractcomparator.models.request.*;
import com.spectralogic.ds3contractcomparator.print.Ds3SpecDiffPrinter;
import com.spectralogic.ds3contractcomparator.print.utils.WriterHelper;

import java.io.Writer;

import static com.spectralogic.ds3contractcomparator.print.htmlprinter.Ds3RequestDiffHtmlPrinter.printAddedRequest;
import static com.spectralogic.ds3contractcomparator.print.htmlprinter.Ds3RequestDiffHtmlPrinter.printDeletedRequest;
import static com.spectralogic.ds3contractcomparator.print.htmlprinter.Ds3RequestDiffHtmlPrinter.printModifiedRequest;
import static com.spectralogic.ds3contractcomparator.print.utils.HtmlPrinterUtils.printIndex;

/**
 * Prints the contents of a {@link Ds3ApiSpecDiff} into an HTML formatted report.
 * Prints {@link Ds3Request} and {@link Ds3Type} that were added, deleted and modified.
 * All items that were not changed between contract versions are not printed.
 */
public class Ds3SpecDiffHtmlPrinter implements Ds3SpecDiffPrinter {

    private final String olderFileName;
    private final String newerFileName;
    private final WriterHelper writer;

    private static final String BEGINNING_OF_REPORT = "<html>\n" +
            "<head>\n" +
            "<style>\n" +
            "table {\n" +
            "  width: 50em;\n" +
            "}\n" +
            "table, th, td {\n" +
            "  border: 1px solid black;\n" +
            "  border-collapse: collapse;\n" +
            "}\n" +
            "</style>\n" +
            "</head>\n" +
            "<body>\n" +
            "<h1>Contract Comparison</h1>\n";

    private static final String END_OF_REPORT = "</body>\n</html>";

    //TODO strip enum properties before creating diff
    public Ds3SpecDiffHtmlPrinter(
            final String olderFileName,
            final String newerFileName,
            final Writer writer) {
        this.olderFileName = olderFileName;
        this.newerFileName = newerFileName;
        this.writer = new WriterHelper(writer);
    }

    /**
     * Prints all {@link Ds3Request} and {@link Ds3Type} that were changed between
     * contract versions into an HTML format.
     */
    @Override
    //todo test
    public void print(final Ds3ApiSpecDiff specDiff) {
        writer.append(BEGINNING_OF_REPORT);
        writer.append("<h2>" + olderFileName + " VS " + newerFileName + "</h2>\n");
        printIndex(specDiff, writer);
        printRequests(specDiff.getRequests(), writer);
        writer.append(END_OF_REPORT);
    }

    //todo test
    protected static void printRequests(final ImmutableList<AbstractDs3RequestDiff> requestDiffs, final WriterHelper writer) {
        //Print modified requests
        writer.append("<h2>Modified Commands</h2>");

        requestDiffs.stream()
                .filter(requestDiff -> requestDiff instanceof ModifiedDs3RequestDiff)
                .forEach(requestDiff -> printModifiedRequest(
                        requestDiff.getOldDs3Request(),
                        requestDiff.getNewDs3Request(),
                        writer));

        //Print deleted requests
        writer.append("<h2>Deleted Commands</h2>");

        requestDiffs.stream()
                .filter(requestDiff -> requestDiff instanceof DeletedDs3RequestDiff)
                .forEach(requestDiff -> printDeletedRequest(
                        requestDiff.getOldDs3Request(),
                        writer));

        //Print added requests
        writer.append("<h2>Added Commands</h2>");

        requestDiffs.stream()
                .filter(requestDiff -> requestDiff instanceof AddedDs3RequestDiff)
                .forEach(requestDiff -> printAddedRequest(
                        requestDiff.getNewDs3Request(),
                        writer));
    }
}
