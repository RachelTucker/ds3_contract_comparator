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

import com.spectralogic.ds3autogen.api.models.apispec.Ds3ApiSpec;
import com.spectralogic.ds3contractcomparator.models.Ds3ApiSpecDiff;

/**
 * Interface for comparing two {@link Ds3ApiSpec}
 */
@FunctionalInterface
public interface Ds3ApiSpecComparator {

    /**
     * Takes in two {@link Ds3ApiSpec} and compares them for differences.
     * @param oldSpec the older API version of the {@link Ds3ApiSpec}
     * @param newSpec the newer API version of the {@link Ds3ApiSpec}
     */
    Ds3ApiSpecDiff compare(final Ds3ApiSpec oldSpec, final Ds3ApiSpec newSpec);
}
