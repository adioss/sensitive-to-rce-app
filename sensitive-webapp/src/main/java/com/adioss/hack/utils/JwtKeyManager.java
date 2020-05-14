package com.adioss.hack.utils;

import com.adioss.hack.model.Role;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.adioss.hack.utils.Utils.getFilePath;

@Service
public class JwtKeyManager {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String ISSUER = "sensitive-webapp";
    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    static {
        OBJECT_MAPPER.enableDefaultTyping();
    }

    public JwtKeyManager() throws Exception {
        publicKey = (RSAPublicKey) KeyTool.loadPublicKeyFromPem(getFilePath("public.key"));
        privateKey = (RSAPrivateKey) KeyTool.loadPrivateKeyFromPem(getFilePath("private.key"));
    }

    public String createJWT() {
        UUID userUUID = UUID.randomUUID();
        Role collectMeme = new Role();
        collectMeme.code = "COLLECT_MEME";
        collectMeme.grant = "R+W";
        try {
            collectMeme.source = new URL("http://" + InetAddress.getLocalHost().getHostAddress());
        } catch (MalformedURLException | UnknownHostException e) {
            e.printStackTrace();
        }
        Map<String, Object> claims = new HashMap<>() {{
            try {
                put("role", OBJECT_MAPPER.writeValueAsString(collectMeme));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }};
        return createJWT(userUUID, claims);
    }

    private String createJWT(UUID userUUID, Map<String, Object> claims) {
        String token = null;
        try {
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            JWTCreator.Builder builder = JWT.create().withIssuer(ISSUER).withSubject(userUUID.toString());
            for (String key : claims.keySet()) {
                builder.withClaim(key, (String) claims.get(key));
            }
            token = builder.sign(algorithm);
        } catch (JWTCreationException exception) {
            exception.printStackTrace();
        }
        return token;
    }

    public void checkSession(Object session) {
        Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
        JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
        DecodedJWT jwt = verifier.verify(new String(Base64.getDecoder().decode(session.toString())));
        String id = jwt.getSubject();
        System.out.println("User session checked: '" + id + "'");
    }
}
