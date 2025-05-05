package web.config;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.io.File;


import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.io.File;

@Validated
@ConfigurationProperties(prefix = "storage", ignoreUnknownFields = false)
public record StorageConfig(@NotEmpty String path) {

    public File resolvePath(String fileName) {
        File root = new File(System.getProperty("user.dir"));
        return new File(root, path + File.separator + fileName);
    }
}