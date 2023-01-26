package org.example.client.impl;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.example.client.JasperClient;
import org.example.config.JasperConfig;
import org.example.config.SimpleConfig;
import org.example.enums.ReportTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JasperClientImpl implements JasperClient {

    private final JasperCompileManager jasperCompileManager;

    private final JasperFillManager jasperFillManager;

    private final JasperExportManager jasperExportManager;

    private final JasperConfig config;

    @Autowired
    public JasperClientImpl(final SimpleConfig simpleConfig) {
        JasperReportsContext jasperReportsContext = DefaultJasperReportsContext.getInstance();
        this.jasperCompileManager = JasperCompileManager.getInstance(jasperReportsContext);
        this.jasperFillManager = JasperFillManager.getInstance(jasperReportsContext);
        this.jasperExportManager = JasperExportManager.getInstance(jasperReportsContext);
        this.config = simpleConfig.getJasper();
    }

    @Override
    public void createReport(final Map<String, Object> parameterMap,
                             //final JRDataSource iterableData,
                             final OutputStream resultOutputStream,
                             final ReportTypeEnum reportTypeEnum) {
        InputStream jrxmlTemplate = downloadJasperSample();
        InputStream jasperReportsTemplate = compileReport(jrxmlTemplate);
        parameterMap.putAll(getParameterMap()); //TODO
        InputStream jrprintTemplate = fillReport(jasperReportsTemplate, parameterMap);
        printReport(jrprintTemplate, resultOutputStream, reportTypeEnum);
    }

    private String prepareUrlToSourceFile() {
        return config.getServerUrl() + "/rest_v2/resources" + config.getFolder() + "/SimpleReports1_files/main_jrxml";
    }

    private InputStream downloadJasperSample() {
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(config.getUsername(),
                config.getPassword());
        CredentialsProvider provider = new BasicCredentialsProvider();
        AuthScope scope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);
        provider.setCredentials(scope, credentials);
        HttpClient httpClient = HttpClientBuilder
                .create()
                .setDefaultCredentialsProvider(provider)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(2, true))
                .build();
        HttpGet httpGet = new HttpGet(prepareUrlToSourceFile());
        httpGet.setHeader(HttpHeaders.ACCEPT, "application/jrxml");
        try {
            HttpResponse response = httpClient.execute(httpGet);
            return response.getEntity().getContent();
        } catch (IOException e) {
            log.error("Exception download file by url: {}. JRException: {}",
                    prepareUrlToSourceFile(), e.getMessage());
            e.printStackTrace();
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
     * @param pathToCompileFile
     * @param parameterMap
     * @return .jrprint
     */
    private InputStream fillReport(final InputStream pathToCompileFile,
                                   final Map<String, Object> parameterMap/*,
                                   final JRDataSource iterableData*/) {
        try (ByteArrayOutputStream fillJrprintTemplate = new ByteArrayOutputStream()){
            jasperFillManager.fillToStream(pathToCompileFile, fillJrprintTemplate, parameterMap);
            try (InputStream result = new ByteArrayInputStream(fillJrprintTemplate.toByteArray())) {
                return result;
            }
        } catch (JRException e) {
            log.error("Exception fill file: {}. JRException: {}", pathToCompileFile, e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("Exception create outputStream: {}. JRException: {}", pathToCompileFile, e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    private void printReport(final InputStream jrprintTemplate,
                             final OutputStream resultOutputStream,
                             final ReportTypeEnum reportTypeEnum) {
        try {
            switch (reportTypeEnum) {
                case PDF -> {
                    jasperExportManager.exportToPdfStream(jrprintTemplate, resultOutputStream);
                    resultOutputStream.flush();
                }
                case EXCEL -> {
                    JRXlsxExporter exporter = new JRXlsxExporter();
                    exporter.setExporterInput(new SimpleExporterInput(jrprintTemplate));
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(resultOutputStream));
                    exporter.exportReport();
                }
                case HTML -> {
                    //jasperExportManager.exportToH(jrprintTemplate, resultOutputStream);
                }
            }
        } catch (JRException e) {
            log.error("Exception print file: {}. JRException: {}", jrprintTemplate, e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> getParameterMap() { //TODO Temporary solution
        Map<String, Object> result = new HashMap<>();
        result.put("value1", "   в верхнем регистре   ");
        result.put("Parameter1", "мамка");
        return result;
    }
}
