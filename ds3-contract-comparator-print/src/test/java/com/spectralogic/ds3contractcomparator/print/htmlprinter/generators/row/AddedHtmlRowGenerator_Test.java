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

package com.spectralogic.ds3contractcomparator.print.htmlprinter.generators.row;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.rows.AddedRow;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.rows.NoChangeRow;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.rows.Row;
import org.junit.Test;

import static com.spectralogic.ds3contractcomparator.print.htmlprinter.generators.row.AddedHtmlRowGenerator.createAddedRows;
import static com.spectralogic.ds3contractcomparator.print.utils.Ds3ObjectFixture.NEW_PARAM;
import static com.spectralogic.ds3contractcomparator.print.utils.Ds3ObjectFixture.getNewAnnotation;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class AddedHtmlRowGenerator_Test {

    @Test
    public void createAddedRows_Test() {
        final ImmutableList<Row> result = createAddedRows(NEW_PARAM, 0);
        assertThat(result.size(), is(3));
        result.forEach(r -> assertTrue(r instanceof AddedRow));
    }

    @Test
    public void createAddedRows_ListInput_Test() {
        final ImmutableList<Row> result = createAddedRows(getNewAnnotation(), 0);
        assertThat(result.size(), is(8));

        //Describe Annotation
        assertThat(result.get(0).getLabel(), is("name"));
        assertTrue(result.get(0) instanceof AddedRow);
        assertThat(result.get(1).getLabel(), is("ds3AnnotationElements"));
        assertTrue(result.get(1) instanceof NoChangeRow);

        //Describe Annotation Element 1
        assertThat(result.get(2).getLabel(), is("name"));
        assertTrue(result.get(2) instanceof AddedRow);

        assertThat(result.get(3).getLabel(), is("value"));
        assertTrue(result.get(3) instanceof AddedRow);

        assertThat(result.get(4).getLabel(), is("valueType"));
        assertTrue(result.get(4) instanceof AddedRow);

        //Describe Annotation Element 2
        assertThat(result.get(5).getLabel(), is("name"));
        assertTrue(result.get(5) instanceof AddedRow);

        assertThat(result.get(6).getLabel(), is("value"));
        assertTrue(result.get(6) instanceof AddedRow);
        assertThat(result.get(6).getNewVal(), is("Val3"));

        assertThat(result.get(7).getLabel(), is("valueType"));
        assertTrue(result.get(7) instanceof AddedRow);
        assertThat(result.get(7).getNewVal(), is("ValType3"));
    }
}
