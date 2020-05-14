package com.adioss.hack.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {
    public static Path getFilePath(String fileName) throws IOException {
        if (System.getenv(fileName) != null) {
            return Paths.get(System.getenv(fileName));
        }
        if (System.getProperty(fileName) != null) {
            return Paths.get(System.getenv(fileName));
        }
        ClassPathResource privateKeyClassPathResource = new ClassPathResource("config/" + fileName, Utils.class.getClassLoader());
        return privateKeyClassPathResource.getFile().toPath();
    }
}
