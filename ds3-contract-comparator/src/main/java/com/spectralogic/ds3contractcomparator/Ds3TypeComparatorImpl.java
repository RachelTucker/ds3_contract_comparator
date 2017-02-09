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

import com.spectralogic.ds3autogen.api.models.apispec.Ds3Type;
import com.spectralogic.ds3contractcomparator.models.type.*;
import org.jetbrains.annotations.Nullable;

public class Ds3TypeComparatorImpl implements Ds3TypeComparator {

    /**
     * Compares two {@link Ds3Type} and creates a {@link AbstractDs3TypeDiff} representing the
     * change(s) in status, if change(s) exist between the {@link Ds3Type}.
     * Status change(s) consist of: No-Change, Added, Deleted, and Modified
     * @param oldDs3Type the older API version of the {@link Ds3Type} or null if it does not exist within the old version
     * @param newDs3Type the newer API version of the {@link Ds3Type} or null if it does not exist within the new version
     * @return
     *   {@link NoChangeDs3TypeDiff} if {@code oldDs3Type} and {@code newDs3Type} are non-null and value equals
     *   {@link AddedDs3TypeDiff} if {@code oldDs3Type} is null and {@code newDs3Type} is non-null
     *   {@link DeletedDs3TypeDiff} if {@code oldDs3Type} is non-null and {@code newDs3Type} is null
     *   {@link ModifiedDs3TypeDiff} if {@code oldDs3Type} and {@code newDs3Type} are not the same
     * @throws IllegalArgumentException if {@code oldDs3Type} and {@code newDs3Type} are both null
     */
    @Override
    public AbstractDs3TypeDiff compare(@Nullable final Ds3Type oldDs3Type, @Nullable final Ds3Type newDs3Type) {
        if (oldDs3Type ==  null && newDs3Type == null) {
            throw new IllegalArgumentException("Cannot compare two null Ds3Types");
        }
        if (oldDs3Type == null) {
            return new AddedDs3TypeDiff(newDs3Type);
        }
        if (newDs3Type == null) {
            return new DeletedDs3TypeDiff(oldDs3Type);
        }
        if (oldDs3Type.equals(newDs3Type)) {
            return new NoChangeDs3TypeDiff(newDs3Type);
        }
        return new ModifiedDs3TypeDiff(oldDs3Type, newDs3Type);
    }
}
