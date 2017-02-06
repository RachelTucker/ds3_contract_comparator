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

import com.spectralogic.ds3contractcomparator.models.request.NoChangeDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.Table;
import org.junit.Test;

import static com.spectralogic.ds3autogen.testutil.Ds3ModelFixtures.getBucketRequest;
import static com.spectralogic.ds3autogen.utils.ConverterUtil.hasContent;
import static com.spectralogic.ds3contractcomparator.print.htmlprinter.generators.HtmlRequestTableGenerator.*;
import static com.spectralogic.ds3contractcomparator.print.utils.Ds3SpecDiffFixture.getAddedRequest;
import static com.spectralogic.ds3contractcomparator.print.utils.Ds3SpecDiffFixture.getDeletedRequest;
import static com.spectralogic.ds3contractcomparator.print.utils.Ds3SpecDiffFixture.getModifiedRequest;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class HtmlRequestTableGenerator_Test {

    @Test (expected = IllegalArgumentException.class)
    public void toModifiedRequestTable_Exception_Test() {
        toModifiedRequestTable(getAddedRequest());
    }

    @Test
    public void toModifiedRequestTable_Test() {
        final Table result = toModifiedRequestTable(getModifiedRequest());
        assertThat(result.getTitle(), is("TestRequest (amazons3)"));
        assertThat(result.getAnchor(), is("TestRequestamazons3"));
        assertTrue(hasContent(result.getRows()));
    }

    @Test (expected = IllegalArgumentException.class)
    public void toDeletedRequestTable_Exception_Test() {
        toDeletedRequestTable(getAddedRequest());
    }

    @Test
    public void toDeletedRequestTable_Test() {
        final Table result = toDeletedRequestTable(getDeletedRequest());
        assertThat(result.getTitle(), is("GetBucketRequestHandler (amazons3)"));
        assertThat(result.getAnchor(), is("GetBucketRequestHandleramazons3"));
        assertTrue(hasContent(result.getRows()));
    }

    @Test (expected = IllegalArgumentException.class)
    public void toAddedRequestTable_Exception_Test() {
        toAddedRequestTable(getDeletedRequest());
    }

    @Test
    public void toAddedRequestTable_Test() {
        final Table result = toAddedRequestTable(getAddedRequest());
        assertThat(result.getTitle(), is("HeadBucketRequestHandler (amazons3)"));
        assertThat(result.getAnchor(), is("HeadBucketRequestHandleramazons3"));
        assertTrue(hasContent(result.getRows()));
    }

    @Test
    public void toRequestTable_Added_Test() {
        final Table result = toRequestTable(getAddedRequest());
        assertThat(result.getTitle(), is("HeadBucketRequestHandler (amazons3)"));
        assertThat(result.getAnchor(), is("HeadBucketRequestHandleramazons3"));
        assertTrue(hasContent(result.getRows()));
    }

    @Test
    public void toRequestTable_Deleted_Test() {
        final Table result = toRequestTable(getDeletedRequest());
        assertThat(result.getTitle(), is("GetBucketRequestHandler (amazons3)"));
        assertThat(result.getAnchor(), is("GetBucketRequestHandleramazons3"));
        assertTrue(hasContent(result.getRows()));
    }

    @Test
    public void toRequestTable_Modified_Test() {
        final Table result = toRequestTable(getModifiedRequest());
        assertThat(result.getTitle(), is("TestRequest (amazons3)"));
        assertThat(result.getAnchor(), is("TestRequestamazons3"));
        assertTrue(hasContent(result.getRows()));

    }

    @Test (expected = IllegalArgumentException.class)
    public void toRequestTable_NoChange_Test() {
        toRequestTable(new NoChangeDs3RequestDiff(getBucketRequest()));
    }
}
