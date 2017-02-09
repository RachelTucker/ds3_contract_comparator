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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3ApiSpec;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Request;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Type;
import com.spectralogic.ds3autogen.testutil.Ds3ModelFixtures;
import com.spectralogic.ds3contractcomparator.models.Ds3ApiSpecDiff;
import com.spectralogic.ds3contractcomparator.models.request.AbstractDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.request.AddedDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.request.DeletedDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.request.NoChangeDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.type.AbstractDs3TypeDiff;
import com.spectralogic.ds3contractcomparator.models.type.AddedDs3TypeDiff;
import com.spectralogic.ds3contractcomparator.models.type.DeletedDs3TypeDiff;
import com.spectralogic.ds3contractcomparator.models.type.NoChangeDs3TypeDiff;
import org.junit.Test;

import static com.spectralogic.ds3autogen.testutil.Ds3ModelFixtures.*;
import static com.spectralogic.ds3contractcomparator.Ds3ApiSpecComparatorImpl.compareDs3Requests;
import static com.spectralogic.ds3contractcomparator.Ds3ApiSpecComparatorImpl.compareDs3Types;
import static com.spectralogic.ds3contractcomparator.Ds3ApiSpecComparatorImpl.getTypeNameUnion;
import static com.spectralogic.ds3contractcomparator.utils.TestDataFixture.createSimpleTestType;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class Ds3ApiSpecComparatorImp_Test {

    private static final Ds3ApiSpecComparatorImpl comparator = new Ds3ApiSpecComparatorImpl();

    private static ImmutableList<Ds3Request> getTestRequests() {
        return ImmutableList.of(
                getBucketRequest(),
                deleteBucketRequest());
    }

    private static ImmutableMap<String, Ds3Type> getTestTypes() {
        final Ds3Type type1 = createSimpleTestType("TypeOne", "");
        final Ds3Type type2 = createSimpleTestType("TypeTwo", "");
        return ImmutableMap.of(
                type1.getName(), type1,
                type2.getName(), type2);
    }

    @Test
    public void toRequestMap_NullList_Test() {
        final ImmutableMap<String, Ds3Request> result = Ds3ApiSpecComparatorImpl.toRequestMap(null);
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(0));
    }

    @Test
    public void toRequestMap_EmptyList_Test() {
        final ImmutableMap<String, Ds3Request> result = Ds3ApiSpecComparatorImpl.toRequestMap(ImmutableList.of());
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(0));
    }

    @Test
    public void toRequestMap_FullList_Test() {
        final ImmutableList<Ds3Request> requests = getTestRequests();
        final ImmutableMap<String, Ds3Request> result = Ds3ApiSpecComparatorImpl.toRequestMap(requests);
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(requests.size()));
        requests.forEach(request -> assertTrue(result.containsKey(request.getName())));
    }

    @Test
    public void getRequestNameUnion_NullLists_Test() {
        final ImmutableSet<String> result = Ds3ApiSpecComparatorImpl.getRequestNameUnion(null, null);
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(0));
    }

    @Test
    public void getRequestNameUnion_EmptyLists_Test() {
        final ImmutableSet<String> result = Ds3ApiSpecComparatorImpl.getRequestNameUnion(ImmutableList.of(), ImmutableList.of());
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(0));
    }

    @Test
    public void getRequestNameUnion_FullLists_Test() {
        final ImmutableList<Ds3Request> oldRequests = getTestRequests();
        final ImmutableList<Ds3Request> newRequests = ImmutableList.of(
                getBucketRequest(),
                Ds3ModelFixtures.createBucketRequest());

        final ImmutableSet<String> result = Ds3ApiSpecComparatorImpl.getRequestNameUnion(oldRequests, newRequests);
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(3));
        oldRequests.forEach(request -> assertTrue(result.contains(request.getName())));
        newRequests.forEach(request -> assertTrue(result.contains(request.getName())));
    }

    @Test
    public void compareDs3Requests_NullLists_Test() {
        final ImmutableList<AbstractDs3RequestDiff> result = compareDs3Requests(null, null);
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(0));
    }

    @Test
    public void compareDs3Requests_EmptyLists_Test() {
        final ImmutableList<AbstractDs3RequestDiff> result = compareDs3Requests(
                ImmutableList.of(),
                ImmutableList.of());
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(0));
    }

    @Test
    public void compareDs3Requests_FullListsNoDiffs_Test() {
        final ImmutableList<Ds3Request> requests = getTestRequests();
        final ImmutableList<AbstractDs3RequestDiff> result = compareDs3Requests(
                requests,
                getTestRequests()); //Call the function again so it's comparing by value not reference
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(requests.size()));
        result.forEach(diff -> assertThat(diff, instanceOf(NoChangeDs3RequestDiff.class)));
    }

    @Test
    public void compareDs3Requests_FullListsWithDiffs_Test() {
        final ImmutableList<Ds3Request> requests1 = getTestRequests();
        final ImmutableList<Ds3Request> requests2 = ImmutableList.of(
                getBucketRequest(), //same as in requests1
                getHeadBucketRequest()); //not in requests1
        final ImmutableList<AbstractDs3RequestDiff> result = compareDs3Requests(requests1, requests2);
        assertThat(result.size(), is(3));

        assertThat(result.get(0), instanceOf(NoChangeDs3RequestDiff.class));
        assertThat(result.get(0).getNewDs3Request().getName(), is("com.spectralogic.s3.server.handler.reqhandler.amazons3.bucket.GetBucketRequestHandler"));
        assertThat(result.get(1), instanceOf(DeletedDs3RequestDiff.class));
        assertThat(result.get(1).getOldDs3Request().getName(), is("com.spectralogic.s3.server.handler.reqhandler.spectrads3.bucket.DeleteBucketRequestHandler"));
        assertThat(result.get(2), instanceOf(AddedDs3RequestDiff.class));
        assertThat(result.get(2).getNewDs3Request().getName(), is("com.spectralogic.s3.server.handler.reqhandler.amazons3.HeadBucketRequestHandler"));
    }

    @Test
    public void getTypeNameUnion_NullMaps_Test() {
        final ImmutableSet<String> result = getTypeNameUnion(null, null);
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(0));
    }

    @Test
    public void getTypeNameUnion_EmptyMaps_Test() {
        final ImmutableSet<String> result = getTypeNameUnion(ImmutableMap.of(), ImmutableMap.of());
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(0));
    }

    @Test
    public void getTypeNameUnion_FullMapsNoDiffs_Test() {
        final ImmutableMap<String, Ds3Type> types = getTestTypes();

        final ImmutableSet<String> result = getTypeNameUnion(types, types);
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(types.size()));

        types.keySet().forEach(name -> assertTrue(result.contains(name)));
    }

    @Test
    public void getTypeNameUnion_FullMapsWithDiffs_Test() {
        final ImmutableMap<String, Ds3Type> oldTypes = getTestTypes();

        final Ds3Type type = createSimpleTestType("com.test.NameUnionTestType", "");
        final ImmutableMap<String, Ds3Type> newTypes = ImmutableMap.of(type.getName(), type);

        final ImmutableSet<String> result = getTypeNameUnion(oldTypes, newTypes);
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(3));

        oldTypes.keySet().forEach(name -> assertTrue(result.contains(name)));
        newTypes.keySet().forEach(name -> assertTrue(result.contains(name)));
    }

    @Test
    public void compareDs3Types_NullMaps_Test() {
        final ImmutableList<AbstractDs3TypeDiff> result = compareDs3Types(null, null);
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(0));
    }

    @Test
    public void compareDs3Types_EmptyMaps_Test() {
        final ImmutableList<AbstractDs3TypeDiff> result = compareDs3Types(ImmutableMap.of(), ImmutableMap.of());
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(0));
    }

    @Test
    public void compareDs3Types_FullMapsNoDiffs_Test() {
        final ImmutableMap<String, Ds3Type> types = getTestTypes();
        final ImmutableList<AbstractDs3TypeDiff> result = compareDs3Types(
                types,
                getTestTypes()); //Call the function again so it's comparing by value not reference
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(types.size()));
        result.forEach(diff -> assertThat(diff, instanceOf(NoChangeDs3TypeDiff.class)));
    }

    @Test
    public void compareDs3Types_FullMapsWithDiffs_Test() {
        final ImmutableMap<String, Ds3Type> oldTypes = getTestTypes();

        final Ds3Type typeOne = createSimpleTestType("TypeOne", "");
        final Ds3Type typeThree = createSimpleTestType("TypeThree", "");
        final ImmutableMap<String, Ds3Type> newTypes = ImmutableMap.of(
                typeOne.getName(), typeOne, //Also in OldTypes
                typeThree.getName(), typeThree); //Not in OldTypes

        final ImmutableList<AbstractDs3TypeDiff> result = compareDs3Types(oldTypes, newTypes);

        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(3));
        assertThat(result.get(0), instanceOf(NoChangeDs3TypeDiff.class));
        assertThat(result.get(0).getNewDs3Type().getName(), is("TypeOne"));
        assertThat(result.get(1), instanceOf(DeletedDs3TypeDiff.class));
        assertThat(result.get(1).getOldDs3Type().getName(), is("TypeTwo"));
        assertThat(result.get(2), instanceOf(AddedDs3TypeDiff.class));
        assertThat(result.get(2).getNewDs3Type().getName(), is("TypeThree"));
    }

    @Test
    public void compare_NullSpec_Test() {
        final Ds3ApiSpecDiff result = comparator.compare(
                new Ds3ApiSpec(null, null),
                new Ds3ApiSpec(null, null));

        assertThat(result, is(notNullValue()));
        assertThat(result.getRequests().size(), is(0));
        assertThat(result.getTypes().size(), is(0));
    }

    @Test
    public void compare_EmptySpec_Test() {
        final Ds3ApiSpecDiff result = comparator.compare(
                new Ds3ApiSpec(ImmutableList.of(), ImmutableMap.of()),
                new Ds3ApiSpec(ImmutableList.of(), ImmutableMap.of()));

        assertThat(result, is(notNullValue()));
        assertThat(result.getRequests().size(), is(0));
        assertThat(result.getTypes().size(), is(0));
    }

    @Test
    public void compare_FullSpec_Test() {
        final Ds3ApiSpecDiff result = comparator.compare(
                new Ds3ApiSpec(getTestRequests(), getTestTypes()),
                new Ds3ApiSpec(getTestRequests(), getTestTypes()));

        assertThat(result, is(notNullValue()));
        result.getRequests().forEach(request -> assertThat(request, instanceOf(NoChangeDs3RequestDiff.class)));
        result.getTypes().forEach(type -> assertThat(type, instanceOf(NoChangeDs3TypeDiff.class)));
    }
}
