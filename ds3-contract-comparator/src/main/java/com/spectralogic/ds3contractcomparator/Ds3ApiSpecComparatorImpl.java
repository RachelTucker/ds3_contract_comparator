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
import com.spectralogic.ds3autogen.utils.ConverterUtil;
import com.spectralogic.ds3autogen.utils.collections.GuavaCollectors;
import com.spectralogic.ds3contractcomparator.models.Ds3ApiSpecDiff;
import com.spectralogic.ds3contractcomparator.models.request.AbstractDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.type.AbstractDs3TypeDiff;

import static com.spectralogic.ds3autogen.utils.ConverterUtil.hasContent;
import static com.spectralogic.ds3autogen.utils.ConverterUtil.isEmpty;

/**
 * Implements {@link Ds3ApiSpecComparator} and performs the {@link Ds3ApiSpec} comparison
 * using {@link Ds3RequestComparatorImpl} and {@link Ds3TypeComparatorImpl}
 */
public class Ds3ApiSpecComparatorImpl implements Ds3ApiSpecComparator {

    @Override
    public Ds3ApiSpecDiff compare(final Ds3ApiSpec oldSpec, final Ds3ApiSpec newSpec) {
        return new Ds3ApiSpecDiff(
                compareDs3Requests(oldSpec.getRequests(), newSpec.getRequests()),
                compareDs3Types(oldSpec.getTypes(), newSpec.getTypes()));
    }

    /**
     * Compares two {@link ImmutableList} of {@link Ds3Request}
     * @param oldRequests List of {@link Ds3Request} from the older API
     * @param newRequests List of {@link Ds3Request} from the newer API
     */
    static ImmutableList<AbstractDs3RequestDiff> compareDs3Requests(
            final ImmutableList<Ds3Request> oldRequests, final ImmutableList<Ds3Request> newRequests) {
        final ImmutableSet<String> requestNames = getRequestNameUnion(oldRequests, newRequests);
        final ImmutableMap<String, Ds3Request> oldMap = toRequestMap(oldRequests);
        final ImmutableMap<String, Ds3Request> newMap = toRequestMap(newRequests);

        final Ds3RequestComparator comparator = new Ds3RequestComparatorImpl();
        return requestNames.stream()
                .map(name -> comparator.compare(oldMap.get(name), newMap.get(name)))
                .collect(GuavaCollectors.immutableList());
    }

    /**
     * Gets the union of all {@link Ds3Request} names in both lists
     */
    static ImmutableSet<String> getRequestNameUnion(
            final ImmutableList<Ds3Request> oldRequests,
            final ImmutableList<Ds3Request> newRequests) {
        final ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        if (ConverterUtil.hasContent(oldRequests)) {
            oldRequests.forEach(request -> builder.add(request.getName()));
        }
        if (ConverterUtil.hasContent(newRequests)) {
            newRequests.forEach(request -> builder.add(request.getName()));
        }
        return builder.build();
    }

    /**
     * Creates an {@link ImmutableMap} of {@link Ds3Request#getName()} to {@link Ds3Request} for easy searching
     */
    static ImmutableMap<String, Ds3Request> toRequestMap(final ImmutableList<Ds3Request> requests) {
        if (isEmpty(requests)) {
            return ImmutableMap.of();
        }
        final ImmutableMap.Builder<String, Ds3Request> builder = ImmutableMap.builder();
        requests.forEach(request -> builder.put(request.getName(), request));
        return builder.build();
    }

    /**
     * Compares two {@link ImmutableMap} of {@link Ds3Type}
     * @param oldTypes Map of {@link Ds3Type} from the older API
     * @param newTypes Map of {@link Ds3Type} from the newer API
     */
    static ImmutableList<AbstractDs3TypeDiff> compareDs3Types(
            final ImmutableMap<String, Ds3Type> oldTypes,
            final ImmutableMap<String, Ds3Type> newTypes) {
        final ImmutableSet<String> typeNames = getTypeNameUnion(oldTypes, newTypes);
        final Ds3TypeComparator comparator = new Ds3TypeComparatorImpl();
        return typeNames.stream()
                .map(name -> comparator.compare(oldTypes.get(name), newTypes.get(name)))
                .collect(GuavaCollectors.immutableList());
    }

    /**
     * Gets the union of all {@link Ds3Type} names in both maps
     */
    static ImmutableSet<String> getTypeNameUnion(
            final ImmutableMap<String, Ds3Type> oldTypes,
            final ImmutableMap<String, Ds3Type> newTypes) {
        final ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        if (hasContent(oldTypes)) {
            oldTypes.keySet().forEach(builder::add);
        }
        if (hasContent(newTypes)) {
            newTypes.keySet().forEach(builder::add);
        }
        return builder.build();
    }
}
