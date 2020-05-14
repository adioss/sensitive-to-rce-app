package com.adioss.hack;

public class Constants {
    public static final String DEFAULT_WEBAPP_URL;

    static {
        DEFAULT_WEBAPP_URL = System.getenv("DEFAULT_WEBAPP_URL") != null ? System.getenv("DEFAULT_WEBAPP_URL") : "http://localhost:8080";
    }
}
