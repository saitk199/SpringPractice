package org.example.client.impl;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.example.client.JasperClient;
import org.example.config.JasperConfig;
import org.example.config.SimpleConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class JasperClientImpl implements JasperClient {

    private final JasperCompileManager jasperCompileManager;

    private final JasperFillManager jasperFillManager;

    private final JasperConfig config;

    private final RestTemplate restTemplate;

    @Autowired
    public JasperClientImpl(final SimpleConfig simpleConfig) {
        JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
        this.jasperCompileManager = JasperCompileManager.getInstance(jasperReportsContext);
        this.jasperFillManager = JasperFillManager.getInstance(jasperReportsContext);
        this.config = simpleConfig.getJasper();
        this.restTemplate = new RestTemplate();
    }

    @Override
    public InputStream createReport(final Map<String, Object> parameterMap,
                                    final JRDataSource iterableData) {
        InputStream jrxmlTemplate = downloadJasperTemplate();
        InputStream jasperReportsTemplate = compileReport(jrxmlTemplate);
        return fillReport(jasperReportsTemplate, parameterMap, iterableData);
    }

    private String prepareUrlToSourceFile() {
        return config.getServerUrl() + config.getPrefix() + config.getFolder() + "/SimpleReports1_files/main_jrxml";
    }

    private InputStream downloadJasperTemplate() {
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(config.getUsername(),
                config.getPassword()));
        try {
            return Objects.requireNonNull(
                    restTemplate.exchange(prepareUrlToSourceFile(), HttpMethod.GET, null, Resource.class)
                            .getBody()).getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @param jerxmlTemplate .jrxml
     * @return .jasper
     */
    private InputStream compileReport(final InputStream jerxmlTemplate) {
        try {
            try (ByteArrayOutputStream compiledJasperReport = new ByteArrayOutputStream()) {
                jasperCompileManager.compileToStream(jerxmlTemplate, compiledJasperReport);
                try (InputStream result = new ByteArrayInputStream(compiledJasperReport.toByteArray())) {
                    return result;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (JRException e) {
            log.error("Exception compile file: {}. JRException: {}", jerxmlTemplate, e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @param pathToCompileFile todo
     * @param parameterMap todo
     * @param iterableData todo
     * @return .jrprint
     */
    private InputStream fillReport(final InputStream pathToCompileFile,
                                   final Map<String, Object> parameterMap,
                                   final JRDataSource iterableData) {
        try (ByteArrayOutputStream fillJrprintTemplate = new ByteArrayOutputStream()) {
            jasperFillManager.fillToStream(pathToCompileFile, fillJrprintTemplate, parameterMap);
            try (InputStream result = new ByteArrayInputStream(fillJrprintTemplate.toByteArray())) {
                return result;
            }
        } catch (JRException | IOException e) {
            log.error("Exception fill file: {}. JRException or IOException: {}", pathToCompileFile, e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
