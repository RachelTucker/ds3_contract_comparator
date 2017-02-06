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
import org.junit.Test;

import static com.spectralogic.ds3contractcomparator.print.utils.HtmlPrinterUtils.toRequestAnchor;
import static com.spectralogic.ds3contractcomparator.print.utils.HtmlPrinterUtils.toRequestTitle;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HtmlPrinterUtils_Test {

    @Test
    public void toRequestTitle_Test() {
        assertThat(toRequestTitle("com.test.Request", Classification.amazons3), is("Request (amazons3)"));
        assertThat(toRequestTitle("com.test.Request", Classification.spectrads3), is("Request (spectrads3)"));
        assertThat(toRequestTitle("com.test.Request", Classification.spectrainternal), is("Request (spectrainternal)"));
    }

    @Test
    public void toRequestAnchor_Test() {
        assertThat(toRequestAnchor("com.test.Request", Classification.amazons3), is("Requestamazons3"));
        assertThat(toRequestAnchor("com.test.Request", Classification.spectrads3), is("Requestspectrads3"));
        assertThat(toRequestAnchor("com.test.Request", Classification.spectrainternal), is("Requestspectrainternal"));
    }
}
