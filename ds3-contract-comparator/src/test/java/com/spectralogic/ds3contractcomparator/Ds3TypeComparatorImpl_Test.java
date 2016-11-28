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

package com.spectralogic.ds3contractcomparator;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Element;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3EnumConstant;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Property;
import com.spectralogic.ds3contractcomparator.models.type.*;
import org.junit.Test;

import static com.spectralogic.ds3contractcomparator.utils.TestDataFixture.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class Ds3TypeComparatorImpl_Test {

    private final Ds3TypeComparatorImpl comparator = new Ds3TypeComparatorImpl();

    @Test (expected = IllegalArgumentException.class)
    public void compare_BothNull_Test() {
        comparator.compare(null, null);
    }

    @Test
    public void compare_OldNull_Test() {
        final AbstractDs3TypeDiff result = comparator.compare(
                null,
                createSimpleTestType());
        assertThat(result, instanceOf(AddedDs3TypeDiff.class));
    }

    @Test
    public void compare_NewNull_Test() {
        final AbstractDs3TypeDiff result = comparator.compare(
                createSimpleTestType(),
                null);
        assertThat(result, instanceOf(DeletedDs3TypeDiff.class));
    }

    @Test
    public void compare_SameType_Test() {
        final AbstractDs3TypeDiff result = comparator.compare(
                createSimpleTestType(),
                createSimpleTestType());
        assertThat(result, instanceOf(NoChangeDs3TypeDiff.class));
    }

    @Test
    public void compare_ModifiedTypeHeader_Test() {
        final AbstractDs3TypeDiff result = comparator.compare(
                createSimpleTestType("NameToMarshalOne"),
                createSimpleTestType("NameToMarshalTwo"));
        assertThat(result, instanceOf(ModifiedDs3TypeDiff.class));
    }

    @Test
    public void compare_ModifiedTypeElement_Test() {
        final AbstractDs3TypeDiff result = comparator.compare(
                createTestTypeElement(
                        new Ds3Element("MyElement", "MyType", "MyComponent1", false)),
                createTestTypeElement(
                        new Ds3Element("MyElement", "MyType", "MyComponent2", false)));
        assertThat(result, instanceOf(ModifiedDs3TypeDiff.class));
    }

    @Test
    public void compareModifiedTypeEnum_Test() {
        final AbstractDs3TypeDiff result = comparator.compare(
                createTestTypeEnum(
                        "NameToMarshal",
                        new Ds3EnumConstant("MyName", ImmutableList.of())),
                createTestTypeEnum(
                        "NameToMarshal",
                        new Ds3EnumConstant("MyName", ImmutableList.of(
                                new Ds3Property("Property", "Value", "Type")))));
        assertThat(result, instanceOf(ModifiedDs3TypeDiff.class));
    }
}
