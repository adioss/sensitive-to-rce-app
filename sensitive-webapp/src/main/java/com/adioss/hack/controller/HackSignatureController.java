package com.adioss.hack.controller;

import com.adioss.hack.model.SignForm;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.Base64;

import static com.adioss.hack.utils.Constants.DEFAULT_MICRO_SERVICE_URL;

@Controller
public class HackSignatureController {
    private final RestTemplate restTemplate;

    public HackSignatureController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/hackSignature")
    public String getIndex(Model model) {
        model.addAttribute("signForm", new SignForm());
        return "sign";
    }

    @PostMapping(value = "/hackSignature", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ModelAndView postIndex(ModelMap model, SignForm signForm) {
        HttpEntity<String> request = new HttpEntity<>(signForm.getBody() + "|" + signForm.getPublicKey());
        String result = restTemplate.postForObject(DEFAULT_MICRO_SERVICE_URL + "/rest/hackSignature", request, String.class);
        model.addAttribute("contentSigned", result);
        model.addAttribute("contentSignedEncoded", Base64.getEncoder().encodeToString(result.getBytes()));
        return new ModelAndView("sign", "sign", model);
    }
}