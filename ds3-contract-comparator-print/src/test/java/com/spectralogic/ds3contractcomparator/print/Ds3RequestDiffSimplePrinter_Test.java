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
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Param;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Request;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3ResponseCode;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3ResponseType;
import com.spectralogic.ds3autogen.api.models.enums.*;
import com.spectralogic.ds3contractcomparator.models.request.AddedDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.request.DeletedDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.request.Ds3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.request.ModifiedDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.print.simpleprinter.Ds3RequestDiffSimplePrinter;
import com.spectralogic.ds3contractcomparator.print.utils.WriterHelper;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import static com.spectralogic.ds3autogen.testutil.Ds3ModelFixtures.getBucketRequest;

public class Ds3RequestDiffSimplePrinter_Test {

    @Test
    public void printRequestDiff_Added_Test() throws IOException {
        final String expected = "******************** ADDED REQUEST GetBucketRequestHandler (amazons3) ********************\n\n" +
                "  RequestName:                   N/A--------------------------------------------------------> GetBucketRequestHandler                                     \n" +
                "  Classification:                N/A                                                          amazons3                                                    \n" +
                "  HttpVerb:                      N/A                                                          GET                                                         \n" +
                "  BucketRequirement:             N/A                                                          REQUIRED                                                    \n" +
                "  ObjectRequirement:             N/A                                                          NOT_ALLOWED                                                 \n" +
                "  IncludeInPath:                 N/A                                                          false                                                       \n" +
                "  OptionalParameters:\n" +
                "    ParamName:                     N/A--------------------------------------------------------> Delimiter                                                   \n" +
                "      Type:                          N/A                                                          String                                                      \n" +
                "      Nullable:                      N/A                                                          true                                                        \n" +
                "    ParamName:                     N/A--------------------------------------------------------> Marker                                                      \n" +
                "      Type:                          N/A                                                          String                                                      \n" +
                "      Nullable:                      N/A                                                          true                                                        \n" +
                "    ParamName:                     N/A--------------------------------------------------------> MaxKeys                                                     \n" +
                "      Type:                          N/A                                                          int                                                         \n" +
                "      Nullable:                      N/A                                                          false                                                       \n" +
                "    ParamName:                     N/A--------------------------------------------------------> Prefix                                                      \n" +
                "      Type:                          N/A                                                          String                                                      \n" +
                "      Nullable:                      N/A                                                          true                                                        \n" +
                "  ResponseCodes:\n" +
                "    ResponseCode:                  N/A--------------------------------------------------------> 200                                                         \n" +
                "      Type:                          N/A                                                          ListBucketResult                                            \n" +
                "\n\n";

        final Ds3RequestDiff diff = new AddedDs3RequestDiff(getBucketRequest());

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);
        final Writer writer = new OutputStreamWriter(outputStream);
        final WriterHelper helper = new WriterHelper(writer);

        Ds3RequestDiffSimplePrinter.printRequestDiff(diff, helper);
        helper.close();

        final String result = new String(outputStream.toByteArray());
        assertThat(result, is(expected));
    }

    @Test
    public void printRequestDiff_Deleted_Test() throws IOException {
        final String expected = "******************** DELETED REQUEST GetBucketRequestHandler (amazons3) ********************\n\n" +
                "  RequestName:                   GetBucketRequestHandler------------------------------------> N/A                                                         \n" +
                "  Classification:                amazons3                                                     N/A                                                         \n" +
                "  HttpVerb:                      GET                                                          N/A                                                         \n" +
                "  BucketRequirement:             REQUIRED                                                     N/A                                                         \n" +
                "  ObjectRequirement:             NOT_ALLOWED                                                  N/A                                                         \n" +
                "  IncludeInPath:                 false                                                        N/A                                                         \n" +
                "  OptionalParameters:\n" +
                "    ParamName:                     Delimiter--------------------------------------------------> N/A                                                         \n" +
                "      Type:                          String                                                       N/A                                                         \n" +
                "      Nullable:                      true                                                         N/A                                                         \n" +
                "    ParamName:                     Marker-----------------------------------------------------> N/A                                                         \n" +
                "      Type:                          String                                                       N/A                                                         \n" +
                "      Nullable:                      true                                                         N/A                                                         \n" +
                "    ParamName:                     MaxKeys----------------------------------------------------> N/A                                                         \n" +
                "      Type:                          int                                                          N/A                                                         \n" +
                "      Nullable:                      false                                                        N/A                                                         \n" +
                "    ParamName:                     Prefix-----------------------------------------------------> N/A                                                         \n" +
                "      Type:                          String                                                       N/A                                                         \n" +
                "      Nullable:                      true                                                         N/A                                                         \n" +
                "  ResponseCodes:\n" +
                "    ResponseCode:                  200--------------------------------------------------------> N/A                                                         \n" +
                "      Type:                          ListBucketResult                                             N/A                                                         \n" +
                "\n\n";

        final Ds3RequestDiff diff = new DeletedDs3RequestDiff(getBucketRequest());

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);
        final Writer writer = new OutputStreamWriter(outputStream);
        final WriterHelper helper = new WriterHelper(writer);

        Ds3RequestDiffSimplePrinter.printRequestDiff(diff, helper);
        helper.close();

        final String result = new String(outputStream.toByteArray());
        assertThat(result, is(expected));
    }

    @Test
    public void printRequestDiff_Modified_Test() throws IOException {
        final String expected = "******************** MODIFIED REQUEST TestRequest (amazons3) ********************\n\n" +
                "  RequestName:                   TestRequest                                                  TestRequest                                                 \n" +
                "  Classification:                amazons3                                                     amazons3                                                    \n" +
                "  HttpVerb:                      DELETE-----------------------------------------------------> HEAD                                                        \n" +
                "  ObjectRequirement:             -----------------------------------------------------------> NOT_ALLOWED                                                 \n" +
                "  Action:                        BULK_DELETE------------------------------------------------>                                                             \n" +
                "  Resource:                      ACTIVE_JOB                                                   ACTIVE_JOB                                                  \n" +
                "  ResourceType:                  NON_SINGLETON                                                NON_SINGLETON                                               \n" +
                "  Operation:                     ALLOCATE                                                     ALLOCATE                                                    \n" +
                "  IncludeInPath:                 false                                                        false                                                       \n" +
                "  OptionalParameters:\n" +
                "    ParamName:                     StaticParam                                                  StaticParam                                                 \n" +
                "      Type:                          TestType                                                     TestType                                                    \n" +
                "      Nullable:                      true                                                         true                                                        \n" +
                "    ParamName:                     DeletedParam-----------------------------------------------> N/A                                                         \n" +
                "      Type:                          TestType                                                     N/A                                                         \n" +
                "      Nullable:                      true                                                         N/A                                                         \n" +
                "    ParamName:                     ModifiedParam                                                ModifiedParam                                               \n" +
                "      Type:                          TestType---------------------------------------------------> ModifiedTestType                                            \n" +
                "      Nullable:                      true-------------------------------------------------------> false                                                       \n" +
                "    ParamName:                     N/A--------------------------------------------------------> AddedParam                                                  \n" +
                "      Type:                          N/A                                                          TestType                                                    \n" +
                "      Nullable:                      N/A                                                          true                                                        \n" +
                "  ResponseCodes:\n" +
                "    ResponseCode:                  200                                                          200                                                         \n" +
                "      Type:                          StaticType                                                   StaticType                                                  \n" +
                "      ComponentType:                 ComponentType                                                ComponentType                                               \n" +
                "    ResponseCode:                  202--------------------------------------------------------> N/A                                                         \n" +
                "      Type:                          DeletedType                                                  N/A                                                         \n" +
                "      ComponentType:                 ComponentType                                                N/A                                                         \n" +
                "    ResponseCode:                  203                                                          203                                                         \n" +
                "      Type:                          ModifiedType                                                 ModifiedType                                                \n" +
                "      ComponentType:                 ComponentType----------------------------------------------> ModifiedComponentType                                       \n" +
                "    ResponseCode:                  N/A--------------------------------------------------------> 201                                                         \n" +
                "      Type:                          N/A                                                          AddedType                                                   \n" +
                "      ComponentType:                 N/A                                                          ComponentType                                               \n" +
                "\n\n";

        final Ds3Request oldRequest = new Ds3Request(
                "TestRequest",
                HttpVerb.DELETE,
                Classification.amazons3,
                null,
                null,
                Action.BULK_DELETE,
                Resource.ACTIVE_JOB,
                ResourceType.NON_SINGLETON,
                Operation.ALLOCATE,
                false,
                ImmutableList.of(
                        new Ds3ResponseCode(200, ImmutableList.of(new Ds3ResponseType("StaticType", "ComponentType"))), //static
                        new Ds3ResponseCode(202, ImmutableList.of(new Ds3ResponseType("DeletedType", "ComponentType"))), //deleted
                        new Ds3ResponseCode(203, ImmutableList.of(new Ds3ResponseType("ModifiedType", "ComponentType")))  //modified
                ),
                ImmutableList.of(
                        new Ds3Param("StaticParam", "TestType", true),
                        new Ds3Param("DeletedParam", "TestType", true),
                        new Ds3Param("ModifiedParam", "TestType", true)),
                null);

        final Ds3Request newRequest = new Ds3Request(
                "TestRequest",
                HttpVerb.HEAD,           //changed
                Classification.amazons3,
                null,
                Requirement.NOT_ALLOWED, //added
                null,              //deleted
                Resource.ACTIVE_JOB,
                ResourceType.NON_SINGLETON,
                Operation.ALLOCATE,
                false,
                ImmutableList.of(
                        new Ds3ResponseCode(200, ImmutableList.of(new Ds3ResponseType("StaticType", "ComponentType"))), //static
                        new Ds3ResponseCode(201, ImmutableList.of(new Ds3ResponseType("AddedType", "ComponentType"))), //added
                        new Ds3ResponseCode(203, ImmutableList.of(new Ds3ResponseType("ModifiedType", "ModifiedComponentType")))  //modified
                ),
                ImmutableList.of(
                        new Ds3Param("StaticParam", "TestType", true),
                        new Ds3Param("AddedParam", "TestType", true),
                        new Ds3Param("ModifiedParam", "ModifiedTestType", false)),
                null);

        final Ds3RequestDiff diff = new ModifiedDs3RequestDiff(oldRequest, newRequest);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024 * 8);
        final Writer writer = new OutputStreamWriter(outputStream);
        final WriterHelper helper = new WriterHelper(writer);

        Ds3RequestDiffSimplePrinter.printRequestDiff(diff, helper);
        helper.close();

        final String result = new String(outputStream.toByteArray());
        assertThat(result, is(expected));
    }
}
