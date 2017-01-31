package com.spectralogic.ds3contractcomparator.print.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Param;

import static com.spectralogic.ds3autogen.utils.ConverterUtil.hasContent;
import static com.spectralogic.ds3autogen.utils.ConverterUtil.isEmpty;

/**
 * Contains utils used by multiple printers
 */
public final class PrinterUtils {

    /**
     * Converts an {@link ImmutableList} of {@link Ds3Param} into an {@link ImmutableMap} of
     * parameter names and {@link Ds3Param}
     */
    public static ImmutableMap<String, Ds3Param> toParamMap(final ImmutableList<Ds3Param> params) {
        if (isEmpty(params)) {
            return ImmutableMap.of();
        }
        final ImmutableMap.Builder<String, Ds3Param> builder = ImmutableMap.builder();
        params.forEach(param -> builder.put(param.getName(), param));
        return builder.build();
    }

    /**
     * Gets the union of names of all params within two {@link ImmutableList} of {@link Ds3Param}
     */
    public static ImmutableSet<String> getParamNameUnion(
            final ImmutableList<Ds3Param> oldParams,
            final ImmutableList<Ds3Param> newParams) {
        final ImmutableSet.Builder<String> builder = ImmutableSet.builder();
        if (hasContent(oldParams)) {
            oldParams.forEach(param -> builder.add(param.getName()));
        }
        if (hasContent(newParams)) {
            newParams.forEach(param -> builder.add(param.getName()));
        }
        return builder.build();
    }
}
