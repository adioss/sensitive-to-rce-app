package com.adioss.hack.controller;

import com.adioss.hack.utils.JwtKeyManager;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.stream.Collectors;

import static com.adioss.hack.utils.Constants.*;
import static java.util.Collections.singletonList;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("display-rest")
public class MainRestController {
    private final JwtKeyManager jwtKeyManager;
    private final RestTemplate restTemplate;

    public MainRestController(JwtKeyManager jwtKeyManager, RestTemplate restTemplate) {
        this.jwtKeyManager = jwtKeyManager;
        this.restTemplate = restTemplate;
    }

    @GetMapping(path = "/gifs", produces = APPLICATION_JSON_UTF8_VALUE)
    public String getGifs(@CookieValue(value = "session") Object session) {
        jwtKeyManager.checkSession(session);
        String result = "";
        result += "[";
        result += GIF_URLS.stream().map(it -> "['" + it + "', '<img src=\"" + it + "\"/>']").collect(Collectors.joining(", "));
        result += "]";
        return result;
    }

    @GetMapping(path = "/lyrics", produces = APPLICATION_JSON_UTF8_VALUE)
    public String getLyrics(@CookieValue(value = "session") Object session) {
        jwtKeyManager.checkSession(session);
        String result = "";
        result += "[";
        result += LYRICS_LINES.stream().map(it -> "[\"" + it + "\"]").collect(Collectors.joining(", "));
        result += "]";
        return result;
    }

    @GetMapping(path = "/addMeme", produces = APPLICATION_JSON_UTF8_VALUE)
    public String addMeme(@CookieValue(value = "session") Object session) {
        byte[] decode = Base64.getDecoder().decode(session.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        String headerValue = "Bearer " + new String(decode);
        headers.add("Authorization", headerValue);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(headers);
        ResponseEntity<String> exchange = restTemplate.exchange(DEFAULT_MICRO_SERVICE_URL + "/rest/collectMeme", HttpMethod.GET, entity, String.class);
        return exchange.getBody();
    }
}
