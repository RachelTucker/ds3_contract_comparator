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

package com.spectralogic.ds3contractcomparator.print.utils;

import com.spectralogic.ds3autogen.api.models.enums.Classification;

import static com.spectralogic.ds3autogen.utils.NormalizingContractNamesUtil.removePath;

/**
 * Utils used in the generation of the HTML report.
 */
public class HtmlPrinterUtils {

    //TODO test
    /**
     * Creates the title for a request used in index and request headers
     */
    public static String toRequestTitle(final String name, final Classification classification) {
        return removePath(name) + " (" + classification.toString() + ")";
    }

    //TODO test
    /**
     * Creates the HTML anchor for a request used in creating internal links
     */
    public static String toRequestAnchor(final String name, final Classification classification) {
        return removePath(name) + classification.toString();
    }
}
