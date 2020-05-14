package com.adioss.hack.controller;

import com.adioss.hack.utils.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping
public class FileRestController {
    private static final String JWKS_FILE_NAME = "jwks.json";

    @GetMapping(path = "/.well-known/{fileName}", produces = APPLICATION_JSON_UTF8_VALUE)
    public String getJwksAsJson(@PathVariable String fileName) throws Exception {
        if (JWKS_FILE_NAME.equals(fileName)) {
            return Files.readString(Utils.getFilePath(JWKS_FILE_NAME));
        }
        return "{}";
    }
}
