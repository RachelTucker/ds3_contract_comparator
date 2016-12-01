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

package com.spectralogic.ds3contractcomparator.cli;

import com.spectralogic.ds3autogen.Ds3SpecParserImpl;
import com.spectralogic.ds3autogen.api.Ds3SpecParser;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3ApiSpec;
import com.spectralogic.ds3contractcomparator.Ds3ApiSpecComparator;
import com.spectralogic.ds3contractcomparator.Ds3ApiSpecComparatorImpl;
import com.spectralogic.ds3contractcomparator.models.Ds3ApiSpecDiff;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

public class Main {
    public static void main(final String[] args) {
        try {
            final Arguments arguments = getArgs(args);
            final Main main = new Main(arguments);

            runMain(main);

        } catch(final Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private static Arguments getArgs(final String[] args) throws Exception {
        try {
            return CLI.getArguments(args);
        } catch (final ParseException e) {
            throw new Exception("Encountered an error when parsing arguments", e);
        }
    }

    private static void runMain(final Main main) throws Exception {
        try {
            main.run();
        } catch(final Exception e) {
            throw new Exception("Encountered an error when generating code", e);
        }
    }

    private final Arguments args;

    private Main(final Arguments args) {
        this.args = args;
    }

    public void run() throws Exception {
        final Ds3SpecParser parser = new Ds3SpecParserImpl();

        System.out.println("Generating comparison for API contract: " + args.getOldApiSpec() + " to " + args.getNewApiSpec());

        final Ds3ApiSpec oldSpec = getSpecFromFileName(args.getOldApiSpec(), parser);
        final Ds3ApiSpec newSpec = getSpecFromFileName(args.getNewApiSpec(), parser);

        final Ds3ApiSpecComparator comparator = new Ds3ApiSpecComparatorImpl();
        final Ds3ApiSpecDiff specDiff = comparator.compare(oldSpec, newSpec);

        //TODO print diff

    }

    private static Ds3ApiSpec getSpecFromFileName(
            final String fileName,
            final Ds3SpecParser parser) throws IOException {
        return parser.getSpec(
                Files.newInputStream(Paths.get(fileName)),
                true);
    }
}