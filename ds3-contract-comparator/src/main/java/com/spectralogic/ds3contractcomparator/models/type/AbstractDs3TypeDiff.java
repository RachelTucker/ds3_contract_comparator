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

package com.spectralogic.ds3contractcomparator.models.type;

import com.spectralogic.ds3autogen.api.models.apispec.Ds3ApiSpec;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Type;

/**
 * Represents a {@link Ds3Type} that changed between {@link Ds3ApiSpec} versions
 */
public class AbstractDs3TypeDiff implements Ds3TypeDiff {

    private final Ds3Type oldDs3Type;
    private final Ds3Type newDs3Type;

    public AbstractDs3TypeDiff(final Ds3Type oldDs3Type, final Ds3Type newDs3Type) {
        this.oldDs3Type = oldDs3Type;
        this.newDs3Type = newDs3Type;
    }

    @Override
    public Ds3Type getOldDs3Type() {
        return oldDs3Type;
    }

    @Override
    public Ds3Type getNewDs3Type() {
        return newDs3Type;
    }
}
