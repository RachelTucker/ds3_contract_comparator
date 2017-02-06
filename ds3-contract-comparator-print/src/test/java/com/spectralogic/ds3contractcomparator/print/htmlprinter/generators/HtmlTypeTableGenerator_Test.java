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

package com.spectralogic.ds3contractcomparator.print.htmlprinter.generators;

import com.spectralogic.ds3contractcomparator.models.type.NoChangeDs3TypeDiff;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.Table;
import org.junit.Test;

import static com.spectralogic.ds3autogen.utils.ConverterUtil.hasContent;
import static com.spectralogic.ds3contractcomparator.print.htmlprinter.generators.HtmlTypeTableGenerator.*;
import static com.spectralogic.ds3contractcomparator.print.utils.Ds3SpecDiffFixture.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class HtmlTypeTableGenerator_Test {

    @Test (expected = IllegalArgumentException.class)
    public void toModifiedTypeTable_Exception_Test() {
        toModifiedTypeTable(getDeletedType());
    }

    @Test
    public void toModifiedTypeTable_Test() {
        final Table result = toModifiedTypeTable(getModdifiedType());
        assertThat(result.getTitle(), is("ModifiedType"));
        assertThat(result.getAnchor(), is("ModifiedType"));
        assertTrue(hasContent(result.getRows()));
    }

    @Test (expected = IllegalArgumentException.class)
    public void toDeletedTypeTable_Exception_Test() {
        toDeletedTypeTable(getAddedType());
    }

    @Test
    public void toDeletedTypeTable_Test() {
        final Table result = toDeletedTypeTable(getDeletedType());
        assertThat(result.getTitle(), is("DeletedType"));
        assertThat(result.getAnchor(), is("DeletedType"));
        assertTrue(hasContent(result.getRows()));
    }

    @Test (expected = IllegalArgumentException.class)
    public void toAddedTypeTable_Exception_Test() {
        toAddedTypeTable(getDeletedType());
    }

    @Test
    public void toAddedTypeTable_Test() {
        final Table result = toAddedTypeTable(getAddedType());
        assertThat(result.getTitle(), is("AddedType"));
        assertThat(result.getAnchor(), is("AddedType"));
        assertTrue(hasContent(result.getRows()));
    }

    @Test
    public void toTypeTable_Added_Test() {
        final Table result = toTypeTable(getAddedType());
        assertThat(result.getTitle(), is("AddedType"));
        assertThat(result.getAnchor(), is("AddedType"));
        assertTrue(hasContent(result.getRows()));
    }

    @Test
    public void toTypeTable_Deleted_Test() {
        final Table result = toTypeTable(getDeletedType());
        assertThat(result.getTitle(), is("DeletedType"));
        assertThat(result.getAnchor(), is("DeletedType"));
        assertTrue(hasContent(result.getRows()));
    }

    @Test
    public void toTypeTable_Modified_Test() {
        final Table result = toTypeTable(getModdifiedType());
        assertThat(result.getTitle(), is("ModifiedType"));
        assertThat(result.getAnchor(), is("ModifiedType"));
        assertTrue(hasContent(result.getRows()));
    }

    @Test (expected = IllegalArgumentException.class)
    public void toTypeTable_NoChange_Test() {
        toTypeTable(new NoChangeDs3TypeDiff(getTestType()));
    }
}
