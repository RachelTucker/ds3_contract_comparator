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
import com.spectralogic.ds3contractcomparator.models.request.ModifiedDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.Ds3SpecDiffHtmlPrinter;

/**
 * Contains util functions used to format output for {@link Ds3SpecDiffHtmlPrinter}
 */
public final class HtmlPrinterUtils {

    private HtmlPrinterUtils() {
        //pass
    }

    public static void printIndex(final Ds3ApiSpecDiff specDiff, final WriterHelper writer) {
        writer.append("<h3>Index</h3>\n" +
                "<table>\n" +
                "<tr><th>Modified Commands</th><tr>\n");

        specDiff.getRequests().stream()
                .filter(request -> request instanceof ModifiedDs3RequestDiff)
                .forEach(requestDiff -> printIndexEntry(
                        requestDiff.getOldDs3Request().getName(),
                        requestDiff.getOldDs3Request().getClassification(),
                        writer));

        writer.append("");
    }

    //todo test
    protected static void printIndexEntry(final String requestName, final Classification classification, final WriterHelper writer) {
        writer.append("<tr>\n" +
                "  <td><a href=\"#" + createRequestAnchor(requestName, classification) + "\">" + requestName + " (" + classification.toString() + ")</a></td>\n" +
                "</tr>\n");
    }

    //todo test
    protected static String createRequestAnchor(final String requestName, final Classification classification) {
        return requestName + classification.toString();
    }
}
