/*
 * ******************************************************************************
 *   Copyright 2016-2017 Spectra Logic Corporation. All Rights Reserved.
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

import com.spectralogic.ds3contractcomparator.models.request.AddedDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.request.Ds3RequestDiff;
import com.spectralogic.ds3contractcomparator.print.utils.WriterHelper;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.regex.Pattern;

import static com.spectralogic.ds3autogen.testutil.Ds3ModelFixtures.getBucketRequest;
import static com.spectralogic.ds3contractcomparator.print.utils.Ds3SpecDiffFixture.*;
import static org.junit.Assert.assertTrue;

public class Ds3RequestDiffSimplePrinter_Test {

    @Test
    public void printRequestDiff_Added_Test() throws IOException {
        final Pattern expectedPattern = Pattern.compile("\\*{20} ADDED REQUEST GetBucketRequestHandler \\(amazons3\\) \\*{20}" +
                "\\s+RequestName:\\s+N/A-+> GetBucketRequestHandler" +
                "\\s+Classification:\\s+N/A\\s+amazons3" +
                "\\s+HttpVerb:\\s+N/A\\s+GET" +
                "\\s+BucketRequirement:\\s+N/A\\s+REQUIRED" +
                "\\s+ObjectRequirement:\\s+N/A\\s+NOT_ALLOWED" +
                "\\s+IncludeInPath:\\s+N/A\\s+false" +
                "\\s+OptionalParameters:" +
                "\\s+ParamName:\\s+N/A-+> Delimiter" +
                "\\s+Type:\\s+N/A\\s+String" +
                "\\s+Nullable:\\s+N/A\\s+true" +
                "\\s+ParamName:\\s+N/A-+> Marker" +
                "\\s+Type:\\s+N/A\\s+String" +
                "\\s+Nullable:\\s+N/A\\s+true" +
                "\\s+ParamName:\\s+N/A-+> MaxKeys" +
                "\\s+Type:\\s+N/A\\s+int" +
                "\\s+Nullable:\\s+N/A\\s+false" +
                "\\s+ParamName:\\s+N/A-+> Prefix" +
                "\\s+Type:\\s+N/A\\s+String" +
                "\\s+Nullable:\\s+N/A\\s+true" +
                "\\s+ResponseCodes:" +
                "\\s+ResponseCode:\\s+N/A-+> 200" +
                "\\s+Type:\\s+N/A\\s+ListBucketResult",
                Pattern.MULTILINE | Pattern.UNIX_LINES);

        final Ds3RequestDiff diff = new AddedDs3RequestDiff(getBucketRequest());

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);
        final Writer writer = new OutputStreamWriter(outputStream);
        final WriterHelper helper = new WriterHelper(writer);

        Ds3RequestDiffSimplePrinter.printRequestDiff(diff, helper);
        helper.close();

        final String result = new String(outputStream.toByteArray());
        assertTrue(expectedPattern.matcher(result).find());
    }

    @Test
    public void printRequestDiff_Deleted_Test() throws IOException {
        final Pattern expectedPattern = Pattern.compile("\\*{20} DELETED REQUEST GetBucketRequestHandler \\(amazons3\\) \\*{20}" +
                "\\s+RequestName:\\s+GetBucketRequestHandler-+> N/A" +
                "\\s+Classification:\\s+amazons3\\s+N/A" +
                "\\s+HttpVerb:\\s+GET\\s+N/A" +
                "\\s+BucketRequirement:\\s+REQUIRED\\s+N/A" +
                "\\s+ObjectRequirement:\\s+NOT_ALLOWED\\s+N/A" +
                "\\s+IncludeInPath:\\s+false\\s+N/A" +
                "\\s+OptionalParameters:" +
                "\\s+ParamName:\\s+Delimiter-+> N/A" +
                "\\s+Type:\\s+String\\s+N/A" +
                "\\s+Nullable:\\s+true\\s+N/A" +
                "\\s+ParamName:\\s+Marker-+> N/A" +
                "\\s+Type:\\s+String\\s+N/A" +
                "\\s+Nullable:\\s+true\\s+N/A" +
                "\\s+ParamName:\\s+MaxKeys-+> N/A" +
                "\\s+Type:\\s+int\\s+N/A" +
                "\\s+Nullable:\\s+false\\s+N/A" +
                "\\s+ParamName:\\s+Prefix-+> N/A" +
                "\\s+Type:\\s+String\\s+N/A" +
                "\\s+Nullable:\\s+true\\s+N/A" +
                "\\s+ResponseCodes:" +
                "\\s+ResponseCode:\\s+200-+> N/A" +
                "\\s+Type:\\s+ListBucketResult\\s+N/A",
                Pattern.MULTILINE | Pattern.UNIX_LINES);

        final Ds3RequestDiff diff = getDeletedRequest();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);
        final Writer writer = new OutputStreamWriter(outputStream);
        final WriterHelper helper = new WriterHelper(writer);

        Ds3RequestDiffSimplePrinter.printRequestDiff(diff, helper);
        helper.close();

        final String result = new String(outputStream.toByteArray());
        assertTrue(expectedPattern.matcher(result).find());
    }

    @Test
    public void printRequestDiff_Modified_Test() throws IOException {
        final Pattern expectedPattern = Pattern.compile("\\*{20} MODIFIED REQUEST TestRequest \\(amazons3\\) \\*{20}" +
                "\\s+RequestName:\\s+TestRequest\\s+TestRequest" +
                "\\s+Classification:\\s+amazons3\\s+amazons3" +
                "\\s+HttpVerb:\\s+DELETE-+> HEAD" +
                "\\s+ObjectRequirement:\\s+-+> NOT_ALLOWED" +
                "\\s+Action:\\s+BULK_DELETE-+>" +
                "\\s+Resource:\\s+ACTIVE_JOB\\s+ACTIVE_JOB" +
                "\\s+ResourceType:\\s+NON_SINGLETON\\s+NON_SINGLETON" +
                "\\s+Operation:\\s+ALLOCATE\\s+ALLOCATE" +
                "\\s+IncludeInPath:\\s+false\\s+false" +
                "\\s+OptionalParameters:" +
                "\\s+ParamName:\\s+StaticParam\\s+StaticParam" +
                "\\s+Type:\\s+TestType\\s+TestType" +
                "\\s+Nullable:\\s+true\\s+true" +
                "\\s+ParamName:\\s+DeletedParam-+> N/A" +
                "\\s+Type:\\s+TestType\\s+N/A" +
                "\\s+Nullable:\\s+true\\s+N/A" +
                "\\s+ParamName:\\s+ModifiedParam\\s+ModifiedParam" +
                "\\s+Type:\\s+TestType-+> ModifiedTestType" +
                "\\s+Nullable:\\s+true-+> false" +
                "\\s+ParamName:\\s+N/A-+> AddedParam" +
                "\\s+Type:\\s+N/A\\s+TestType" +
                "\\s+Nullable:\\s+N/A\\s+true" +
                "\\s+ResponseCodes:" +
                "\\s+ResponseCode:\\s+200\\s+200" +
                "\\s+Type:\\s+StaticType\\s+StaticType" +
                "\\s+ComponentType:\\s+ComponentType\\s+ComponentType" +
                "\\s+ResponseCode:\\s+202-+> N/A" +
                "\\s+Type:\\s+DeletedType\\s+N/A" +
                "\\s+ComponentType:\\s+ComponentType\\s+N/A" +
                "\\s+ResponseCode:\\s+203\\s+203" +
                "\\s+Type:\\s+ModifiedType\\s+ModifiedType" +
                "\\s+ComponentType:\\s+ComponentType-+> ModifiedComponentType" +
                "\\s+ResponseCode:\\s+N/A-+> 201" +
                "\\s+Type:\\s+N/A\\s+AddedType" +
                "\\s+ComponentType:\\s+N/A\\s+ComponentType",
                Pattern.MULTILINE | Pattern.UNIX_LINES);

        final Ds3RequestDiff diff = getModifiedRequest();

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);
        final Writer writer = new OutputStreamWriter(outputStream);
        final WriterHelper helper = new WriterHelper(writer);

        Ds3RequestDiffSimplePrinter.printRequestDiff(diff, helper);
        helper.close();

        final String result = new String(outputStream.toByteArray());
        assertTrue(expectedPattern.matcher(result).find());
    }
}
