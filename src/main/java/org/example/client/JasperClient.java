package org.example.client;

import net.sf.jasperreports.engine.JRDataSource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

@Service
public interface JasperClient {
    InputStream createReport(Map<String, Object> parameterMap,
                             JRDataSource iterableData);
}
