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

package com.spectralogic.ds3contractcomparator;
import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Param;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Request;
import com.spectralogic.ds3autogen.api.models.enums.Classification;
import com.spectralogic.ds3contractcomparator.models.request.*;
import org.junit.Test;

import static com.spectralogic.ds3autogen.testutil.Ds3ModelFixtures.getBucketRequest;
import static com.spectralogic.ds3autogen.testutil.Ds3ModelPartialDataFixture.createDs3RequestTestData;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class Ds3RequestComparatorImpl_Test {

    private final Ds3RequestComparatorImpl comparator = new Ds3RequestComparatorImpl();

    @Test (expected = IllegalArgumentException.class)
    public void compare_BothNull_Test() {
        comparator.compare(null, null);
    }

    @Test
    public void compare_OldNull_Test() {
        final AbstractDs3RequestDiff result = comparator.compare(
                null,
                getBucketRequest());
        assertThat(result, instanceOf(AddedDs3RequestDiff.class));
    }

    @Test
    public void compare_NewNull_Test() {
        final AbstractDs3RequestDiff result = comparator.compare(
                getBucketRequest(),
                null);
        assertThat(result, instanceOf(DeletedDs3RequestDiff.class));
    }

    @Test
    public void compare_SameRequest_Test() {
        final AbstractDs3RequestDiff result = comparator.compare(
                getBucketRequest(),
                getBucketRequest());
        assertThat(result, instanceOf(NoChangeDs3RequestDiff.class));
    }

    @Test
    public void compare_ModifiedRequestHeader_Test() {
        final String requestName = "com.test.TestRequestHandler";
        final Ds3Request request1 = createDs3RequestTestData(requestName, Classification.amazons3);
        final Ds3Request request2 = createDs3RequestTestData(requestName, Classification.spectrads3);
        final AbstractDs3RequestDiff result = comparator.compare(request1, request2);
        assertThat(result, instanceOf(ModifiedDs3RequestDiff.class));
    }

    @Test
    public void compare_ModifiedRequestParams_Test() {
        final Ds3Request request1 = createDs3RequestTestData(
                false,
                ImmutableList.of(new Ds3Param("TypeName", "Boolean", false)),
                ImmutableList.of());

        final Ds3Request request2 = createDs3RequestTestData(
                false,
                ImmutableList.of(new Ds3Param("TypeName", "boolean", false)),
                ImmutableList.of());

        final AbstractDs3RequestDiff result = comparator.compare(request1, request2);
        assertThat(result, instanceOf(ModifiedDs3RequestDiff.class));
    }
}
