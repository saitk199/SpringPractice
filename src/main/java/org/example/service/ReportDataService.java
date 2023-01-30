package org.example.service;

import net.sf.jasperreports.engine.JRDataSource;
import org.example.dto.SimpleObject;

import java.util.Map;

public interface ReportDataService<T> {
    Map<String, Object> prepareParameterMap(T dataObject);
    JRDataSource prepareIterableData(T dataObject);
}
