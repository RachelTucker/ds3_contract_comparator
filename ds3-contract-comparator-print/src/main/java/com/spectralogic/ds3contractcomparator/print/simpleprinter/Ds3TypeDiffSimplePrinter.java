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
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Element;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3EnumConstant;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Type;
import com.spectralogic.ds3contractcomparator.models.type.*;
import com.spectralogic.ds3contractcomparator.print.utils.WriterHelper;

import static com.spectralogic.ds3autogen.utils.ConverterUtil.hasContent;
import static com.spectralogic.ds3autogen.utils.ConverterUtil.isEmpty;
import static com.spectralogic.ds3autogen.utils.NormalizingContractNamesUtil.removePath;
import static com.spectralogic.ds3contractcomparator.print.simpleprinter.Ds3ElementDiffSimplePrinter.printElementDiff;
import static com.spectralogic.ds3contractcomparator.print.simpleprinter.Ds3EnumConstantDiffSimplePrinter.printEnumConstantDiff;
import static com.spectralogic.ds3contractcomparator.print.utils.SimplePrinterUtils.*;

/**
 * Prints the difference in a {@link Ds3Type} between two versions of a contract.
 * Prints a {@link Ds3Type} if it was added, deleted or modified.
 * If the {@link Ds3Type} was not changed between contract versions, it is not printed.
 */
public final class Ds3TypeDiffSimplePrinter {

    private static final int LABEL_WIDTH = 20;
    private static final int COLUMN_WIDTH = 50;
    private static final int INDENT = 1;

    /**
     * Prints the changes in a {@link Ds3TypeDiff} if the type was modified, added or changed.
     * If there was no change, then nothing is printed.
     */
    public static void printTypeDiff(final Ds3TypeDiff typeDiff, final WriterHelper writer) {
        if (typeDiff instanceof ModifiedDs3TypeDiff) {
            printModifiedType(typeDiff.getOldDs3Type(), typeDiff.getNewDs3Type(), writer);
            return;
        }
        if (typeDiff instanceof AddedDs3TypeDiff) {
            printAddedType(typeDiff.getNewDs3Type(), writer);
            return;
        }
        if (typeDiff instanceof DeletedDs3TypeDiff) {
            printDeletedType(typeDiff.getOldDs3Type(), writer);
            return;
        }
        if (typeDiff instanceof NoChangeDs3TypeDiff) {
            //Do not print
            return;
        }
        throw new IllegalArgumentException("Simple printer cannot print the implementation of Ds3TypeDiff: " + typeDiff.getClass());
    }

    //TODO test
    /**
     * Prints a {@link Ds3Type} that exists in the older contract but not in the newer contract
     */
    private static void printDeletedType(final Ds3Type oldType, final WriterHelper writer) {
        writer.append("DELETED TYPE ").append(removePath(oldType.getName())).append("\n");

        printModifiedLine("Name:", removePath(oldType.getName()), "N/A", LABEL_WIDTH, COLUMN_WIDTH, INDENT, writer);
        printDeletedLine("NameToMarshal:", oldType.getNameToMarshal(), LABEL_WIDTH, COLUMN_WIDTH, INDENT, writer);
        printElements(oldType.getElements(), ImmutableList.of(), writer);
        printEnumConstants(oldType.getEnumConstants(), ImmutableList.of(), writer);

        writer.append("\n");
    }

    //TODO test
    /**
     * Prints a {@link Ds3Type} that exists in the newer contract but not in the older contract
     */
    private static void printAddedType(final Ds3Type newType, final WriterHelper writer) {
        writer.append("ADDED TYPE ").append(removePath(newType.getName())).append("\n");

        printModifiedLine("Name:", "N/A", removePath(newType.getName()), LABEL_WIDTH, COLUMN_WIDTH, INDENT, writer);
        printAddedLine("NameToMarshal:", newType.getNameToMarshal(), LABEL_WIDTH, COLUMN_WIDTH, INDENT, writer);
        printElements(ImmutableList.of(), newType.getElements(), writer);
        printEnumConstants(ImmutableList.of(), newType.getEnumConstants(), writer);

        writer.append("\n");
    }

    //TODO test
    /**
     * Prints a {@link Ds3Type} that exists in both contracts but was modified between versions
     */
    private static void printModifiedType(final Ds3Type oldType, final Ds3Type newType, final WriterHelper writer) {
        writer.append("MODIFIED TYPE ").append(removePath(oldType.getName())).append("\n");

        printModifiedLine("Name:", removePath(oldType.getName()), removePath(newType.getName()), LABEL_WIDTH, COLUMN_WIDTH, INDENT, writer);
        printModifiedLine("NameToMarshal:", oldType.getNameToMarshal(), newType.getNameToMarshal(), LABEL_WIDTH, COLUMN_WIDTH, INDENT, writer);
        printElements(oldType.getElements(), newType.getElements(), writer);
        printEnumConstants(oldType.getEnumConstants(), newType.getEnumConstants(), writer);

        writer.append("\n");
    }

    //TODO test
    /**
     * Prints the changes between two {@link ImmutableList} of {@link Ds3Element}. If both lists are
     * empty, then nothing is printed.
     */
    private static void printElements(
            final ImmutableList<Ds3Element> oldElements,
            final ImmutableList<Ds3Element> newElements,
            final WriterHelper writer) {
        if (isEmpty(oldElements) && isEmpty(newElements)) {
            //do not print empty values
            return;
        }
        writer.append(indent(INDENT)).append("Elements:").append("\n");

        final ImmutableSet<String> elementNames = getElementNameUnion(oldElements, newElements);
        final ImmutableMap<String, Ds3Element> oldMap = toElementMap(oldElements);
        final ImmutableMap<String, Ds3Element> newMap = toElementMap(newElements);

        elementNames.forEach(name -> printElementDiff(oldMap.get(name), newMap.get(name), writer));
    }

    //TODO test
    /**
     * Gets the union of names of all params within two {@link ImmutableList} of {@link Ds3Element}
     */
    private static ImmutableSet<String> getElementNameUnion(
            final ImmutableList<Ds3Element> oldElements,
            final ImmutableList<Ds3Element> newElements) {
        final ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        if (hasContent(oldElements)) {
            oldElements.forEach(element -> builder.add(element.getName()));
        }
        if (hasContent(newElements)) {
            newElements.forEach(element -> builder.add(element.getName()));
        }
        return builder.build();
    }

    //TODO test
    /**
     * Converts a {@link ImmutableList} of {@link Ds3Element} into an {@link ImmutableMap} of
     * element names and {@link Ds3Element}
     */
    private static ImmutableMap<String, Ds3Element> toElementMap(final ImmutableList<Ds3Element> elements) {
        if (isEmpty(elements)) {
            return ImmutableMap.of();
        }
        final ImmutableMap.Builder<String, Ds3Element> builder = ImmutableMap.builder();
        elements.forEach(element -> builder.put(element.getName(), element));
        return builder.build();
    }

    //TODO test
    /**
     * Prints the changes between two {@link ImmutableList} of {@link Ds3Element}. If both lists are
     * empty, then nothing is printed.
     */
    private static void printEnumConstants(
            final ImmutableList<Ds3EnumConstant> oldEnums,
            final ImmutableList<Ds3EnumConstant> newEnums,
            final WriterHelper writer) {
        if (isEmpty(oldEnums) && isEmpty(newEnums)) {
            //do not print empty values
            return;
        }
        writer.append(indent(INDENT)).append("EnumConstants:").append("\n");

        final ImmutableSet<String> enumNames = getEnumConstantNameUnion(oldEnums, newEnums);
        final ImmutableMap<String, Ds3EnumConstant> oldMap = toEnumConstantMap(oldEnums);
        final ImmutableMap<String, Ds3EnumConstant> newMap = toEnumConstantMap(newEnums);

        enumNames.forEach(name -> printEnumConstantDiff(oldMap.get(name), newMap.get(name), writer));
    }

    //TODO test
    /**
     * Gets the union of names of all params within two {@link ImmutableList} of {@link Ds3EnumConstant}
     */
    private static ImmutableSet<String> getEnumConstantNameUnion(
            final ImmutableList<Ds3EnumConstant> oldEnums,
            final ImmutableList<Ds3EnumConstant> newEnums) {
        final ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        if (hasContent(oldEnums)) {
            oldEnums.forEach(enumConstant -> builder.add(enumConstant.getName()));
        }
        if (hasContent(newEnums)) {
            newEnums.forEach(enumConstant -> builder.add(enumConstant.getName()));
        }
        return builder.build();
    }

    //TODO test
    /**
     * Converts a {@link ImmutableList} of {@link Ds3EnumConstant} into an {@link ImmutableMap} of
     * enum values (i.e. names) and {@link Ds3EnumConstant}
     */
    private static ImmutableMap<String, Ds3EnumConstant> toEnumConstantMap(final ImmutableList<Ds3EnumConstant> enumConstants) {
        if (isEmpty(enumConstants)) {
            return ImmutableMap.of();
        }
        final ImmutableMap.Builder<String, Ds3EnumConstant> builder = ImmutableMap.builder();
        enumConstants.forEach(enumConstant -> builder.put(enumConstant.getName(), enumConstant));
        return builder.build();
    }
}
