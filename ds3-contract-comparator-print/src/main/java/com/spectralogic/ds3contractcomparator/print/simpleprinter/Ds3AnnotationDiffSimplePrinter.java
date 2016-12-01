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
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Annotation;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3AnnotationElement;
import com.spectralogic.ds3contractcomparator.print.utils.WriterHelper;
import org.jetbrains.annotations.Nullable;

import static com.spectralogic.ds3autogen.utils.ConverterUtil.hasContent;
import static com.spectralogic.ds3autogen.utils.ConverterUtil.isEmpty;
import static com.spectralogic.ds3autogen.utils.NormalizingContractNamesUtil.removePath;
import static com.spectralogic.ds3contractcomparator.print.simpleprinter.Ds3AnnotationElementDiffSimplePrinter.printAnnotationElementDiff;
import static com.spectralogic.ds3contractcomparator.print.utils.SimplePrinterUtils.indent;
import static com.spectralogic.ds3contractcomparator.print.utils.SimplePrinterUtils.printModifiedLine;

/**
 * Prints the difference in a {@link Ds3Annotation} between two versions of a contract
 */
final class Ds3AnnotationDiffSimplePrinter {

    private static final int LABEL_WIDTH = 20;
    private static final int COLUMN_WIDTH = 50;
    private static final int INDENT = 4;

    /**
     * Prints the difference in a {@link Ds3Annotation} between two versions of a contract
     */
    static void printAnnotationDiff(
            @Nullable final Ds3Annotation oldAnnotation,
            @Nullable final Ds3Annotation newAnnotation,
            final WriterHelper writer) {
        if (oldAnnotation == null && newAnnotation == null) {
            throw new IllegalArgumentException("Cannot print difference between two null Ds3Annotations");
        }
        if (oldAnnotation == null) {
            printAddedAnnotation(newAnnotation, writer);
            return;
        }
        if (newAnnotation == null) {
            printDeletedAnnotation(oldAnnotation, writer);
            return;
        }
        printModifiedAnnotation(oldAnnotation, newAnnotation, writer);
    }

    //TODO test
    /**
     * Prints a {@link Ds3Annotation} that exists in the newer contract but not in the older contract
     */
    private static void printAddedAnnotation(final Ds3Annotation newAnnotation, final WriterHelper writer) {
        printModifiedLine("Name:", "N/A", removePath(newAnnotation.getName()), LABEL_WIDTH, COLUMN_WIDTH, INDENT, writer);
        printAnnotationElements(ImmutableList.of(), newAnnotation.getDs3AnnotationElements(), writer);
    }

    //TODO test
    /**
     * Prints a {@link Ds3Annotation} that exists in the older contract but not in the newer contract
     */
    private static void printDeletedAnnotation(final Ds3Annotation oldAnnotation, final WriterHelper writer) {
        printModifiedLine("Name:", removePath(oldAnnotation.getName()), "N/A", LABEL_WIDTH, COLUMN_WIDTH, INDENT, writer);
        printAnnotationElements(oldAnnotation.getDs3AnnotationElements(), ImmutableList.of(), writer);
    }

    //TODO test
    /**
     * Prints a {@link Ds3Annotation} that was modified between contract versions
     */
    private static void printModifiedAnnotation(
            final Ds3Annotation oldAnnotation,
            final Ds3Annotation newAnnotation,
            final WriterHelper writer) {
        printModifiedLine("Name:", removePath(oldAnnotation.getName()), removePath(newAnnotation.getName()), LABEL_WIDTH, COLUMN_WIDTH, INDENT, writer);
        printAnnotationElements(oldAnnotation.getDs3AnnotationElements(), newAnnotation.getDs3AnnotationElements(), writer);
    }

    //TODO test
    /**
     * Prints the changes between two {@link ImmutableList} of {@link Ds3AnnotationElement}. If both lists are
     * empty, then nothing is printed.
     */
    private static void printAnnotationElements(
            final ImmutableList<Ds3AnnotationElement> oldElements,
            final ImmutableList<Ds3AnnotationElement> newElements,
            final WriterHelper writer) {
        if (isEmpty(oldElements) && isEmpty(newElements)) {
            //do not print empty values
            return;
        }
        writer.append(indent(INDENT + 1)).append("AnnotationElements:").append("\n");

        final ImmutableSet<String> annotationNames = getAnnotationElementNameUnion(oldElements, newElements);
        final ImmutableMap<String, Ds3AnnotationElement> oldParamMap = toAnnotationElementMap(oldElements);
        final ImmutableMap<String, Ds3AnnotationElement> newParamMap = toAnnotationElementMap(newElements);

        annotationNames.forEach(name -> printAnnotationElementDiff(
                oldParamMap.get(name),
                newParamMap.get(name),
                writer));
    }

    //todo test
    /**
     * Gets the union of names of all params within two {@link ImmutableList} of {@link Ds3AnnotationElement}
     */
    private static ImmutableSet<String> getAnnotationElementNameUnion(
            final ImmutableList<Ds3AnnotationElement> oldElements,
            final ImmutableList<Ds3AnnotationElement> newElements) {
        final ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        if (hasContent(oldElements)) {
            oldElements.forEach(element -> builder.add(element.getName()));
        }
        if (hasContent(newElements)) {
            newElements.forEach(element -> builder.add(element.getName()));
        }
        return builder.build();
    }

    //todo test
    /**
     * Converts an {@link ImmutableList} of {@link Ds3AnnotationElement} into an {@link ImmutableMap} of
     * annotation names and {@link Ds3AnnotationElement}
     */
    private static ImmutableMap<String, Ds3AnnotationElement> toAnnotationElementMap(
            final ImmutableList<Ds3AnnotationElement> elements) {
        if (isEmpty(elements)) {
            return ImmutableMap.of();
        }
        final ImmutableMap.Builder<String, Ds3AnnotationElement> builder = ImmutableMap.builder();
        elements.forEach(element -> builder.put(element.getName(), element));
        return builder.build();
    }
}
