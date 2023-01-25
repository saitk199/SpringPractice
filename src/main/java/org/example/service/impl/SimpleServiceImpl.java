package org.example.service.impl;

import org.example.config.SimpleConfig;
import org.example.service.SimpleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimpleServiceImpl implements SimpleService {
    /**
     * Application configuration
     */
    public final SimpleConfig config;

    @Autowired
    public SimpleServiceImpl(final SimpleConfig config) {
        this.config = config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getApplicationNameWithVersion() {
        return config.getAppName() + config.getNested().getVersion();
    }
}
