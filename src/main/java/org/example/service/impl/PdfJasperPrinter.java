package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperReportsContext;
import org.example.service.JasperPrinter;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
@Service
public class PdfJasperPrinter implements JasperPrinter {
    private final JasperExportManager jasperExportManager;

    public PdfJasperPrinter() {
        JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
        this.jasperExportManager = JasperExportManager.getInstance(jasperReportsContext);
    }

    @Override
    public void printReport(InputStream jrprintReport, OutputStream resultOutputStream) {
        try {
            jasperExportManager.exportToPdfStream(jrprintReport, resultOutputStream);
        } catch (JRException e) {
            log.error("Exception print pdf file: {}. JRException | IOException: {}", jrprintReport, e.getMessage());
            throw new RuntimeException("", e);
        }
    }
}
