package com.adioss.hack.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyTool {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String concertToString(Key key) {
        StringWriter writer1 = new StringWriter();
        try (JcaPEMWriter writer = new JcaPEMWriter(writer1)) {
            writer.writeObject(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer1.toString();
    }

    public static void writePem(Key key, Path path) {
        try (JcaPEMWriter writer = new JcaPEMWriter(new FileWriter(path.toFile()))) {
            writer.writeObject(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PrivateKey loadPrivateKeyFromPem(Path toFile) throws Exception {
        KeyFactory factory = KeyFactory.getInstance("RSA", "BC");
        PemFile pemFile = new PemFile(toFile.toFile());
        byte[] content = pemFile.getPemObject().getContent();
        PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
        return factory.generatePrivate(privKeySpec);
    }

    public static PublicKey loadPublicKeyFromPem(Path path) throws Exception {
        KeyFactory factory = KeyFactory.getInstance("RSA", "BC");
        PemFile pemFile = new PemFile(path.toFile());
        byte[] content = pemFile.getPemObject().getContent();
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
        return factory.generatePublic(pubKeySpec);
    }

    public static PublicKey loadPublicKeyFromPem(String pemAsString) throws Exception {
        KeyFactory factory = KeyFactory.getInstance("RSA", "BC");
        PemString pemFile = new PemString(pemAsString);
        byte[] content = pemFile.getPemObject().getContent();
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
        return factory.generatePublic(pubKeySpec);
    }

    private static class PemFile {
        private PemObject pemObject;

        public PemFile(File file) throws IOException {
            try (PemReader pemReader = new PemReader(new InputStreamReader(new FileInputStream(file)))) {
                this.pemObject = pemReader.readPemObject();
            }
        }

        public PemObject getPemObject() {
            return pemObject;
        }
    }

    private static class PemString {
        private PemObject pemObject;

        public PemString(String content) throws IOException {
            try (PemReader pemReader = new PemReader(new InputStreamReader(new ByteArrayInputStream(content.getBytes(Charset.defaultCharset()))))) {
                this.pemObject = pemReader.readPemObject();
            }
        }

        public PemObject getPemObject() {
            return pemObject;
        }
    }
}
