package org.example.service;

import net.sf.jasperreports.engine.JRDataSource;
import org.example.dto.SimpleObject;

import java.util.Map;

public interface ReportDataService {
    Map<String, Object> prepareParamertMap(SimpleObject simpleObject);
    JRDataSource prepareIterableData(SimpleObject simpleObject);
}
