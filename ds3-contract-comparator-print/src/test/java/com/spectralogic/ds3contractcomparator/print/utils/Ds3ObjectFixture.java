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
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Param;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3ResponseType;

/**
 * Contains fixtures for simple Ds3 Objects
 */
public class Ds3ObjectFixture {

    public static final Ds3Param DELETED_PARAM = new Ds3Param("OldName", "OldType", false);
    public static final Ds3Param ADDED_PARAM = new Ds3Param("NewName", "NewType", false);
    public static final Ds3Param OLD_PARAM = new Ds3Param("ChangedParam", "Type1", false);
    public static final Ds3Param NEW_PARAM = new Ds3Param("ChangedParam", "Type2", false);
    public static final Ds3Param NO_CHANGE_PARAM = new Ds3Param("NoChangeParam", "NoChangeType", false);
    public static final Ds3Param PARAM_WITH_PATHS = new Ds3Param("com.test.TestName", "com.test.TestType", false);

    private Ds3ObjectFixture() {
        //pass
    }

    public static ImmutableList<Ds3Param> getOldParamList() {
        return ImmutableList.of(
                DELETED_PARAM,
                NO_CHANGE_PARAM,
                OLD_PARAM);
    }

    public static ImmutableList<Ds3Param> getNewParamList() {
        return ImmutableList.of(
                ADDED_PARAM,
                NO_CHANGE_PARAM,
                NEW_PARAM);
    }

    public static Ds3ResponseType getNullEntriesResponseType() {
        return new Ds3ResponseType("TestType", null, null);
    }

    public static Ds3Annotation getOldAnnotation() {
        return new Ds3Annotation("Name", ImmutableList.of(
                new Ds3AnnotationElement("Element1", "Val1", "ValType1"),
                new Ds3AnnotationElement("Element2", "Val2", "ValType2")));
    }

    public static Ds3Annotation getNewAnnotation() {
        return new Ds3Annotation("Name", ImmutableList.of(
                new Ds3AnnotationElement("Element1", "Val1", "ValType1"),
                new Ds3AnnotationElement("Element2", "Val3", "ValType3")));
    }
}
