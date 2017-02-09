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

package com.spectralogic.ds3contractcomparator.print;

import com.spectralogic.ds3contractcomparator.models.Ds3ApiSpecDiff;

/**
 * Interface for printing the contents of a {@link Ds3ApiSpecDiff}
 */
@FunctionalInterface
public interface Ds3SpecDiffPrinter {

    void print(final Ds3ApiSpecDiff specDiff);
}
