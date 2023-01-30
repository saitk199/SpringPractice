package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.example.service.JasperPrinter;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
@Service
public class ExcelJasperPrinter implements JasperPrinter {
    @Override
    public void printReport(final InputStream jrprintReport,
                            final OutputStream resultOutputStream) {
        try {
            JRXlsExporter exporter = new JRXlsExporter();
            exporter.setExporterInput(new SimpleExporterInput(jrprintReport));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(resultOutputStream));
            exporter.exportReport();
        } catch (JRException e) {
            log.error("Exception print xls file: {}. JRException | IOException: {}", jrprintReport, e.getMessage());
            throw new RuntimeException("", e);
        }
    }
}
