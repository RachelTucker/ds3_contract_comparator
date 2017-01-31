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
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Param;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Request;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3ResponseCode;
import com.spectralogic.ds3contractcomparator.models.request.*;
import com.spectralogic.ds3contractcomparator.print.utils.WriterHelper;

import static com.spectralogic.ds3autogen.utils.ConverterUtil.hasContent;
import static com.spectralogic.ds3autogen.utils.ConverterUtil.isEmpty;
import static com.spectralogic.ds3autogen.utils.NormalizingContractNamesUtil.removePath;
import static com.spectralogic.ds3contractcomparator.print.simpleprinter.Ds3ParamDiffSimplePrinter.printParamDiff;
import static com.spectralogic.ds3contractcomparator.print.simpleprinter.Ds3ResponseDiffSimplePrinter.printResponseCodeDiff;
import static com.spectralogic.ds3contractcomparator.print.utils.PrinterUtils.getParamNameUnion;
import static com.spectralogic.ds3contractcomparator.print.utils.PrinterUtils.toParamMap;
import static com.spectralogic.ds3contractcomparator.print.utils.SimplePrinterUtils.*;

/**
 * Prints the difference in a {@link Ds3Request} between two versions of a contract.
 * Prints a {@link Ds3Request} if it was added, deleted or modified.
 * If the {@link Ds3Request} was not changed between contract versions, it is not printed.
 */
final class Ds3RequestDiffSimplePrinter {

    private static final int INDENT = 1;

    /**
     * Prints the changes in a {@link Ds3RequestDiff} if the request was modified, added or changed.
     * If there was no change, then nothing is printed.
     */
    static void printRequestDiff(final Ds3RequestDiff requestDiff, final WriterHelper writer) {
        if (requestDiff instanceof ModifiedDs3RequestDiff) {
            printModifiedRequest(requestDiff.getOldDs3Request(), requestDiff.getNewDs3Request(), writer);
            return;
        }
        if (requestDiff instanceof AddedDs3RequestDiff) {
            printAddedRequest(requestDiff.getNewDs3Request(), writer);
            return;
        }
        if (requestDiff instanceof DeletedDs3RequestDiff) {
            printDeletedRequest(requestDiff.getOldDs3Request(), writer);
            return;
        }
        if (requestDiff instanceof NoChangeDs3RequestDiff) {
            //Do not print
            return;
        }
        throw new IllegalArgumentException("Simple printer cannot print the implementation of Ds3RequestDiff: " + requestDiff.getClass());
    }

    /**
     * Prints a {@link Ds3Request} that exists in the newer contract but not in the older contract
     */
    private static void printAddedRequest(final Ds3Request newRequest, final WriterHelper writer) {
        writer.append("******************** ADDED REQUEST ").append(removePath(newRequest.getName()))
                .append(" (").append(newRequest.getClassification().toString())
                .append(") ********************\n\n");

        printModifiedLine("RequestName:", "N/A", removePath(newRequest.getName()), INDENT, writer);
        printAddedLine("Classification:", newRequest.getClassification().toString(), INDENT, writer);
        printAddedLine("HttpVerb:", newRequest.getHttpVerb(), INDENT, writer);
        printAddedLine("BucketRequirement:", newRequest.getBucketRequirement(), INDENT, writer);
        printAddedLine("ObjectRequirement:", newRequest.getObjectRequirement(), INDENT, writer);
        printAddedLine("Action:", newRequest.getAction(), INDENT, writer);
        printAddedLine("Resource:", newRequest.getResource(), INDENT, writer);
        printAddedLine("ResourceType:", newRequest.getResourceType(), INDENT, writer);
        printAddedLine("Operation:", newRequest.getOperation(), INDENT, writer);
        printAddedLine("IncludeInPath:", newRequest.getIncludeInPath(), INDENT, writer);
        printRequestParameters("RequiredParameters:", ImmutableList.of(), newRequest.getRequiredQueryParams(), writer);
        printRequestParameters("OptionalParameters:", ImmutableList.of(), newRequest.getOptionalQueryParams(), writer);
        printResponseCodes(ImmutableList.of(), newRequest.getDs3ResponseCodes(), writer);

        writer.append("\n\n");
    }

    /**
     * Prints a {@link Ds3Request} that exists in the older contract but not in the newer contract
     */
    private static void printDeletedRequest(final Ds3Request oldRequest, final WriterHelper writer) {
        writer.append("******************** DELETED REQUEST ").append(removePath(oldRequest.getName()))
                .append(" (").append(oldRequest.getClassification().toString())
                .append(") ********************\n\n");

        printModifiedLine("RequestName:", removePath(oldRequest.getName()), "N/A", INDENT, writer);
        printDeletedLine("Classification:", oldRequest.getClassification().toString(), INDENT, writer);
        printDeletedLine("HttpVerb:", oldRequest.getHttpVerb(), INDENT, writer);
        printDeletedLine("BucketRequirement:", oldRequest.getBucketRequirement(), INDENT, writer);
        printDeletedLine("ObjectRequirement:", oldRequest.getObjectRequirement(), INDENT, writer);
        printDeletedLine("Action:", oldRequest.getAction(), INDENT, writer);
        printDeletedLine("Resource:", oldRequest.getResource(), INDENT, writer);
        printDeletedLine("ResourceType:", oldRequest.getResourceType(), INDENT, writer);
        printDeletedLine("Operation:", oldRequest.getOperation(), INDENT, writer);
        printDeletedLine("IncludeInPath:", oldRequest.getIncludeInPath(), INDENT, writer);
        printRequestParameters("RequiredParameters:", oldRequest.getRequiredQueryParams(), ImmutableList.of(), writer);
        printRequestParameters("OptionalParameters:", oldRequest.getOptionalQueryParams(), ImmutableList.of(), writer);
        printResponseCodes(oldRequest.getDs3ResponseCodes(), ImmutableList.of(), writer);

        writer.append("\n\n");
    }

    /**
     * Prints a {@link Ds3Request} that was modified between the contract versions
     */
    private static void printModifiedRequest(final Ds3Request oldRequest, final Ds3Request newRequest, final WriterHelper writer) {
        writer.append("******************** MODIFIED REQUEST ").append(removePath(oldRequest.getName()))
                .append(" (").append(oldRequest.getClassification().toString())
                .append(") ********************\n\n");

        printModifiedLine("RequestName:", removePath(oldRequest.getName()), removePath(newRequest.getName()), INDENT, writer);
        printModifiedLine("Classification:", oldRequest.getClassification().toString(), newRequest.getClassification().toString(), INDENT, writer);
        printModifiedLine("HttpVerb:", oldRequest.getHttpVerb(), newRequest.getHttpVerb(), INDENT, writer);
        printModifiedLine("BucketRequirement:", oldRequest.getBucketRequirement(), newRequest.getBucketRequirement(), INDENT, writer);
        printModifiedLine("ObjectRequirement:", oldRequest.getObjectRequirement(), newRequest.getObjectRequirement(), INDENT, writer);
        printModifiedLine("Action:", oldRequest.getAction(), newRequest.getAction(), INDENT, writer);
        printModifiedLine("Resource:", oldRequest.getResource(), newRequest.getResource(), INDENT, writer);
        printModifiedLine("ResourceType:", oldRequest.getResourceType(), newRequest.getResourceType(), INDENT, writer);
        printModifiedLine("Operation:", oldRequest.getOperation(), newRequest.getOperation(), INDENT, writer);
        printModifiedLine("IncludeInPath:", oldRequest.getIncludeInPath(), newRequest.getIncludeInPath(), INDENT, writer);
        printRequestParameters("RequiredParameters:", oldRequest.getRequiredQueryParams(), newRequest.getRequiredQueryParams(), writer);
        printRequestParameters("OptionalParameters:", oldRequest.getOptionalQueryParams(), newRequest.getOptionalQueryParams(), writer);
        printResponseCodes(oldRequest.getDs3ResponseCodes(), newRequest.getDs3ResponseCodes(), writer);

        writer.append("\n\n");
    }

    /**
     * Prints the changes between two {@link ImmutableList} of {@link Ds3ResponseCode}. If both lists are
     * empty, then nothing is printed.
     */
    private static void printResponseCodes(
            final ImmutableList<Ds3ResponseCode> oldCodes,
            final ImmutableList<Ds3ResponseCode> newCodes,
            final WriterHelper writer) {
        if (isEmpty(oldCodes) && isEmpty(newCodes)) {
            //do not print empty values
            return;
        }
        writer.append(indent(INDENT)).append("ResponseCodes:").append("\n");

        final ImmutableSet<Integer> codes = getResponseCodeUnion(oldCodes, newCodes);
        final ImmutableMap<Integer, Ds3ResponseCode> oldCodeMap = toCodeMap(oldCodes);
        final ImmutableMap<Integer, Ds3ResponseCode> newCodeMap = toCodeMap(newCodes);

        codes.forEach(code -> printResponseCodeDiff(
                oldCodeMap.get(code),
                newCodeMap.get(code),
                writer));
    }

    /**
     * Gets the union of response codes within two {@link ImmutableList} of {@link Ds3ResponseCode}
     */
    private static ImmutableSet<Integer> getResponseCodeUnion(
            final ImmutableList<Ds3ResponseCode> oldCodes,
            final ImmutableList<Ds3ResponseCode> newCodes) {
        final ImmutableSet.Builder<Integer> builder = ImmutableSet.builder();
        if (hasContent(oldCodes)) {
            oldCodes.forEach(code -> builder.add(code.getCode()));
        }
        if (hasContent(newCodes)) {
            newCodes.forEach(code -> builder.add(code.getCode()));
        }
        return builder.build();
    }

    /**
     * Converts an {@link ImmutableList} of {@link Ds3ResponseCode} into an {@link ImmutableMap} of
     * codes and {@link Ds3ResponseCode}
     */
    private static ImmutableMap<Integer, Ds3ResponseCode> toCodeMap(final ImmutableList<Ds3ResponseCode> codes) {
        if (isEmpty(codes)) {
            return ImmutableMap.of();
        }
        final ImmutableMap.Builder<Integer, Ds3ResponseCode> builder = ImmutableMap.builder();
        codes.forEach(code -> builder.put(code.getCode(), code));
        return builder.build();
    }

    /**
     * Prints the changes between two {@link ImmutableList} of {@link Ds3Param}. If both lists are
     * empty, then nothing is printed.
     * @param label Label describing these parameters, such as RequiredParameters or OptionalParameters
     */
    private static void printRequestParameters(
            final String label,
            final ImmutableList<Ds3Param> oldParams,
            final ImmutableList<Ds3Param> newParams,
            final WriterHelper writer) {
        if (isEmpty(oldParams) && isEmpty(newParams)) {
            //do not print empty values
            return;
        }
        writer.append(indent(INDENT)).append(label).append("\n");

        final ImmutableSet<String> paramNames = getParamNameUnion(oldParams, newParams);
        final ImmutableMap<String, Ds3Param> oldParamMap = toParamMap(oldParams);
        final ImmutableMap<String, Ds3Param> newParamMap = toParamMap(newParams);

        paramNames.forEach(name -> printParamDiff(
                oldParamMap.get(name),
                newParamMap.get(name),
                writer));
    }
}
