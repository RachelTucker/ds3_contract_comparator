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
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.regex.Pattern;

import static com.spectralogic.ds3contractcomparator.print.utils.Ds3SpecDiffFixture.getTestSpecDiff;
import static com.spectralogic.ds3contractcomparator.print.utils.HtmlPrinterUtils.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class HtmlPrinterUtils_Test {

    @Test
    public void printIndex_Test() throws IOException {

        final Pattern expectedPattern = Pattern.compile("<h3>Index</h3>" +
                        "\\s+<table>" +
                        "\\s+<tr><th>Modified Commands</th><tr>" +
                        "\\s+<tr>" +
                        "\\s+<td><a href=\"#TestRequestamazons3\">TestRequest \\(amazons3\\)</a></td>" +
                        "\\s+</tr>" +
                        "\\s+<tr><th>Deleted Commands</th><tr>" +
                        "\\s+<tr>" +
                        "\\s+<td><a href=\"#GetBucketRequestHandleramazons3\">GetBucketRequestHandler \\(amazons3\\)</a></td>" +
                        "\\s+</tr>" +
                        "\\s+<tr><th>Added Commands</th><tr>" +
                        "\\s+<tr>" +
                        "\\s+<td><a href=\"#HeadBucketRequestHandleramazons3\">HeadBucketRequestHandler \\(amazons3\\)</a></td>" +
                        "\\s+</tr>" +
                        "\\s+<tr><th>Modified Types</th><tr>" +
                        "\\s+<tr>" +
                        "\\s+<td><a href=\"#ModifiedType\">ModifiedType</a></td>" +
                        "\\s+</tr>" +
                        "\\s+<tr><th>Deleted Types</th><tr>" +
                        "\\s+<tr>" +
                        "\\s+<td><a href=\"#DeletedType\">DeletedType</a></td>" +
                        "\\s+</tr>" +
                        "\\s+<tr><th>Added Types</th><tr>" +
                        "\\s+<tr>" +
                        "\\s+<td><a href=\"#AddedType\">AddedType</a></td>" +
                        "\\s+</tr>" +
                        "\\s+</table>",
                Pattern.MULTILINE | Pattern.UNIX_LINES);

        final Ds3ApiSpecDiff specDiff = getTestSpecDiff();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);
        final Writer writer = new OutputStreamWriter(outputStream);
        final WriterHelper helper = new WriterHelper(writer);

        printIndex(specDiff, helper);
        helper.close();

        final String result = new String(outputStream.toByteArray());
        assertTrue(expectedPattern.matcher(result).find());
    }

    @Test
    public void createRequestAnchor_Test() {
        assertThat(createRequestAnchor("RequestName", Classification.amazons3),
                is("RequestNameamazons3"));

        assertThat(createRequestAnchor("RequestName", Classification.spectrads3),
                is("RequestNamespectrads3"));

        assertThat(createRequestAnchor("RequestName", Classification.spectrainternal),
                is("RequestNamespectrainternal"));
    }

    @Test
    public void createAddedEntry_Test() {
        final String expected = "<tr>\n" +
                "  <td style=\"padding-left:1em;\">Label</td>\n" +
                "  <td style=\"padding-left:1em;\">N/A</td>\n" +
                "  <td style=\"padding-left:1em;background-color:lightgreen;\">Value</td>\n" +
                "</tr>\n";

        final String result = createAddedEntry("Label", "Value", 1);

        assertThat(result, is(expected));
    }

    @Test
    public void createDeletedEntry_Test() {
        final String expected = "<tr>\n" +
                "  <td style=\"padding-left:1em;\">Label</td>\n" +
                "  <td style=\"padding-left:1em;background-color:tomato;\">Value</td>\n" +
                "  <td style=\"padding-left:1em;\">N/A</td>\n" +
                "</tr>\n";

        final String result = createDeletedEntry("Label", "Value", 1);

        assertThat(result, is(expected));
    }

    @Test
    public void createModifiedEntry_Test() {
        final String expected = "<tr>\n" +
                "  <td style=\"padding-left:1em;\">Label</td>\n" +
                "  <td style=\"padding-left:1em;background-color:tomato;\">OldValue</td>\n" +
                "  <td style=\"padding-left:1em;background-color:lightgreen;\">NewValue</td>\n" +
                "</tr>\n";

        final String result = createModifiedEntry("Label", "OldValue", "NewValue", 1);

        assertThat(result, is(expected));
    }
}
