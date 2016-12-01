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

import com.spectralogic.ds3autogen.api.models.apispec.Ds3Request;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Type;
import com.spectralogic.ds3contractcomparator.models.Ds3ApiSpecDiff;
import com.spectralogic.ds3contractcomparator.models.request.NoChangeDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.type.NoChangeDs3TypeDiff;
import com.spectralogic.ds3contractcomparator.print.Ds3SpecDiffPrinter;
import com.spectralogic.ds3contractcomparator.print.utils.WriterHelper;

import java.io.Writer;

import static com.spectralogic.ds3contractcomparator.print.simpleprinter.Ds3RequestDiffSimplePrinter.printRequestDiff;
import static com.spectralogic.ds3contractcomparator.print.simpleprinter.Ds3TypeDiffSimplePrinter.printTypeDiff;

/**
 * Simple printer for printing the contents of a {@link Ds3ApiSpecDiff}.
 * Prints {@link Ds3Request} and {@link Ds3Type} that were added, deleted or modified.
 * All items that were not changed between contract versions are not printed.
 */
public class Ds3SpecDiffSimplePrinter implements Ds3SpecDiffPrinter {

    private final WriterHelper writer;
    private final boolean printProperties;
    private final boolean printAllAnnotations;

    public Ds3SpecDiffSimplePrinter(
            final Writer writer,
            final boolean printProperties,
            final boolean printAllAnnotations) {
        this.writer = new WriterHelper(writer);
        this.printProperties = printProperties;
        this.printAllAnnotations = printAllAnnotations;
    }

    /**
     * Prints all {@link Ds3Request} and {@link Ds3Type} that were changed between contract versions
     */
    public void print(final Ds3ApiSpecDiff specDiff) {
        specDiff.getRequests().stream()
                .filter(request -> !(request instanceof NoChangeDs3RequestDiff))
                .forEach(request -> printRequestDiff(request, writer));

        specDiff.getTypes().stream()
                .filter(type -> !(type instanceof NoChangeDs3TypeDiff))
                .forEach(type -> printTypeDiff(type, writer, printProperties, printAllAnnotations));
    }
}
