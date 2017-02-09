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

package com.spectralogic.ds3contractcomparator.print.htmlprinter;

import com.google.common.collect.ImmutableList;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3ApiSpec;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Request;
import com.spectralogic.ds3autogen.api.models.apispec.Ds3Type;
import com.spectralogic.ds3autogen.utils.collections.GuavaCollectors;
import com.spectralogic.ds3contractcomparator.models.Ds3ApiSpecDiff;
import com.spectralogic.ds3contractcomparator.models.request.AbstractDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.request.AddedDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.request.DeletedDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.request.Ds3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.request.ModifiedDs3RequestDiff;
import com.spectralogic.ds3contractcomparator.models.type.AbstractDs3TypeDiff;
import com.spectralogic.ds3contractcomparator.models.type.AddedDs3TypeDiff;
import com.spectralogic.ds3contractcomparator.models.type.DeletedDs3TypeDiff;
import com.spectralogic.ds3contractcomparator.models.type.Ds3TypeDiff;
import com.spectralogic.ds3contractcomparator.models.type.ModifiedDs3TypeDiff;
import com.spectralogic.ds3contractcomparator.print.Ds3SpecDiffPrinter;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.generators.HtmlRequestTableGenerator;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.generators.HtmlTypeTableGenerator;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.HtmlReport;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.Section;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.body.Table;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.index.IndexEntry;
import com.spectralogic.ds3contractcomparator.print.htmlprinter.models.index.IndexSection;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;

import static com.spectralogic.ds3autogen.utils.NormalizingContractNamesUtil.removePath;
import static com.spectralogic.ds3contractcomparator.print.utils.HtmlPrinterUtils.toRequestAnchor;
import static com.spectralogic.ds3contractcomparator.print.utils.HtmlPrinterUtils.toRequestTitle;

/**
 * Generates and prints the HTML report highlighting the differences between
 * two {@link Ds3ApiSpec}.
 */
public class HtmlReportPrinter implements Ds3SpecDiffPrinter {

    private static final Logger LOG = LoggerFactory.getLogger(HtmlReportPrinter.class);

    private final Configuration config = new Configuration(Configuration.VERSION_2_3_23);
    private final Writer writer;
    private final String oldContractName;
    private final String newContractName;

    public HtmlReportPrinter(final Writer writer, final String oldContractName, final String newContractName) {
        this.writer = writer;
        this.oldContractName = oldContractName;
        this.newContractName = newContractName;

        config.setDefaultEncoding("UTF-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        config.setClassForTemplateLoading(HtmlReportPrinter.class, "/tmpls");
    }

    @Override
    public void print(final Ds3ApiSpecDiff specDiff) {
        try {
            generateReport(specDiff, oldContractName, newContractName, config, writer);
        } catch (final Exception e) {
            LOG.error("Unable to print report: " + e.getMessage(), e);
        }
    }

    /**
     * Generates the HTML report from a {@link Ds3ApiSpecDiff}
     */
    private static void generateReport(
            final Ds3ApiSpecDiff specDiff,
            final String oldContractName,
            final String newContractName,
            final Configuration config,
            final Writer writer) throws IOException, TemplateException {

        final HtmlReport report = new HtmlReport(
                oldContractName,
                newContractName,
                toIndex(specDiff),
                toSections(specDiff));

        final Template template = config.getTemplate("html_report.ftl");
        template.process(report, writer);
    }

    /**
     * Creates the models representing the report index
     */
    private static ImmutableList<IndexSection> toIndex(final Ds3ApiSpecDiff specDiff) {
        return ImmutableList.of(
                new IndexSection("Modified Commands", toRequestIndexEntryList(specDiff.getRequests(), ModifiedDs3RequestDiff.class)),
                new IndexSection("Deleted Commands", toRequestIndexEntryList(specDiff.getRequests(), DeletedDs3RequestDiff.class)),
                new IndexSection("Added Commands", toRequestIndexEntryList(specDiff.getRequests(), AddedDs3RequestDiff.class)),
                new IndexSection("Modified Types", toTypeIndexEntryList(specDiff.getTypes(), ModifiedDs3TypeDiff.class)),
                new IndexSection("Deleted Types", toTypeIndexEntryList(specDiff.getTypes(), DeletedDs3TypeDiff.class)),
                new IndexSection("Added Types", toTypeIndexEntryList(specDiff.getTypes(), AddedDs3TypeDiff.class)));
    }

    /**
     * Creates the index table entries representing all {@link Ds3TypeDiff}
     */
    private static ImmutableList<IndexEntry> toTypeIndexEntryList(
            final ImmutableList<AbstractDs3TypeDiff> typeDiffs,
            final Class<? extends AbstractDs3TypeDiff> typeClass) {
        return typeDiffs.stream()
                .filter(t -> t.getClass() == typeClass)
                .map(HtmlReportPrinter::toTypeIndexEntry)
                .collect(GuavaCollectors.immutableList());
    }

    /**
     * Creates the index table entry representing a single {@link Ds3TypeDiff}
     */
    private static IndexEntry toTypeIndexEntry(final AbstractDs3TypeDiff typeDiff) {
        final Ds3Type type;
        if (typeDiff instanceof DeletedDs3TypeDiff) {
            type = typeDiff.getOldDs3Type();
        } else {
            type = typeDiff.getNewDs3Type();
        }
        final String name = removePath(type.getName());
        return new IndexEntry(name, name);
    }

    /**
     * Creates the index table entries for all {@link Ds3RequestDiff}
     */
    private static ImmutableList<IndexEntry> toRequestIndexEntryList(
            final ImmutableList<AbstractDs3RequestDiff> requestDiffs,
            final Class<? extends  AbstractDs3RequestDiff> requestClass) {
        return requestDiffs.stream()
                .filter(r -> r.getClass() == requestClass)
                .map(HtmlReportPrinter::toRequestIndexEntry)
                .collect(GuavaCollectors.immutableList());
    }

    /**
     * Creates the index table entry for a single {@link Ds3RequestDiff}
     */
    private static IndexEntry toRequestIndexEntry(final AbstractDs3RequestDiff requestDiff) {
        final Ds3Request request;
        if (requestDiff instanceof DeletedDs3RequestDiff) {
            request = requestDiff.getOldDs3Request();
        } else {
            request = requestDiff.getNewDs3Request();
        }
        return new IndexEntry(
                toRequestTitle(request.getName(), request.getClassification()),
                toRequestAnchor(request.getName(), request.getClassification()));
    }

    /**
     * Creates the body sections of the report
     */
    private static ImmutableList<Section> toSections(final Ds3ApiSpecDiff specDiff) {
        return ImmutableList.of(
                new Section("Modified Commands", toRequestTableList(specDiff.getRequests(), ModifiedDs3RequestDiff.class)),
                new Section("Deleted Commands", toRequestTableList(specDiff.getRequests(), DeletedDs3RequestDiff.class)),
                new Section("Added Commands", toRequestTableList(specDiff.getRequests(), AddedDs3RequestDiff.class)),
                new Section("Modified Types", toTypeTableList(specDiff.getTypes(), ModifiedDs3TypeDiff.class)),
                new Section("Deleted Types", toTypeTableList(specDiff.getTypes(), DeletedDs3TypeDiff.class)),
                new Section("Added Types", toTypeTableList(specDiff.getTypes(), AddedDs3TypeDiff.class)));
    }

    /**
     * Creates the report tables for the specified type of {@link AbstractDs3RequestDiff}
     */
    private static ImmutableList<Table> toRequestTableList(
            final ImmutableList<AbstractDs3RequestDiff> requestDiffs,
            final Class<? extends  AbstractDs3RequestDiff> requestClass) {
        return requestDiffs.stream()
                .filter(r -> r.getClass() == requestClass)
                .map(HtmlRequestTableGenerator::toRequestTable)
                .collect(GuavaCollectors.immutableList());
    }

    /**
     * Creates the report tables for the specified type of {@link AbstractDs3TypeDiff}
     */
    private static ImmutableList<Table> toTypeTableList(
            final ImmutableList<AbstractDs3TypeDiff> typeDiffs,
            final Class<? extends AbstractDs3TypeDiff> typeClass) {
        return typeDiffs.stream()
                .filter(t -> t.getClass() == typeClass)
                .map(HtmlTypeTableGenerator::toTypeTable)
                .collect(GuavaCollectors.immutableList());
    }
}
