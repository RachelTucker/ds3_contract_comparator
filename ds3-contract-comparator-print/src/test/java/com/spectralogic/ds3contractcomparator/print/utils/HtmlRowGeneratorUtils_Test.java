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

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Annotation;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3AnnotationElement;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3ResponseCode;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3ResponseType;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Optional;

import static com.spectralogic.ds3contractcomparator.print.utils.Ds3ObjectFixture.*;
import static com.spectralogic.ds3contractcomparator.print.utils.HtmlRowGeneratorUtils.*;
import static com.spectralogic.ds3contractcomparator.print.utils.TestUtils.getObjectField;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class HtmlRowGeneratorUtils_Test {

    @Test (expected = IllegalArgumentException.class)
    public void getListPropertyFromObject_WrongField_Test() throws NoSuchFieldException {
        final Ds3Annotation annotation = getOldAnnotation();
        final Field field = getObjectField(annotation, "name");
        getListPropertyFromObject(field, annotation);
    }

    @Test
    public void getListPropertyFromObject_Test() throws NoSuchFieldException {
        final Ds3Annotation annotation = getOldAnnotation();
        final Field field = getObjectField(annotation, "ds3AnnotationElements");
        final ImmutableList<Ds3AnnotationElement> result = getListPropertyFromObject(field, annotation);
        assertThat(result.size(), is(2));
    }

    @Test (expected = IllegalArgumentException.class)
    public void getUniqueProperty_NullObject_Test() {
        getUniqueProperty(null);
    }

    @Test
    public void getUniqueProperty_NameProperty_Test() {
        final String result = getUniqueProperty(NO_CHANGE_PARAM);
        assertThat(result, is("name"));
    }

    @Test
    public void getUniqueProperty_CodeProperty_Test() {
        final Ds3ResponseCode code = new Ds3ResponseCode(200, ImmutableList.of());
        final String result = getUniqueProperty(code);
        assertThat(result, is("code"));
    }

    @Test
    public void getUniqueProperty_TypeProperty_Test() {
        final Ds3ResponseType type = new Ds3ResponseType("Type","Component", "Original");
        final String result = getUniqueProperty(type);
        assertThat(result, is("type"));
    }

    @Test
    public void getUniqueProperty_UnknownProperty_Test() {
        final String result = getUniqueProperty(new TestUnknownProperty());
        assertThat(result, is(""));
    }

    @Test
    public void toFieldIndent_UniqueProperty_Test() throws NoSuchFieldException {
        final Field field = getObjectField(NO_CHANGE_PARAM, "name");
        final int result = toFieldIndent(1, NO_CHANGE_PARAM, field);
        assertThat(result, is(1));
    }

    @Test
    public void toFieldIndent_NotUniqueProperty_Test() throws NoSuchFieldException {
        final Field field = getObjectField(NO_CHANGE_PARAM, "type");
        final int result = toFieldIndent(1, NO_CHANGE_PARAM, field);
        assertThat(result, is(2));
    }

    @Test
    public void getPropertyValue_NullObject_Test() {
        final Optional<String> result = getPropertyValue(null, "Val");
        assertFalse(result.isPresent());
    }

    @Test
    public void getPropertyValue_NullValue_Test() {
        final Optional<String> result = getPropertyValue(getNullEntriesResponseType(), "componentType");
        assertFalse(result.isPresent());
    }

    @Test
    public void getPropertyValue_NameValue_Test() {
        final Optional<String> result = getPropertyValue(PARAM_WITH_PATHS, "name");
        assertTrue(result.isPresent());
        assertThat(result.get(), is("TestName"));
    }

    @Test
    public void getPropertyValue_TypeValue_Test() {
        final Optional<String> result = getPropertyValue(PARAM_WITH_PATHS, "type");
        assertTrue(result.isPresent());
        assertThat(result.get(), is("TestType"));
    }

    @Test
    public void getPropertyValue_DoesNotExist_Test() {
        final Optional<String> result = getPropertyValue(PARAM_WITH_PATHS, "doesNotExist");
        assertFalse(result.isPresent());
    }

    @Test
    public void getPropertyValue_Test() {
        final Optional<String> result = getPropertyValue(PARAM_WITH_PATHS, "nullable");
        assertTrue(result.isPresent());
        assertThat(result.get(), is("false"));
    }
}
