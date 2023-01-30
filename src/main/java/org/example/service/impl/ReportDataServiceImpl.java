package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRDataSource;
import org.example.dto.SimpleObject;
import org.example.service.ReportDataService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ReportDataServiceImpl implements ReportDataService {
    @Override
    public Map<String, Object> prepareParamertMap(final SimpleObject simpleObject) {
        Map<String, Object> result = new HashMap<>();
        result.put("value1", "   в верхнем регистре   ");
        result.put("Parameter1", "мамка");
        return result;
    }

    @Override
    public JRDataSource prepareIterableData(final SimpleObject simpleObject) {
        return null;
    }
}
