package com.loistudio.tools;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.RSAPublicKeySpec;
import java.io.*;
import java.util.Base64;
import java.nio.file.*;

public class DigitalSignature {
    private KeyPair pair;
    
    public DigitalSignature() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4);
            keyGen.initialize(spec);
            this.pair = keyGen.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static PublicKey privateKeyToPublicKey(PrivateKey key) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPrivateCrtKey privk = (RSAPrivateCrtKey)key;
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(privk.getModulus(), privk.getPublicExponent());
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        return publicKey;
    }
    
    public PublicKey initPublicKey() throws Exception {
        return pair.getPublic();
    }
    
    public PrivateKey initPrivateKey() throws Exception {
        return pair.getPrivate();
    }
    
    public boolean verifySignature(String data, byte[] signature, PublicKey publicKey) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(data.getBytes());
        return sig.verify(signature);
    }
    
    public byte[] signData(String data, PrivateKey privateKey) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(privateKey);
        sig.update(data.getBytes());
        byte[] signature = sig.sign();
        return signature; 
    }

    public static byte[] signData(String data, String privateKeyFile) throws Exception {
        byte[] privKeyBytes = Files.readAllBytes(Paths.get(privateKeyFile));  
        PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privKey = kf.generatePrivate(privKeySpec);
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(privKey);
        sig.update(data.getBytes());
        byte[] signature = sig.sign();
        return signature;
    }
    
    public static boolean verifySignature(String data, byte[] signature, String publicKeyFile) throws Exception {
        byte[] publKeyBytes = Files.readAllBytes(Paths.get(publicKeyFile));
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey = kf.generatePublic(publicKeySpec);
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(data.getBytes());
        return sig.verify(signature);
    }
    
    public static boolean verifySignature(String data, byte[] signature, byte[] publKeyBytes) throws Exception {
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey = kf.generatePublic(publicKeySpec);
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(data.getBytes());
        return sig.verify(signature);
    }

    public static void initFile() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4);
        keyGen.initialize(spec);
        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();
        FileOutputStream fos = new FileOutputStream("private.key");
        fos.write(privateKey.getEncoded());
        fos.close();
        fos = new FileOutputStream("public.key"); 
        fos.write(publicKey.getEncoded());
        fos.close();
    }
}