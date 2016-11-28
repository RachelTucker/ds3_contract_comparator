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

package com.spectralogic.ds3contractcomparator;

import com.spectralogic.ds3autogen.api.models.apispec.Ds3Type;
import com.spectralogic.ds3contractcomparator.models.type.AbstractDs3TypeDiff;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for comparing two {@link Ds3Type}
 */
@FunctionalInterface
public interface Ds3TypeComparator {

    /**
     * Compares two {@link Ds3Type} and creates a {@link AbstractDs3TypeDiff} containing their differences
     * @param oldDs3Type the older API version of the {@link Ds3Type} or null if it does not exist within the old version
     * @param newDs3Type the newer API version of the {@link Ds3Type} or null if it does not exist within the new version
     */
    AbstractDs3TypeDiff compare(@Nullable final Ds3Type oldDs3Type, @Nullable final Ds3Type newDs3Type);
}
