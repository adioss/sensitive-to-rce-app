package com.adioss.hack.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class KeyTool {
    static {
        Security.addProvider(new BouncyCastleProvider());
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
}
