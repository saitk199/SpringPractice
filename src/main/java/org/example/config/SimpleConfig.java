package org.example.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Simple class configuration application
 *
 * @author <a href="mailto:saitek1998chita@gmail.ru">Ostrovskiy L.I.</a>
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("application")
public class SimpleConfig {
    /**
     * Application name
     */
    private String appName;

    /**
     * Nested application properties
     */
    private NestedPropertiesConfig nested;
}
