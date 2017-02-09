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

package com.spectralogic.ds3contractcomparator.print.htmlprinter.models

import com.google.common.collect.ImmutableList
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.Section
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.index.IndexSection

/**
 * Represents the Html report
 */
data class HtmlReport(val oldContract: String,
                      val newContract: String,
                      val indexSections: ImmutableList<IndexSection>,
                      val sections: ImmutableList<Section>)
