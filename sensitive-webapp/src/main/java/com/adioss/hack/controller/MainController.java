package com.adioss.hack.controller;

import com.adioss.hack.utils.JwtKeyManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController {
    private final JwtKeyManager jwtKeyManager;

    public MainController(JwtKeyManager jwtKeyManager) {
        this.jwtKeyManager = jwtKeyManager;
    }

    @GetMapping({"/display"})
    public String getIndex(HttpServletResponse response, @CookieValue(value = "session", required = true) Object session) throws Exception {
        jwtKeyManager.checkSession(session);
        return "display";
    }
}
