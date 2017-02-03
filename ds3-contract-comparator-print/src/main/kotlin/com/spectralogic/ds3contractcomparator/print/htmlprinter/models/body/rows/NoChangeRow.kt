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

package com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.rows

import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.rows.Row

/**
 * A table row that has not changed in value between contracts
 */
class NoChangeRow(override val indent: Int,
                  override val label: String,
                  val value: String) : Row {

    override val oldVal: String
        get() = value

    override val newVal: String
        get() = value

    override val oldColor: String
        get() = RowConstants.NO_COLOR

    override val newColor: String
        get() = RowConstants.NO_COLOR
}
