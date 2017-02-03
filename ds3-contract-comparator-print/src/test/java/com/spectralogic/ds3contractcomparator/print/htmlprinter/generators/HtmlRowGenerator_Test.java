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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Param;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3ResponseCode;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3ResponseType;
import org.junit.Test;

import java.lang.reflect.Field;

import static com.spectralogic.ds3contractcomparator.print.htmlprinter.generators.HtmlRowGenerator.*;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class HtmlRowGenerator_Test {

    private static final Ds3Param DELETED_PARAM = new Ds3Param("OldName", "OldType", false);
    private static final Ds3Param ADDED_PARAM = new Ds3Param("NewName", "NewType", false);
    private static final Ds3Param OLD_PARAM = new Ds3Param("ChangedParam", "Type1", false);
    private static final Ds3Param NEW_PARAM = new Ds3Param("ChangedParam", "Type2", false);
    private static final Ds3Param NO_CHANGE_PARAM = new Ds3Param("NoChangeParam", "NoChangeType", false);

    private static ImmutableList<Ds3Param> getOldParamList() {
        return ImmutableList.of(
                DELETED_PARAM,
                NO_CHANGE_PARAM,
                OLD_PARAM);
    }

    private static ImmutableList<Ds3Param> getNewParamList() {
        return ImmutableList.of(
                ADDED_PARAM,
                NO_CHANGE_PARAM,
                NEW_PARAM);
    }

    private static Ds3ResponseType getNullEntriesResponseType() {
        return new Ds3ResponseType("TestType", null, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getFields_NullEntries_Test() {
        getFields(null, null);
    }

    @Test
    public void getFields_OldNull_Test() {
        final Field[] result = getFields(null, NEW_PARAM);

        assertThat(result.length, is(3));
        assertThat(result[0].getName(), is("name"));
        assertThat(result[1].getName(), is("type"));
        assertThat(result[2].getName(), is("nullable"));
    }

    @Test
    public void getFields_NewNull_Test() {
        final Field[] result = getFields(OLD_PARAM, null);

        assertThat(result.length, is(3));
        assertThat(result[0].getName(), is("name"));
        assertThat(result[1].getName(), is("type"));
        assertThat(result[2].getName(), is("nullable"));
    }

    @Test
    public void getFields_Test() {
        final Field[] result = getFields(OLD_PARAM, NEW_PARAM);

        assertThat(result.length, is(3));
        assertThat(result[0].getName(), is("name"));
        assertThat(result[1].getName(), is("type"));
        assertThat(result[2].getName(), is("nullable"));
    }



    @Test
    public void getFields_NullValues_Test() {
        final Field[] result = getFields(getNullEntriesResponseType(), getNullEntriesResponseType());

        assertThat(result.length, is(3));
        assertThat(result[0].getName(), is("type"));
        assertThat(result[1].getName(), is("componentType"));
        assertThat(result[2].getName(), is("originalTypeName"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void isReadable_NullObjects_Test() {
        isReadable(null, null, "name");
    }

    @Test
    public void isReadable_OldNull_Test() {
        assertTrue(isReadable(null, NEW_PARAM, "name"));
        assertTrue(isReadable(null, NEW_PARAM, "type"));
        assertTrue(isReadable(null, NEW_PARAM, "nullable"));
        assertFalse(isReadable(null, NEW_PARAM, "doesNotExist"));
    }

    @Test
    public void isReadableNewNull_Test() {
        assertTrue(isReadable(OLD_PARAM, null, "name"));
        assertTrue(isReadable(OLD_PARAM, null, "type"));
        assertTrue(isReadable(OLD_PARAM, null, "nullable"));
        assertFalse(isReadable(OLD_PARAM, null, "doesNotExist"));
    }

    @Test
    public void isReadable_Test() {
        assertTrue(isReadable(OLD_PARAM, NEW_PARAM, "name"));
        assertTrue(isReadable(OLD_PARAM, NEW_PARAM, "type"));
        assertTrue(isReadable(OLD_PARAM, NEW_PARAM, "nullable"));
        assertFalse(isReadable(OLD_PARAM, NEW_PARAM, "doesNotExist"));
    }

    @Test
    public void isReadable_NullValues_Test() {
        assertTrue(isReadable(getNullEntriesResponseType(), getNullEntriesResponseType(), "type"));
        assertTrue(isReadable(getNullEntriesResponseType(), getNullEntriesResponseType(), "componentType"));
        assertTrue(isReadable(getNullEntriesResponseType(), getNullEntriesResponseType(), "originalTypeName"));
        assertFalse(isReadable(getNullEntriesResponseType(), getNullEntriesResponseType(), "doesNotExist"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void getPropertyName_NullObjects_Test() {
        getPropertyName(null, null);
    }

    @Test
    public void getPropertyName_OldNull_Test() {
        final String result = getPropertyName(null, NEW_PARAM);
        assertThat(result, is("name"));
    }

    @Test
    public void getPropertyName_NewNull_Test() {
        final String result = getPropertyName(OLD_PARAM, null);
        assertThat(result, is("name"));
    }

    @Test
    public void getPropertyName_NameProperty_Test() {
        final String result = getPropertyName(OLD_PARAM, NEW_PARAM);
        assertThat(result, is("name"));
    }

    @Test
    public void getPropertyName_CodeProperty_Test() {
        final Ds3ResponseCode responseCode = new Ds3ResponseCode(203, ImmutableList.of());
        final String result = getPropertyName(responseCode, responseCode);
        assertThat(result, is("code"));
    }

    @Test
    public void getPropertyName_TypeProperty_Test() {
        final String result = getPropertyName(getNullEntriesResponseType(), getNullEntriesResponseType());
        assertThat(result, is("type"));
    }

    private class TestUnknownProperty {
        public int testVal;
    }

    @Test
    public void getPropertyName_UnknownProperty_Test() {
        final String result = getPropertyName(new TestUnknownProperty(), new TestUnknownProperty());
        assertThat(result, is(""));
    }

    @Test (expected = IllegalArgumentException.class)
    public void getPropertyNameFromList_NullLists_Test() {
        getPropertyNameFromList(null, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void getPropertyNameFromList_EmptyLists_Test() {
        getPropertyNameFromList(ImmutableList.of(), ImmutableList.of());
    }

    @Test
    public void getPropertyNameFromList_OldEmpty_Test() {
        final String result = getPropertyNameFromList(null, getNewParamList());
        assertThat(result, is("name"));
    }

    @Test
    public void getPropertyNameFromList_NewEmpty_Test() {
        final String result = getPropertyNameFromList(getOldParamList(), null);
        assertThat(result, is("name"));
    }

    @Test
    public void getPropertyNameFromList_Test() {
        final String result = getPropertyNameFromList(getOldParamList(), getNewParamList());
        assertThat(result, is("name"));
    }

    @Test
    public void toPropertyMap_NullList_Test() {
        final ImmutableMap<String, Ds3Param> result = toPropertyMap(null, "name");
        assertThat(result.size(), is(0));
    }

    @Test
    public void toPropertyMap_EmptyList_Test() {
        final ImmutableMap<String, Ds3Param> result = toPropertyMap(ImmutableList.of(), "name");
        assertThat(result.size(), is(0));
    }

    @Test
    public void toPropertyMap_Test() {
        final ImmutableMap<String, Ds3Param> result = toPropertyMap(getOldParamList(), "name");
        assertThat(result.size(), is(3));

        assertTrue(result.containsKey(NO_CHANGE_PARAM.getName()));
        assertTrue(result.containsKey(OLD_PARAM.getName()));
        assertTrue(result.containsKey(DELETED_PARAM.getName()));

        assertThat(result.get(NO_CHANGE_PARAM.getName()), is(NO_CHANGE_PARAM));
        assertThat(result.get(OLD_PARAM.getName()), is(OLD_PARAM));
        assertThat(result.get(DELETED_PARAM.getName()), is(DELETED_PARAM));
    }

    @Test
    public void toPropertyUnion_NullLists_Test() {
        final ImmutableSet<String> result = toPropertyUnion(null, null, "name");
        assertThat(result.size(), is(0));
    }

    @Test
    public void toPropertyUnion_EmptyLists_Test() {
        final ImmutableSet<String> result = toPropertyUnion(ImmutableList.of(), ImmutableList.of(), "name");
        assertThat(result.size(), is(0));
    }

    @Test
    public void toPropertyUnion_NullOld_Test() {
        final ImmutableSet<String> result = toPropertyUnion(null, getNewParamList(), "name");
        assertThat(result.size(), is(3));
        assertThat(result, hasItem(NO_CHANGE_PARAM.getName()));
        assertThat(result, hasItem(NEW_PARAM.getName()));
        assertThat(result, hasItem(ADDED_PARAM.getName()));
    }

    @Test
    public void toPropertyUnion_NullNew_Test() {
        final ImmutableSet<String> result = toPropertyUnion(getOldParamList(), null, "name");
        assertThat(result.size(), is(3));
        assertThat(result, hasItem(NO_CHANGE_PARAM.getName()));
        assertThat(result, hasItem(OLD_PARAM.getName()));
        assertThat(result, hasItem(DELETED_PARAM.getName()));
    }

    @Test
    public void toPropertyUnion_Test() {
        final ImmutableSet<String> result = toPropertyUnion(getOldParamList(), getNewParamList(), "name");
        assertThat(result.size(), is(4));
        assertThat(result, hasItem(NO_CHANGE_PARAM.getName()));
        assertThat(result, hasItem(OLD_PARAM.getName()));
        assertThat(result, hasItem(DELETED_PARAM.getName()));
        assertThat(result, hasItem(ADDED_PARAM.getName()));
    }
}
