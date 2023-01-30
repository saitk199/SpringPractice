package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRDataSource;
import org.example.client.JasperClient;
import org.example.dto.SimpleObject;
import org.example.enums.ReportTypeEnum;
import org.example.service.ReportDataService;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

@Slf4j
@Service
public class ReportCreator {
    private final ReportDataService reportDataService;
    private final JasperClient jasperClient;
    private final PdfJasperPrinter pdfJasperPrinter;
    private final ExcelJasperPrinter excelJasperPrinter;
    private final HtmlJasperPrinter htmlJasperPrinter;

    public ReportCreator(final ReportDataService reportDataService,
                         final JasperClient jasperClient,
                         final PdfJasperPrinter pdfJasperPrinter,
                         final ExcelJasperPrinter excelJasperPrinter,
                         final HtmlJasperPrinter htmlJasperPrinter) {
        this.reportDataService = reportDataService;
        this.jasperClient = jasperClient;
        this.pdfJasperPrinter = pdfJasperPrinter;
        this.excelJasperPrinter = excelJasperPrinter;
        this.htmlJasperPrinter = htmlJasperPrinter;
    }

    public void createReport(final ReportTypeEnum reportType,
                             final SimpleObject simpleObject,
                             final OutputStream resultOutputStream){
        Map<String, Object> parametrMap = reportDataService.prepareParamertMap(simpleObject);
        JRDataSource iterableData = reportDataService.prepareIterableData(simpleObject);
        InputStream jrprintTemplate = jasperClient.createReport(parametrMap, iterableData);
        switch (reportType) {
            case PDF -> pdfJasperPrinter.printReport(jrprintTemplate, resultOutputStream);
            case EXCEL -> excelJasperPrinter.printReport(jrprintTemplate, resultOutputStream);
            case HTML -> htmlJasperPrinter.printReport(jrprintTemplate, resultOutputStream);
        }
    }
}
