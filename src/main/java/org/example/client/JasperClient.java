package org.example.client;

import net.sf.jasperreports.engine.JRDataSource;
import org.example.enums.ReportTypeEnum;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Service
public interface JasperClient {
    void createReport(Map<String, Object> parameterMap,
                      /*JRDataSource iterableData,*/
                      OutputStream resultOutputStream,
                      ReportTypeEnum reportTypeEnum) throws IOException;
}
