/*
 * ******************************************************************************
 *   Copyright 2016 Spectra Logic Corporation. All Rights Reserved.
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

package com.spectralogic.ds3contractcomparator.utils;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Element;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3EnumConstant;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Type;

/**
 * Contains fixtures for creating test data
 */
public final class TestDataFixture {

    private static final String DEFAULT_NAME = "com.test.TestType";

    public static Ds3Type createSimpleTestType() {
        return createSimpleTestType("");
    }

    public static Ds3Type createSimpleTestType(final String nameToMarshal) {
        return createSimpleTestType(DEFAULT_NAME, nameToMarshal);
    }
    public static Ds3Type createSimpleTestType(final String name, final String nameToMarshal) {
        return createTestTypeEnum(
                name,
                nameToMarshal,
                new Ds3EnumConstant("SIMPLE_ENUM", ImmutableList.of()));
    }

    public static Ds3Type createTestTypeElement(final Ds3Element ds3Element) {
        return createTestTypeElement(DEFAULT_NAME, ds3Element);
    }

    public static Ds3Type createTestTypeElement(final String name, final Ds3Element ds3Element) {
        return new Ds3Type(
                name,
                "NameToMarshal",
                ImmutableList.of(
                        ds3Element,
                        new Ds3Element("DefaultName", "DefaultType", "DefaultComponent", true)),
                ImmutableList.of());
    }

    public static Ds3Type createTestTypeEnum(final String nameToMarshal, final Ds3EnumConstant ds3EnumConstant) {
        return createTestTypeEnum(DEFAULT_NAME, nameToMarshal, ds3EnumConstant);
    }

    public static Ds3Type createTestTypeEnum(
            final String name,
            final String nameToMarshal,
            final Ds3EnumConstant ds3EnumConstant) {
        return new Ds3Type(
                name,
                nameToMarshal,
                ImmutableList.of(),
                ImmutableList.of(
                        ds3EnumConstant,
                        new Ds3EnumConstant("DEFAULT_ENUM", ImmutableList.of())));
    }
}
