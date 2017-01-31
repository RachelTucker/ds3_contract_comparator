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

package com.spectralogic.ds3contractcomparator.print.htmlprinter;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Param;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Request;
import com.spectralogic.ds3autogen.api.models.enums.Classification;
import com.spectralogic.ds3contractcomparator.print.utils.WriterHelper;

import static com.spectralogic.ds3autogen.utils.ConverterUtil.isEmpty;
import static com.spectralogic.ds3autogen.utils.Helper.hasContent;
import static com.spectralogic.ds3autogen.utils.NormalizingContractNamesUtil.removePath;
import static com.spectralogic.ds3contractcomparator.print.utils.HtmlPrinterUtils.*;
import static com.spectralogic.ds3contractcomparator.print.utils.PrinterUtils.getParamNameUnion;
import static com.spectralogic.ds3contractcomparator.print.utils.PrinterUtils.toParamMap;

/**
 * Prints the difference in a {@link Ds3Request} between two versions of a contract in HTML format.
 * Prints a {@link Ds3Request} if it was added, deleted or modified.
 * If the {@link Ds3Request} was not changed between contract versions, it is not printed.
 */
public final class Ds3RequestDiffHtmlPrinter {

    private final static int INDENT = 0;

    /**
     * Prints a {@link Ds3Request} that exists in the newer contract but not in the older contract
     */
    public static void printAddedRequest(final Ds3Request newRequest, final WriterHelper writer) {
        writer.append(createRequestBeginning(newRequest.getName(), newRequest.getClassification()));
        writer.append(createAddedEntry("RequestName", removePath(newRequest.getName()), INDENT));
        writer.append(createAddedEntry("Classification", newRequest.getClassification(), INDENT));
        writer.append(createAddedEntry("HttpVerb", newRequest.getHttpVerb(), INDENT));
        writer.append(createAddedEntry("BucketRequirement", newRequest.getBucketRequirement(), INDENT));
        writer.append(createAddedEntry("ObjectRequirement", newRequest.getObjectRequirement(), INDENT));
        writer.append(createAddedEntry("Action", newRequest.getAction(), INDENT));
        writer.append(createAddedEntry("Resource", newRequest.getResource(), INDENT));
        writer.append(createAddedEntry("ResourceType", newRequest.getResourceType(), INDENT));
        writer.append(createAddedEntry("Operation", newRequest.getOperation(), INDENT));
        writer.append(createAddedEntry("IncludeInPath", newRequest.getIncludeInPath(), INDENT));
        writer.append("</table>\n");
    }

    /**
     * Prints a {@link Ds3Request} that exists in the older contract but not in the newer contract
     */
    public static void printDeletedRequest(final Ds3Request oldRequest, final WriterHelper writer) {
        writer.append(createRequestBeginning(oldRequest.getName(), oldRequest.getClassification()));
        writer.append(createDeletedEntry("RequestName", removePath(oldRequest.getName()), INDENT));
        writer.append(createDeletedEntry("Classification", oldRequest.getClassification(), INDENT));
        writer.append(createDeletedEntry("HttpVerb", oldRequest.getHttpVerb(), INDENT));
        writer.append(createDeletedEntry("BucketRequirement", oldRequest.getBucketRequirement(), INDENT));
        writer.append(createDeletedEntry("ObjectRequirement", oldRequest.getObjectRequirement(), INDENT));
        writer.append(createDeletedEntry("Action", oldRequest.getAction(), INDENT));
        writer.append(createDeletedEntry("Resource", oldRequest.getResource(), INDENT));
        writer.append(createDeletedEntry("ResourceType", oldRequest.getResourceType(), INDENT));
        writer.append(createDeletedEntry("Operation", oldRequest.getOperation(), INDENT));
        writer.append(createDeletedEntry("IncludeInPath", oldRequest.getIncludeInPath(), INDENT));

        writer.append("</table>\n");
    }

    /**
     * Prints a {@link Ds3Request} that was modified between the contract versions
     */
    public static void printModifiedRequest(final Ds3Request oldRequest, final Ds3Request newRequest, final WriterHelper writer) {
        writer.append(createRequestBeginning(oldRequest.getName(), oldRequest.getClassification()));
        writer.append(createModifiedEntry("RequestName", removePath(oldRequest.getName()), removePath(newRequest.getName()), INDENT));
        writer.append(createModifiedEntry("Classification", oldRequest.getClassification(), newRequest.getClassification(), INDENT));
        writer.append(createModifiedEntry("HttpVerb", oldRequest.getHttpVerb(), newRequest.getHttpVerb(), INDENT));
        writer.append(createModifiedEntry("BucketRequirement", oldRequest.getBucketRequirement(), newRequest.getBucketRequirement(), INDENT));
        writer.append(createModifiedEntry("ObjectRequirement", oldRequest.getObjectRequirement(), newRequest.getObjectRequirement(), INDENT));
        writer.append(createModifiedEntry("Action", oldRequest.getAction(), newRequest.getAction(), INDENT));
        writer.append(createModifiedEntry("Resource", oldRequest.getResource(), newRequest.getResource(), INDENT));
        writer.append(createModifiedEntry("ResourceType", oldRequest.getResourceType(), newRequest.getResourceType(), INDENT));
        writer.append(createModifiedEntry("Operation", oldRequest.getOperation(), newRequest.getOperation(), INDENT));
        writer.append(createModifiedEntry("IncludeInPath", oldRequest.getIncludeInPath(), newRequest.getIncludeInPath(), INDENT));
        writer.append("</table>\n");
    }

    private static String createRequestBeginning(final String requestName, final Classification classification) {
        final String name = removePath(requestName);
        return "<h3 id=\"" + createRequestAnchor(name, classification) + "\">" + name
                + " (" + classification.toString() + ")</h3>\n"
                + "<table>";
    }
}
