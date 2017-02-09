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

package com.spectralogic.ds3contractcomparator.models;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3ApiSpec;
import com.spectralogic.ds3contractcomparator.models.request.AbstractDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.type.AbstractDs3TypeDiff;

/**
 * Contains the differences between two {@link Ds3ApiSpec}
 */
public class Ds3ApiSpecDiff {

    final ImmutableList<AbstractDs3RequestDiff> requests;
    final ImmutableList<AbstractDs3TypeDiff> types;

    public Ds3ApiSpecDiff(
            final ImmutableList<AbstractDs3RequestDiff> requests,
            final ImmutableList<AbstractDs3TypeDiff> types) {
        this.requests = requests;
        this.types = types;
    }

    public ImmutableList<AbstractDs3RequestDiff> getRequests() {
        return requests;
    }

    public ImmutableList<AbstractDs3TypeDiff> getTypes() {
        return types;
    }
}
