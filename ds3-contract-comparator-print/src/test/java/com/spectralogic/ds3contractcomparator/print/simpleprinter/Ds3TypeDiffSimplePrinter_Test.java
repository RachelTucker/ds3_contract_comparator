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

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.apispec.*;
import com.spectralogic.ds3contractcomparator.models.type.AddedDs3TypeDiff;
import com.spectralogic.ds3contractcomparator.models.type.DeletedDs3TypeDiff;
import com.spectralogic.ds3contractcomparator.models.type.Ds3TypeDiff;
import com.spectralogic.ds3contractcomparator.models.type.ModifiedDs3TypeDiff;
import com.spectralogic.ds3contractcomparator.print.utils.WriterHelper;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class Ds3TypeDiffSimplePrinter_Test {

    private static Ds3Type getTestType() {
        return new Ds3Type(
                "com.test.TestType",
                "TestTypeNameToMarshal",
                ImmutableList.of(
                        new Ds3Element(
                                "ElementName",
                                "ElementType",
                                "ElementComponentType",
                                ImmutableList.of(
                                        new Ds3Annotation(
                                                "com.spectralogic.util.bean.lang.SortBy",
                                                ImmutableList.of(new Ds3AnnotationElement(
                                                        "com.test.AnnotationElementName",
                                                        "com.test.AnnotationElementValue",
                                                        "com.test.AnnotationElementValueType")))),
                                true)),
                ImmutableList.of(
                        new Ds3EnumConstant(
                                "EnumConstantName",
                                ImmutableList.of(new Ds3Property(
                                        "PropertyName",
                                        "PropertyValue",
                                        "PropertyValueType")))));
    }

    @Test
    public void printTypeDiff_AddedNoFiltering_Test() throws IOException {
        final Pattern expectedPattern = Pattern.compile("\\*{20} ADDED TYPE TestType \\*{20}" +
                        "\\s+TypeName:\\s+N/A-+> TestType" +
                        "\\s+NameToMarshal:\\s+N/A\\s+TestTypeNameToMarshal" +
                        "\\s+Elements:" +
                        "\\s+ElementName:\\s+N/A-+> ElementName" +
                        "\\s+Type:\\s+N/A\\s+ElementType" +
                        "\\s+ComponentType:\\s+N/A\\s+ElementComponentType" +
                        "\\s+Nullable:\\s+N/A\\s+true" +
                        "\\s+Annotations:" +
                        "\\s+AnnotationName:\\s+N/A-+> SortBy" +
                        "\\s+AnnotationElements:" +
                        "\\s+AnnotationElementName:\\s+N/A-+> AnnotationElementName" +
                        "\\s+Value:\\s+N/A\\s+AnnotationElementValue" +
                        "\\s+ValueType:\\s+N/A\\s+AnnotationElementValueType" +
                        "\\s+EnumConstants:" +
                        "\\s+EnumConstantName:\\s+N/A-+> EnumConstantName" +
                        "\\s+Properties:" +
                        "\\s+PropertyName:\\s+N/A-+> PropertyName" +
                        "\\s+Value:\\s+N/A\\s+PropertyValue" +
                        "\\s+ValueType:\\s+N/A\\s+PropertyValueType",
                Pattern.MULTILINE | Pattern.UNIX_LINES);

        final Ds3TypeDiff diff = new AddedDs3TypeDiff(getTestType());

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);
        final Writer writer = new OutputStreamWriter(outputStream);
        final WriterHelper helper = new WriterHelper(writer);

        Ds3TypeDiffSimplePrinter.printTypeDiff(diff, helper, true, true);
        helper.close();

        final String result = new String(outputStream.toByteArray());
        assertTrue(expectedPattern.matcher(result).find());
    }

    @Test
    public void printTypeDiff_AddedWithFiltering_Test() throws IOException {
        final Pattern expectedPattern = Pattern.compile("\\*{20} ADDED TYPE TestType \\*{20}" +
                        "\\s+TypeName:\\s+N/A-+> TestType" +
                        "\\s+NameToMarshal:\\s+N/A\\s+TestTypeNameToMarshal" +
                        "\\s+Elements:" +
                        "\\s+ElementName:\\s+N/A-+> ElementName" +
                        "\\s+Type:\\s+N/A\\s+ElementType" +
                        "\\s+ComponentType:\\s+N/A\\s+ElementComponentType" +
                        "\\s+Nullable:\\s+N/A\\s+true" +
                        "\\s+Annotations:" +
                        "\\s+EnumConstants:" +
                        "\\s+EnumConstantName:\\s+N/A-+> EnumConstantName",
                Pattern.MULTILINE | Pattern.UNIX_LINES);

        final Ds3TypeDiff diff = new AddedDs3TypeDiff(getTestType());

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);
        final Writer writer = new OutputStreamWriter(outputStream);
        final WriterHelper helper = new WriterHelper(writer);

        Ds3TypeDiffSimplePrinter.printTypeDiff(diff, helper, false, false);
        helper.close();

        final String result = new String(outputStream.toByteArray());
        assertTrue(expectedPattern.matcher(result).find());
    }

    @Test
    public void printTypeDiff_DeletedNoFiltering_Test() throws IOException {
        final Pattern expectedPattern = Pattern.compile("\\*{20} DELETED TYPE TestType \\*{20}" +
                        "\\s+TypeName:\\s+TestType-+> N/A" +
                        "\\s+NameToMarshal:\\s+TestTypeNameToMarshal\\s+N/A" +
                        "\\s+Elements:" +
                        "\\s+ElementName:\\s+ElementName-+> N/A" +
                        "\\s+Type:\\s+ElementType\\s+N/A" +
                        "\\s+ComponentType:\\s+ElementComponentType\\s+N/A" +
                        "\\s+Nullable:\\s+true\\s+N/A" +
                        "\\s+Annotations:" +
                        "\\s+AnnotationName:\\s+SortBy-+> N/A" +
                        "\\s+AnnotationElements:" +
                        "\\s+AnnotationElementName:\\s+AnnotationElementName-+> N/A" +
                        "\\s+Value:\\s+AnnotationElementValue\\s+N/A" +
                        "\\s+ValueType:\\s+AnnotationElementValueType\\s+N/A" +
                        "\\s+EnumConstants:" +
                        "\\s+EnumConstantName:\\s+EnumConstantName-+> N/A" +
                        "\\s+Properties:" +
                        "\\s+PropertyName:\\s+PropertyName-+> N/A" +
                        "\\s+Value:\\s+PropertyValue\\s+N/A" +
                        "\\s+ValueType:\\s+PropertyValueType\\s+N/A",
                Pattern.MULTILINE | Pattern.UNIX_LINES);

        final Ds3TypeDiff diff = new DeletedDs3TypeDiff(getTestType());

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);
        final Writer writer = new OutputStreamWriter(outputStream);
        final WriterHelper helper = new WriterHelper(writer);

        Ds3TypeDiffSimplePrinter.printTypeDiff(diff, helper, true, true);
        helper.close();

        final String result = new String(outputStream.toByteArray());
        assertTrue(expectedPattern.matcher(result).find());
    }

    @Test
    public void printTypeDiff_DeletedWithFiltering_Test() throws IOException {
        final Pattern expectedPattern = Pattern.compile("\\*{20} DELETED TYPE TestType \\*{20}" +
                        "\\s+TypeName:\\s+TestType-+> N/A" +
                        "\\s+NameToMarshal:\\s+TestTypeNameToMarshal\\s+N/A" +
                        "\\s+Elements:" +
                        "\\s+ElementName:\\s+ElementName-+> N/A" +
                        "\\s+Type:\\s+ElementType\\s+N/A" +
                        "\\s+ComponentType:\\s+ElementComponentType\\s+N/A" +
                        "\\s+Nullable:\\s+true\\s+N/A" +
                        "\\s+Annotations:" +
                        "\\s+EnumConstants:" +
                        "\\s+EnumConstantName:\\s+EnumConstantName-+> N/A",
                Pattern.MULTILINE | Pattern.UNIX_LINES);

        final Ds3TypeDiff diff = new DeletedDs3TypeDiff(getTestType());

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);
        final Writer writer = new OutputStreamWriter(outputStream);
        final WriterHelper helper = new WriterHelper(writer);

        Ds3TypeDiffSimplePrinter.printTypeDiff(diff, helper, false, false);
        helper.close();

        final String result = new String(outputStream.toByteArray());
        assertTrue(expectedPattern.matcher(result).find());
    }

    @Test
    public void printTypeDiff_Modified_Test() throws IOException {
        final Pattern expectedPattern = Pattern.compile("\\*{20} MODIFIED TYPE TestType \\*{20}" +
                        "\\s+TypeName:\\s+TestType\\s+TestType" +
                        "\\s+NameToMarshal:\\s+OldNameToMarshal-+> NewNameToMarshal",
                Pattern.MULTILINE | Pattern.UNIX_LINES);

        final Ds3Type oldType = new Ds3Type(
                "com.test.TestType",
                "OldNameToMarshal",
                ImmutableList.of(),
                ImmutableList.of());

        final Ds3Type newType = new Ds3Type(
                "com.test.TestType",
                "NewNameToMarshal",
                ImmutableList.of(),
                ImmutableList.of());

        final Ds3TypeDiff diff = new ModifiedDs3TypeDiff(oldType, newType);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);
        final Writer writer = new OutputStreamWriter(outputStream);
        final WriterHelper helper = new WriterHelper(writer);

        Ds3TypeDiffSimplePrinter.printTypeDiff(diff, helper, false, false);
        helper.close();

        final String result = new String(outputStream.toByteArray());
        assertTrue(expectedPattern.matcher(result).find());
    }
}
