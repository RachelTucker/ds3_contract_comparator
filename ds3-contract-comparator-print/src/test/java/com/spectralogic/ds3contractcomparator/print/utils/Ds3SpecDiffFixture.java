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

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Param;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Request;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3ResponseCode;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3ResponseType;
import com.spectralogic.ds3autogen.api.models.enums.*;
import com.spectralogic.ds3contractcomparator.models.Ds3ApiSpecDiff;
import com.spectralogic.ds3contractcomparator.models.request.AbstractDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.request.AddedDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.request.DeletedDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.request.ModifiedDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.type.AbstractDs3TypeDiff;

import static com.spectralogic.ds3autogen.testutil.Ds3ModelFixtures.getBucketRequest;

/**
 * Contains fixtures for testing.
 */
public final class Ds3SpecDiffFixture {

    public static Ds3ApiSpecDiff getSpecDiff() {
        final ImmutableList<AbstractDs3RequestDiff> requests = ImmutableList.of(
                getAddedRequest(),
                getDeletedRequest(),
                getModifiedRequest()
        );

        final ImmutableList<AbstractDs3TypeDiff> types = ImmutableList.of(); //TODO

        return new Ds3ApiSpecDiff(requests, types);
    }

    public static AddedDs3RequestDiff getAddedRequest() {
        return new AddedDs3RequestDiff(getBucketRequest());
    }

    public static DeletedDs3RequestDiff getDeletedRequest() {
        return new DeletedDs3RequestDiff(getBucketRequest());
    }

    public static ModifiedDs3RequestDiff getModifiedRequest() {
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

        return new ModifiedDs3RequestDiff(oldRequest, newRequest);
    }
}
