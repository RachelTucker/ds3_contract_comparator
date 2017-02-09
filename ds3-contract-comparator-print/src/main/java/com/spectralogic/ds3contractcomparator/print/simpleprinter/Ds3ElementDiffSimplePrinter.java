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

package com.spectralogic.ds3contractcomparator.print.simpleprinter;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Annotation;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Element;
import com.spectralogic.ds3contractcomparator.print.utils.WriterHelper;
import org.jetbrains.annotations.Nullable;

import static com.spectralogic.ds3autogen.utils.ConverterUtil.hasContent;
import static com.spectralogic.ds3autogen.utils.ConverterUtil.isEmpty;
import static com.spectralogic.ds3autogen.utils.NormalizingContractNamesUtil.removePath;
import static com.spectralogic.ds3contractcomparator.print.simpleprinter.Ds3AnnotationDiffSimplePrinter.printAnnotationDiff;
import static com.spectralogic.ds3contractcomparator.print.utils.SimplePrinterUtils.*;

/**
 * Prints the difference in a {@link Ds3Element} between two versions of a contract
 */
final class Ds3ElementDiffSimplePrinter {

    private static final int INDENT = 2;

    /**
     * Prints the difference in a {@link Ds3Element} between two versions of a contract
     */
    static void printElementDiff(
            @Nullable final Ds3Element oldElement,
            @Nullable final Ds3Element newElement,
            final WriterHelper writer,
            final boolean printAllAnnotations) {
        if (oldElement == null && newElement == null) {
            throw new IllegalArgumentException("Cannot print difference between two null Ds3Elements");
        }
        if (oldElement == null) {
            printAddedElement(newElement, writer, printAllAnnotations);
            return;
        }
        if (newElement == null) {
            printDeletedElement(oldElement, writer, printAllAnnotations);
            return;
        }
        printModifiedElement(oldElement, newElement, writer, printAllAnnotations);
    }

    /**
     * Prints a {@link Ds3Element} that exists in the newer contract but not in the older contract
     */
    private static void printAddedElement(
            final Ds3Element newElement,
            final WriterHelper writer,
            final boolean printAllAnnotations) {
        printModifiedLine("ElementName:", "N/A", removePath(newElement.getName()), INDENT, writer);
        printAddedLine("Type:", removePath(newElement.getType()), INDENT + 1, writer);
        printAddedLine("ComponentType:", removePath(newElement.getComponentType()), INDENT + 1, writer);
        printAddedLine("Nullable:", newElement.getNullable(), INDENT + 1, writer);
        printAnnotations(ImmutableList.of(), newElement.getDs3Annotations(), writer, printAllAnnotations);
    }

    /**
     * Prints a {@link Ds3Element} that exists in the older contract but not in the newer contract
     */
    private static void printDeletedElement(
            final Ds3Element oldElement,
            final WriterHelper writer,
            final boolean printAllAnnotations) {
        printModifiedLine("ElementName:", removePath(oldElement.getName()), "N/A", INDENT, writer);
        printDeletedLine("Type:", removePath(oldElement.getType()), INDENT + 1, writer);
        printDeletedLine("ComponentType:", removePath(oldElement.getComponentType()), INDENT + 1, writer);
        printDeletedLine("Nullable:", oldElement.getNullable(), INDENT + 1, writer);
        printAnnotations(oldElement.getDs3Annotations(), ImmutableList.of(), writer, printAllAnnotations);
    }

    /**
     * Prints a {@link Ds3Element} that was modified between contract versions
     */
    private static void printModifiedElement(
            final Ds3Element oldElement,
            final Ds3Element newElement,
            final WriterHelper writer,
            final boolean printAllAnnotations) {
        printModifiedLine("ElementName:", removePath(oldElement.getName()), removePath(newElement.getName()), INDENT, writer);
        printModifiedLine("Type:", removePath(oldElement.getType()), removePath(newElement.getType()), INDENT + 1, writer);
        printModifiedLine("ComponentType:", removePath(oldElement.getComponentType()), removePath(newElement.getComponentType()), INDENT + 1, writer);
        printModifiedLine("Nullable:", oldElement.getNullable(), newElement.getNullable(), INDENT + 1, writer);
        printAnnotations(oldElement.getDs3Annotations(), newElement.getDs3Annotations(), writer, printAllAnnotations);
    }

    /**
     * Prints the changes between two {@link ImmutableList} of {@link Ds3Annotation}. If both lists are
     * empty, then nothing is printed.
     */
    private static void printAnnotations(
            final ImmutableList<Ds3Annotation> oldAnnotations,
            final ImmutableList<Ds3Annotation> newAnnotations,
            final WriterHelper writer,
            final boolean printAllAnnotations) {
        if (isEmpty(oldAnnotations) && isEmpty(newAnnotations)) {
            //do not print empty values
            return;
        }
        writer.append(indent(INDENT + 1)).append("Annotations:").append("\n");

        final ImmutableSet<String> annotationNames = getAnnotationNameUnion(oldAnnotations, newAnnotations);
        final ImmutableMap<String, Ds3Annotation> oldParamMap = toAnnotationMap(oldAnnotations);
        final ImmutableMap<String, Ds3Annotation> newParamMap = toAnnotationMap(newAnnotations);

        annotationNames.stream()
                .filter((annotationName) -> shouldPrintAnnotation(annotationName, printAllAnnotations))
                .forEach(name -> printAnnotationDiff(
                oldParamMap.get(name),
                newParamMap.get(name),
                writer));
    }

    /**
     * Determines if an annotation should be printed based on filtering of commonly non-relevant annotations
     */
    private static boolean shouldPrintAnnotation(final String annotationName, final boolean printAllAnnotations) {
        return printAllAnnotations ||
                !annotationName.endsWith("SortBy") &&
                !annotationName.endsWith("MustMatchRegularExpression") &&
                !annotationName.endsWith("References") &&
                !annotationName.endsWith("DefaultBooleanValue") &&
                !annotationName.endsWith("DefaultEnumValue") &&
                !annotationName.endsWith("DefaultToCurrentDate") &&
                !annotationName.endsWith("DefaultStringValue") &&
                !annotationName.endsWith("DefaultLongValue") &&
                !annotationName.endsWith("DefaultIntegerValue") &&
                !annotationName.endsWith("DefaultDoubleValue") &&
                !annotationName.endsWith("CascadeDelete");
    }

    /**
     * Gets the union of names of all params within two {@link ImmutableList} of {@link Ds3Annotation}
     */
    private static ImmutableSet<String> getAnnotationNameUnion(
            final ImmutableList<Ds3Annotation> oldAnnotations,
            final ImmutableList<Ds3Annotation> newAnnotations) {
        final ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        if (hasContent(oldAnnotations)) {
            oldAnnotations.forEach(annotation -> builder.add(annotation.getName()));
        }
        if (hasContent(newAnnotations)) {
            newAnnotations.forEach(annotation -> builder.add(annotation.getName()));
        }
        return builder.build();
    }

    /**
     * Converts an {@link ImmutableList} of {@link Ds3Annotation} into an {@link ImmutableMap} of
     * annotation names and {@link Ds3Annotation}
     */
    private static ImmutableMap<String, Ds3Annotation> toAnnotationMap(final ImmutableList<Ds3Annotation> annotations) {
        if (isEmpty(annotations)) {
            return ImmutableMap.of();
        }
        final ImmutableMap.Builder<String, Ds3Annotation> builder = ImmutableMap.builder();
        annotations.forEach(annotation -> builder.put(annotation.getName(), annotation));
        return builder.build();
    }
}
