package com.loistudio.tools;

import java.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class AseEncrypt {
    public static String encryptASE192(String input, String key) throws Exception {
        byte[] keyBytes = generateKey(key);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        byte[] iv = new byte[16];     
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encryptedBytes = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);   
    }
    
    public static String decryptASE192(String input, String key) throws Exception {
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
    
    public static String encryptDES(String data, String key) throws Exception {
        byte[] keyBytes = generateKey(key);
        byte[] desKey = new byte[8];
        System.arraycopy(keyBytes, 0, desKey, 0, 8);
        SecretKey secretKey = new SecretKeySpec(desKey, "DES");
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptData = cipher.doFinal(data.getBytes());
        String base64 = Base64.getEncoder().encodeToString(encryptData);
        return base64;   
    }
        
    public static String decryptDES(String data, String key) throws Exception{
        byte[] keyBytes = generateKey(key);
        byte[] desKey = new byte[8];
        System.arraycopy(keyBytes, 0, desKey, 0, 8);
        SecretKey secretKey = new SecretKeySpec(desKey, "DES");
        Cipher cipher = Cipher.getInstance("DES");     
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] base64Data = Base64.getDecoder().decode(data);
        byte[] decryptData = cipher.doFinal(base64Data);    
        return new String(decryptData);
    }
    
    public static String encryptTripleDES(String input, String key) throws Exception {
        byte[] keyBytes = generateKey(key);
        byte[] desKey = new byte[24];
        System.arraycopy(keyBytes, 0, desKey, 0, 24);
        SecretKey secretKey = new SecretKeySpec(desKey, "DESede");
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    
    public static String decryptTripleDES(String input, String key) throws Exception {
        byte[] keyBytes = generateKey(key);
        byte[] desKey = new byte[24];
        System.arraycopy(keyBytes, 0, desKey, 0, 24);
        SecretKey secretKey = new SecretKeySpec(desKey, "DESede");
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = Base64.getDecoder().decode(input);
        byte[] decrypted = cipher.doFinal(decryptedBytes);
        return new String(decrypted);
    } 
    
    private static byte[] generateKey(String key) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");  
        return digest.digest(key.getBytes());
    }
}