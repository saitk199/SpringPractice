package org.example.client.impl;

import net.sf.jasperreports.engine.JRDataSource;
import org.example.client.JasperClient;

import java.io.OutputStream;
import java.util.Map;

public class JasperClientImpl implements JasperClient {
    @Override
    public void createReport(Map<String, Object> parameterMap, JRDataSource iterableData, OutputStream resultOutputStream) {

    }
}
