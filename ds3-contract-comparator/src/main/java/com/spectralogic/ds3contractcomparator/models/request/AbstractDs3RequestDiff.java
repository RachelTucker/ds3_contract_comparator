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

package com.spectralogic.ds3contractcomparator.models.request;

import com.spectralogic.ds3autogen.api.models.apispec.Ds3ApiSpec;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Request;

/**
 * Represents a {@link Ds3Request} that changed between {@link Ds3ApiSpec} versions
 */
public abstract class AbstractDs3RequestDiff implements Ds3RequestDiff {

    private final Ds3Request oldDs3Request;
    private final Ds3Request newDs3Request;

    public AbstractDs3RequestDiff(
            final Ds3Request oldDs3Request,
            final Ds3Request newDs3Request) {
        this.oldDs3Request = oldDs3Request;
        this.newDs3Request = newDs3Request;
    }

    @Override
    public Ds3Request getOldDs3Request() {
        return oldDs3Request;
    }

    @Override
    public Ds3Request getNewDs3Request() {
        return newDs3Request;
    }
}
