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

public class Arguments {

    private final String oldApiSpec;
    private final String newApiSpec;
    private final String outputFile;
    private final boolean help;
    private final boolean properties;
    private final boolean annotations;
    private final PrinterType printerType;

    public Arguments(
            final String oldApiSpec,
            final String newApiSpec,
            final String outputFile,
            final boolean help,
            final boolean properties,
            final boolean annotations,
            final PrinterType printerType) {
        this.oldApiSpec = oldApiSpec;
        this.newApiSpec = newApiSpec;
        this.outputFile = outputFile;
        this.help = help;
        this.properties = properties;
        this.annotations = annotations;
        this.printerType = printerType;
    }


    public String getOldApiSpec() {
        return oldApiSpec;
    }

    public String getNewApiSpec() {
        return newApiSpec;
    }

    public boolean isHelp() {
        return help;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public boolean isProperties() {
        return properties;
    }

    public boolean isAnnotations() {
        return annotations;
    }

    public PrinterType getPrinterType() {
        return printerType;
    }
}
