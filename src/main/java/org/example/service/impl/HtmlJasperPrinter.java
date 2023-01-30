package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.service.JasperPrinter;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
@Service
public class HtmlJasperPrinter implements JasperPrinter {
    @Override
    public void printReport(InputStream jrprintTemplate, OutputStream resultOutputStream) {
        //TODO ???
    }
}
