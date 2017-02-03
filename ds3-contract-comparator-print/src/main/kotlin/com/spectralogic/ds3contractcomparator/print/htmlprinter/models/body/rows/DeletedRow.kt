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

package com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body

import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.rows.Row
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.rows.RowConstants

/**
 * A table row that represents that an element was deleted
 */
class DeletedRow(override val indent: Int,
                 override val label: String,
                 override val oldVal: String) : Row {

    override val oldColor: String
        get() = RowConstants.DELETED_COLOR

    override val newColor: String
        get() = RowConstants.NO_COLOR

    override val newVal: String
        get() = RowConstants.NA
}
