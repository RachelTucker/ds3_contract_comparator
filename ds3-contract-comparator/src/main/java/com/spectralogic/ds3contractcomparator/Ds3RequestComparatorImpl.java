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

import com.spectralogic.ds3autogen.api.models.apispec.Ds3Request;
import com.spectralogic.ds3contractcomparator.models.request.*;
import org.jetbrains.annotations.Nullable;

public class Ds3RequestComparatorImpl implements Ds3RequestComparator {

    /**
     * Compares two {@link Ds3Request} and creates a {@link AbstractDs3RequestDiff} representing the
     * change(s) in status, if change(s) exist between the {@link Ds3Request}.
     * Status change(s) consist of: No-Change, Added, Deleted, and Modified
     * @param oldRequest the older API version of the {@link Ds3Request} or null if it does not exist within the old version
     * @param newRequest the newer API version of the {@link Ds3Request} or null if it does not exist within the new version
     * @return
     *   {@link NoChangeDs3RequestDiff} if {@code oldRequest} and {@code newRequest} are non-null and value equals
     *   {@link AddedDs3RequestDiff} if {@code oldRequest} is null and {@code newRequest} is non-null
     *   {@link DeletedDs3RequestDiff} if {@code oldRequest} is non-null and {@code newRequest} is null
     *   {@link ModifiedDs3RequestDiff} if {@code oldRequest} and {@code newRequest} are not the same
     * @throws IllegalArgumentException if {@code oldRequest} and {@code newRequest} are both null
     */
    @Override
    public AbstractDs3RequestDiff compare(@Nullable final Ds3Request oldRequest, @Nullable final Ds3Request newRequest) {
        if (oldRequest == null && newRequest == null) {
            throw new IllegalArgumentException("Cannot compare two null Ds3Requests");
        }
        if (oldRequest == null) {
            return new AddedDs3RequestDiff(newRequest);
        }
        if (newRequest == null) {
            return new DeletedDs3RequestDiff(oldRequest);
        }
        if (oldRequest.equals(newRequest)) {
            return new NoChangeDs3RequestDiff(newRequest);
        }
        return new ModifiedDs3RequestDiff(oldRequest, newRequest);
    }
}
