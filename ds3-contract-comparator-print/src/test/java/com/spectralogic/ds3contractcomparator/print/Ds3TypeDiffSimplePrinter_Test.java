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

package com.spectralogic.ds3contractcomparator.print;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.apispec.*;
import com.spectralogic.ds3contractcomparator.models.type.AddedDs3TypeDiff;
import com.spectralogic.ds3contractcomparator.models.type.DeletedDs3TypeDiff;
import com.spectralogic.ds3contractcomparator.models.type.Ds3TypeDiff;
import com.spectralogic.ds3contractcomparator.models.type.ModifiedDs3TypeDiff;
import com.spectralogic.ds3contractcomparator.print.simpleprinter.Ds3TypeDiffSimplePrinter;
import com.spectralogic.ds3contractcomparator.print.utils.WriterHelper;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
                                                "com.test.AnnotationName",
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
    public void printTypeDiff_Added_Test() throws IOException {
        final String expected = "ADDED TYPE TestType\n" +
                "  Name:                N/A----------------------------------------------> TestType                                          \n" +
                "  NameToMarshal:       N/A                                                TestTypeNameToMarshal                             \n" +
                "  Elements:\n" +
                "    Name:                N/A----------------------------------------------> ElementName                                       \n" +
                "      Type:                N/A                                                ElementType                                       \n" +
                "      ComponentType:       N/A                                                ElementComponentType                              \n" +
                "      Nullable:            N/A                                                true                                              \n" +
                "      Annotations:\n" +
                "        Name:                N/A----------------------------------------------> AnnotationName                                    \n" +
                "          AnnotationElements:\n" +
                "            Name:                N/A----------------------------------------------> AnnotationElementName                             \n" +
                "              Value:               N/A                                                AnnotationElementValue                            \n" +
                "              ValueType:           N/A                                                AnnotationElementValueType                        \n" +
                "  EnumConstants:\n" +
                "    Name:                N/A----------------------------------------------> EnumConstantName                                  \n" +
                "      Properties:\n" +
                "        Name:                N/A----------------------------------------------> PropertyName                                      \n" +
                "          Value:               N/A                                                PropertyValue                                     \n" +
                "          ValueType:           N/A                                                PropertyValueType                                 \n\n";

        final Ds3TypeDiff diff = new AddedDs3TypeDiff(getTestType());

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);
        final Writer writer = new OutputStreamWriter(outputStream);
        final WriterHelper helper = new WriterHelper(writer);

        Ds3TypeDiffSimplePrinter.printTypeDiff(diff, helper);
        helper.close();

        final String result = new String(outputStream.toByteArray());
        assertThat(result, is(expected));
    }

    @Test
    public void printTypeDiff_Deleted_Test() throws IOException {
        final String expected = "DELETED TYPE TestType\n" +
                "  Name:                TestType-----------------------------------------> N/A                                               \n" +
                "  NameToMarshal:       TestTypeNameToMarshal                              N/A                                               \n" +
                "  Elements:\n" +
                "    Name:                ElementName--------------------------------------> N/A                                               \n" +
                "      Type:                ElementType                                        N/A                                               \n" +
                "      ComponentType:       ElementComponentType                               N/A                                               \n" +
                "      Nullable:            true                                               N/A                                               \n" +
                "      Annotations:\n" +
                "        Name:                AnnotationName-----------------------------------> N/A                                               \n" +
                "          AnnotationElements:\n" +
                "            Name:                AnnotationElementName----------------------------> N/A                                               \n" +
                "              Value:               AnnotationElementValue                             N/A                                               \n" +
                "              ValueType:           AnnotationElementValueType                         N/A                                               \n" +
                "  EnumConstants:\n" +
                "    Name:                EnumConstantName---------------------------------> N/A                                               \n" +
                "      Properties:\n" +
                "        Name:                PropertyName-------------------------------------> N/A                                               \n" +
                "          Value:               PropertyValue                                      N/A                                               \n" +
                "          ValueType:           PropertyValueType                                  N/A                                               \n\n";

        final Ds3TypeDiff diff = new DeletedDs3TypeDiff(getTestType());

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);
        final Writer writer = new OutputStreamWriter(outputStream);
        final WriterHelper helper = new WriterHelper(writer);

        Ds3TypeDiffSimplePrinter.printTypeDiff(diff, helper);
        helper.close();

        final String result = new String(outputStream.toByteArray());
        assertThat(result, is(expected));
    }

    @Test
    public void printTypeDiff_Modified_Test() throws IOException {
        final String expected = "MODIFIED TYPE TestType\n" +
                "  Name:                TestType                                           TestType                                          \n" +
                "  NameToMarshal:       OldNameToMarshal---------------------------------> NewNameToMarshal                                  \n\n";

        final Ds3Type oldType = new Ds3Type(
                "com.test.TestType",
                "OldNameToMarshal",
                null, //todo
                null); //todo

        final Ds3Type newType = new Ds3Type(
                "com.test.TestType",
                "NewNameToMarshal",
                null,
                null);

        final Ds3TypeDiff diff = new ModifiedDs3TypeDiff(oldType, newType);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);
        final Writer writer = new OutputStreamWriter(outputStream);
        final WriterHelper helper = new WriterHelper(writer);

        Ds3TypeDiffSimplePrinter.printTypeDiff(diff, helper);
        helper.close();

        final String result = new String(outputStream.toByteArray());
        assertThat(result, is(expected));
    }
}
