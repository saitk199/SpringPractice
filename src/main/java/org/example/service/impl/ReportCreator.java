package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRDataSource;
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
    private final PdfJasperPrinter pdfJasperPrinter;
    private final ExcelJasperPrinter excelJasperPrinter;
    private final HtmlJasperPrinter htmlJasperPrinter;

    @Autowired
    public ReportCreator(final JasperClient jasperClient,
                         final PdfJasperPrinter pdfJasperPrinter,
                         final ExcelJasperPrinter excelJasperPrinter,
                         final HtmlJasperPrinter htmlJasperPrinter) {
        this.jasperClient = jasperClient;
        this.pdfJasperPrinter = pdfJasperPrinter;
        this.excelJasperPrinter = excelJasperPrinter;
        this.htmlJasperPrinter = htmlJasperPrinter;
    }

    public <T, D extends ReportDataService<T>> void createReport(final D dataService,
                                                                 final ReportTypeEnum reportType,
                                                                 final T dataObject,
                                                                 final OutputStream resultOutputStream) {
        Map<String, Object> parameterMap = dataService.prepareParameterMap(dataObject);
        JRDataSource iterableData = dataService.prepareIterableData(dataObject);
        InputStream jrprintReport = jasperClient.createReport(parameterMap, iterableData);
        switch (reportType) {
            case PDF -> pdfJasperPrinter.printReport(jrprintReport, resultOutputStream);
            case EXCEL -> excelJasperPrinter.printReport(jrprintReport, resultOutputStream);
            case HTML -> htmlJasperPrinter.printReport(jrprintReport, resultOutputStream);
        }
    }
}
