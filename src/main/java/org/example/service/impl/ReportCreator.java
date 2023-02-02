package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXmlExporterOutput;
import org.example.client.JasperClient;
import org.example.enums.ReportTypeEnum;
import org.example.service.ReportDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

@Slf4j
@Service
public class ReportCreator {
    private final JasperClient jasperClient;

    @Autowired
    public ReportCreator(final JasperClient jasperClient) {
        this.jasperClient = jasperClient;
    }

    public <T, D extends ReportDataService<T>> void createReport(final D dataService,
                                                                 final ReportTypeEnum reportType,
                                                                 final T dataObject,
                                                                 final OutputStream resultOutputStream) {
        Map<String, Object> parameterMap = dataService.prepareParameterMap(dataObject);
        JRDataSource iterableData = dataService.prepareIterableData(dataObject);
        InputStream jrprintReport = jasperClient.createReport(parameterMap, iterableData);
        switch (reportType) {
            case PDF -> {
                JRPdfExporter jrPdfExporter = new JRPdfExporter();
                printReport(jrPdfExporter, jrprintReport, resultOutputStream);
            }
            case EXCEL -> {
                JRXlsExporter jrXlsExporter = new JRXlsExporter();
                printReport(jrXlsExporter, jrprintReport, resultOutputStream);
            }
            case HTML -> {
                HtmlExporter htmlExporter = new HtmlExporter();
                printReport(htmlExporter, jrprintReport, resultOutputStream);
            }
            case DOCX -> {
                JRDocxExporter jrDocxExporter = new JRDocxExporter();
                printReport(jrDocxExporter, jrprintReport, resultOutputStream);
            }
        }
    }

    private <E extends JRAbstractExporter> void printReport(final E exporter,
                                                            final InputStream jrprintReport,
                                                            final OutputStream resultOutputStream) {
        try {
            if (exporter instanceof HtmlExporter) {
                exporter.setExporterOutput(new SimpleXmlExporterOutput(resultOutputStream));
            } else {
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(resultOutputStream));
            }
        exporter.setExporterInput(new SimpleExporterInput(jrprintReport));
        exporter.exportReport();
        } catch (JRException e) {
            log.error("Error print jasper report");
            throw new RuntimeException("Error print jasper report", e);
        }
    }
}
