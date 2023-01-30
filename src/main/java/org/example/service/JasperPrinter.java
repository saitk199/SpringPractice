package org.example.service;

import java.io.InputStream;
import java.io.OutputStream;

public interface JasperPrinter {
    void printReport(InputStream jrprintReport, OutputStream resultOutputStream);
}
