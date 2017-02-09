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

package com.spectralogic.ds3contractcomparator.cli;

import com.spectralogic.ds3autogen.utils.Guards;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CLI {

    private static final Logger LOG = LoggerFactory.getLogger(CLI.class);

    final private Options options;

    private CLI() {
        this.options = new Options();
        final Option oldSpec = new Option("o", true, "The spec file for the older version of the DS3 API");
        final Option newSpec = new Option("n", true, "The spec file for the newer version of the DS3 API");
        final Option outFile = new Option("d", true, "The file name for the output of the comparison");
        final Option help = new Option("h", false, "Print usage");

        final Option properties = new Option(null, false, "Prints EnumConstant Properties which are excluded by default");
        properties.setLongOpt("properties");

        final Option annotations = new Option(null, false, "Prints all Element Annotations instead of filtering out less used annotations by default");
        annotations.setLongOpt("annotations");

        final Option printer = new Option("p", true, "Specify report printer: " + PrinterType.valuesString());

        options.addOption(oldSpec);
        options.addOption(newSpec);
        options.addOption(outFile);
        options.addOption(help);
        options.addOption(properties);
        options.addOption(annotations);
        options.addOption(printer);
    }

    static Arguments getArguments(final String[] args) throws Exception {
        final CLI cli = new CLI();

        final Arguments arguments = cli.processArgs(args);

        if (arguments.isHelp()) {
            cli.printUsage();
            System.exit(0);
        }

        return arguments;
    }

    @SuppressWarnings("deprecation")
    private Arguments processArgs(final String[] args) throws ParseException {
        final CommandLineParser parser = new BasicParser();
        final CommandLine cmd = parser.parse(options, args);

        final String oldSpec = cmd.getOptionValue("o");
        final String newSpec = cmd.getOptionValue("n");
        final String outFile = cmd.getOptionValue("d");
        final boolean help = cmd.hasOption("h");
        final boolean properties = cmd.hasOption("properties");
        final boolean annotations = cmd.hasOption("annotations");
        final PrinterType printerType = processPrinterType(cmd);

        final Arguments arguments = new Arguments(oldSpec, newSpec, outFile, help, properties, annotations, printerType);

        validateArguments(arguments);

        return arguments;
    }

    private PrinterType processPrinterType(final CommandLine cmd) {
        try {
            return Guards.returnIfNull(cmd.getOptionValue("p").toUpperCase(), PrinterType::valueOf);
        } catch (final NullPointerException e) {
            //Default to HTML printer
            return PrinterType.HTML;
        } catch (final Exception e) {
            LOG.error("Unknown printer selection", e);
            throw new IllegalArgumentException(cmd.getOptionValue("p") + " is not a supported printer type");
        }
    }

    private void validateArguments(final Arguments arguments) throws MissingArgumentException {
        if (arguments.isHelp()) return; //Nothing else to verify
        if (arguments.getOldApiSpec() == null) throw new MissingArgumentException("-o is a required argument");
        if (arguments.getNewApiSpec() == null) throw new MissingArgumentException("-n is a required argument");
        if (arguments.getOutputFile() == null) throw new MissingArgumentException("-d is a required argument");
    }

    private void printUsage() {
        final HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("ds3_contract_comparator", options);
    }
}
