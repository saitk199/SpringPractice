package org.example.web;


import lombok.extern.slf4j.Slf4j;
import org.example.dto.SimpleObject;
import org.example.enums.ReportTypeEnum;
import org.example.service.impl.ReportCreator;
import org.example.service.impl.ReportDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static org.springframework.http.ResponseEntity.ok;


@Slf4j
@RestController
@RequestMapping(value = "/jasper/api/v1")
public class JasperController {
    private final ReportCreator jasperCreator;

    private final ReportDataServiceImpl reportDataService;

    @Autowired
    public JasperController(final ReportCreator jasperCreator,
                            final ReportDataServiceImpl reportDataService) {
        this.jasperCreator = jasperCreator;
        this.reportDataService = reportDataService;
    }

    @GetMapping(value = "/report/{reportType}")
    public ResponseEntity<Void> createReport(@PathVariable("reportType") final ReportTypeEnum reportType) {
        String pathResultFile = "C:/reports/simple-report-result" + "." + reportType.getFileType();
        try (OutputStream outputStream = new FileOutputStream(pathResultFile)) {
            jasperCreator.createReport(reportDataService, reportType, new SimpleObject(), outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ok().build();
    }
}
