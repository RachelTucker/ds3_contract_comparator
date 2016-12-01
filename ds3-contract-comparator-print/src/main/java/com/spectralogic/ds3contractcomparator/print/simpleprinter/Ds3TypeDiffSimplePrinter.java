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

import com.spectralogic.ds3autogen.api.models.apispec.Ds3Type;
import com.spectralogic.ds3contractcomparator.models.type.*;
import com.spectralogic.ds3contractcomparator.print.utils.WriterHelper;

/**
 * Prints the difference in a {@link Ds3Type} between two versions of a contract.
 * Prints a {@link Ds3Type} if it was added, deleted or modified.
 * If the {@link Ds3Type} was not changed between contract versions, it is not printed.
 */
public class Ds3TypeDiffSimplePrinter {

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

    private static void printDeletedType(final Ds3Type oldType, final WriterHelper writer) {
        //todo
    }

    private static void printAddedType(final Ds3Type newType, final WriterHelper writer) {
        //todo
    }

    private static void printModifiedType(final Ds3Type oldType, final Ds3Type newType, final WriterHelper writerHelper) {
        //todo
    }
}
