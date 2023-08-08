package com.loistudio.tools;

import java.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AseEncrypt {
    public static String encrypt(String input, String key) throws Exception {
        byte[] keyBytes = generateKey(key);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        byte[] iv = new byte[16];     
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encryptedBytes = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);   
    }
    
    
    public static String decrypt(String input, String key) throws Exception {
        byte[] keyBytes = generateKey(key);  
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");   
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        byte[] iv = new byte[16];      
        IvParameterSpec ivSpec = new IvParameterSpec(iv);  
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decryptedBytes = Base64.getDecoder().decode(input);        
        byte[] decrypted = cipher.doFinal(decryptedBytes);
        return new String(decrypted);
    }
    
    
    private static byte[] generateKey(String key) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");  
        return digest.digest(key.getBytes());
    }
}