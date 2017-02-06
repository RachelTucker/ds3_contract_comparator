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
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.DeletedRow;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.rows.NoChangeRow;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.rows.Row;
import org.junit.Test;

import static com.spectralogic.ds3contractcomparator.print.htmlprinter.generators.row.DeletedHtmlRowGenerator.createDeletedRows;
import static com.spectralogic.ds3contractcomparator.print.utils.Ds3ObjectFixture.DELETED_PARAM;
import static com.spectralogic.ds3contractcomparator.print.utils.Ds3ObjectFixture.getOldAnnotation;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DeletedHtmlRowGenerator_Test {

    @Test
    public void createDeletedRows_Test() {
        final ImmutableList<Row> result = createDeletedRows(DELETED_PARAM, 0);
        assertThat(result.size(), is(3));
        result.forEach(r -> assertTrue(r instanceof DeletedRow));
    }

    @Test
    public void createDeletedRows_ListInput_Test() {
        final ImmutableList<Row> result = createDeletedRows(getOldAnnotation(), 0);
        assertThat(result.size(), is(8));

        //Describe Annotation
        assertThat(result.get(0).getLabel(), is("name"));
        assertTrue(result.get(0) instanceof DeletedRow);
        assertThat(result.get(1).getLabel(), is("ds3AnnotationElements"));
        assertTrue(result.get(1) instanceof NoChangeRow);

        //Describe Annotation Element 1
        assertThat(result.get(2).getLabel(), is("name"));
        assertTrue(result.get(2) instanceof DeletedRow);

        assertThat(result.get(3).getLabel(), is("value"));
        assertTrue(result.get(3) instanceof DeletedRow);

        assertThat(result.get(4).getLabel(), is("valueType"));
        assertTrue(result.get(4) instanceof DeletedRow);

        //Describe Annotation Element 2
        assertThat(result.get(5).getLabel(), is("name"));
        assertTrue(result.get(5) instanceof DeletedRow);

        assertThat(result.get(6).getLabel(), is("value"));
        assertTrue(result.get(6) instanceof DeletedRow);
        assertThat(result.get(6).getOldVal(), is("Val2"));

        assertThat(result.get(7).getLabel(), is("valueType"));
        assertTrue(result.get(7) instanceof DeletedRow);
        assertThat(result.get(7).getOldVal(), is("ValType2"));
    }
}
