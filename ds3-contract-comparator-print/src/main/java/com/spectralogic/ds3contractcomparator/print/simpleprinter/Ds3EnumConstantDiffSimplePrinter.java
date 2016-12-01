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

package com.spectralogic.ds3contractcomparator.print.simpleprinter;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3EnumConstant;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Property;
import com.spectralogic.ds3contractcomparator.print.utils.WriterHelper;
import org.jetbrains.annotations.Nullable;

import static com.google.common.collect.Iterables.isEmpty;
import static com.spectralogic.ds3autogen.utils.ConverterUtil.hasContent;
import static com.spectralogic.ds3contractcomparator.print.simpleprinter.Ds3PropertyDiffSimplePrinter.printPropertyDiff;
import static com.spectralogic.ds3contractcomparator.print.utils.SimplePrinterUtils.indent;
import static com.spectralogic.ds3contractcomparator.print.utils.SimplePrinterUtils.printModifiedLine;

/**
 * Prints the difference in a {@link Ds3EnumConstant} between two versions of a contract
 */
class Ds3EnumConstantDiffSimplePrinter {

    private static final int INDENT = 2;

    //todo test
    /**
     * Prints the difference in a {@link Ds3EnumConstant} between two versions of a contract
     */
    static void printEnumConstantDiff(
            @Nullable final Ds3EnumConstant oldEnum,
            @Nullable final Ds3EnumConstant newEnum,
            final WriterHelper writer,
            final boolean printProperties) {
        if (oldEnum == null && newEnum == null) {
            throw new IllegalArgumentException("Cannot print difference between two null Ds3Elements");
        }
        if (oldEnum == null) {
            printAddedEnumConstant(newEnum, writer, printProperties);
            return;
        }
        if (newEnum == null) {
            printDeletedEnumConstant(oldEnum, writer, printProperties);
            return;
        }
        printModifiedEnumConstant(oldEnum, newEnum, writer, printProperties);
    }

    //TODO test
    /**
     * Prints a {@link Ds3EnumConstant} that exists in the newer contract but not in the older contract
     */
    private static void printAddedEnumConstant(final Ds3EnumConstant newEnum, final WriterHelper writer, final boolean printProperties) {
        printModifiedLine("EnumConstantName:", "N/A", newEnum.getName(), INDENT, writer);
        printProperties(ImmutableList.of(), newEnum.getDs3Properties(), printProperties, writer);
    }

    //TODO test
    /**
     * Prints a {@link Ds3EnumConstant} that exists in the older contract but not in the newer contract
     */
    private static void printDeletedEnumConstant(final Ds3EnumConstant oldEnum, final WriterHelper writer, final boolean printProperties) {
        printModifiedLine("EnumConstantName:", oldEnum.getName(), "N/A", INDENT, writer);
        printProperties(oldEnum.getDs3Properties(), ImmutableList.of(), printProperties, writer);
    }

    //TODO test
    /**
     * Prints a {@link Ds3EnumConstant} that was modified between contract versions
     */
    private static void printModifiedEnumConstant(
            final Ds3EnumConstant oldEnum,
            final Ds3EnumConstant newEnum,
            final WriterHelper writer,
            final boolean printProperties) {
        printModifiedLine("EnumConstantName:", oldEnum.getName(), newEnum.getName(), INDENT, writer);
        printProperties(oldEnum.getDs3Properties(), newEnum.getDs3Properties(), printProperties, writer);
    }

    //TODO test
    /**
     * Prints the changes between two {@link ImmutableList} of {@link Ds3Property}. If both lists are
     * empty, then nothing is printed.
     */
    private static void printProperties(
            final ImmutableList<Ds3Property> oldProperties,
            final ImmutableList<Ds3Property> newProperties,
            final boolean printProperties,
            final WriterHelper writer) {
        if (!printProperties || (isEmpty(oldProperties) && isEmpty(newProperties))) {
            //do not print empty values
            return;
        }
        writer.append(indent(INDENT + 1)).append("Properties:").append("\n");

        final ImmutableSet<String> propertyNames = getPropertyNameUnion(oldProperties, newProperties);
        final ImmutableMap<String, Ds3Property> oldPropertyMap = toPropertyMap(oldProperties);
        final ImmutableMap<String, Ds3Property> newPropertyMap = toPropertyMap(newProperties);

        propertyNames.forEach(name -> printPropertyDiff(
                oldPropertyMap.get(name),
                newPropertyMap.get(name),
                writer));
    }

    //todo test
    /**
     * Gets the union of names of all properties within two {@link ImmutableList} of {@link Ds3Property}
     */
    private static ImmutableSet<String> getPropertyNameUnion(
            final ImmutableList<Ds3Property> oldProperties,
            final ImmutableList<Ds3Property> newProperties) {
        final ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        if (hasContent(oldProperties)) {
            oldProperties.forEach(property -> builder.add(property.getName()));
        }
        if (hasContent(newProperties)) {
            newProperties.forEach(property -> builder.add(property.getName()));
        }
        return builder.build();
    }

    //todo test
    /**
     * Converts an {@link ImmutableList} of {@link Ds3Property} into an {@link ImmutableMap} of
     * property names and {@link Ds3Property}
     */
    private static ImmutableMap<String, Ds3Property> toPropertyMap(final ImmutableList<Ds3Property> properties) {
        if (isEmpty(properties)) {
            return ImmutableMap.of();
        }
        final ImmutableMap.Builder<String, Ds3Property> builder = ImmutableMap.builder();
        properties.forEach(property -> builder.put(property.getName(), property));
        return builder.build();
    }
}
