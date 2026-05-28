package com.anthology.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "google.drive")
public class GoogleDriveConfig {
    private String credentialsPath;
    private String folderId;
}
