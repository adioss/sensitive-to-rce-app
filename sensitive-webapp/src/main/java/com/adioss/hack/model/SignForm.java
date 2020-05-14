package com.adioss.hack.model;

public class SignForm {
    private String publicKey;
    private String body;

    public SignForm(String publicKey, String body) {
        this.publicKey = publicKey;
        this.body = body;
    }

    public SignForm() {

    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
