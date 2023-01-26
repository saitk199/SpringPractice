package org.example.web;


import lombok.extern.slf4j.Slf4j;
import org.example.client.JasperClient;
import org.example.enums.ReportTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.HashMap;

import static org.springframework.http.ResponseEntity.ok;


@Slf4j
@RestController
@RequestMapping(value = "/jasper/api/v1")
public class JasperController {
    private final JasperClient jasperClient;

    @Autowired
    public JasperController(final JasperClient jasperClient) {
        this.jasperClient = jasperClient;
    }

    @GetMapping(value = "/report/{reportType}")
    public ResponseEntity<Void> createReport(@PathVariable("reportType") final ReportTypeEnum reportType) {
        String pathResultFile = "C:/reports/simple-report-result";
        switch (reportType) {
            case EXCEL -> {
                pathResultFile += ".xls";
                try (OutputStream outputStream = new FileOutputStream(pathResultFile)) {
                    jasperClient.createReport(new HashMap<>(), outputStream, reportType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            case PDF -> {
                pathResultFile += ".pdf";
                try (OutputStream outputStream = new FileOutputStream(pathResultFile)) {
                    jasperClient.createReport(new HashMap<>(), outputStream, reportType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return ok().build();
    }
}
