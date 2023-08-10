package com.loistudio.tools;

import java.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.*;

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
    
    public static String encryptTripleASE192(String input, String key) throws Exception {
        byte[] keyBytes = generateKey(key);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        byte[] iv = new byte[64];
        byte[][] dst = new byte[4][16];
        ByteBuffer buffer = ByteBuffer.wrap(iv);
        for(int i=0; i<4; i++) {
            buffer.get(dst[i]); 
        }
        IvParameterSpec ivSpec = new IvParameterSpec(dst[3]);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encryptedBytes = cipher.doFinal(input.getBytes());
        String v1 = Base64.getEncoder().encodeToString(encryptedBytes);   
        ivSpec = new IvParameterSpec(dst[1]);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        encryptedBytes = cipher.doFinal(v1.getBytes());
        String v2 = Base64.getEncoder().encodeToString(encryptedBytes);
        ivSpec = new IvParameterSpec(dst[2]);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        encryptedBytes = cipher.doFinal(v2.getBytes());
        String v3 = Base64.getEncoder().encodeToString(encryptedBytes);
        ivSpec = new IvParameterSpec(dst[0]);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        encryptedBytes = cipher.doFinal(v3.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    
    public static String decryptTripleASE192(String input, String key) throws Exception {
        byte[] keyBytes = generateKey(key);  
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");   
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        byte[] iv = new byte[64];
        byte[][] dst = new byte[4][16];
        ByteBuffer buffer = ByteBuffer.wrap(iv);
        for(int i=0; i<4; i++) {
            buffer.get(dst[i]); 
        }
        IvParameterSpec ivSpec = new IvParameterSpec(dst[0]);  
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] decryptedBytes = Base64.getDecoder().decode(input);        
        byte[] decrypted = cipher.doFinal(decryptedBytes);
        String v1 = new String(decrypted);
        ivSpec = new IvParameterSpec(dst[2]);  
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        decryptedBytes = Base64.getDecoder().decode(v1);        
        decrypted = cipher.doFinal(decryptedBytes);
        String v2 = new String(decrypted);
        ivSpec = new IvParameterSpec(dst[1]);  
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        decryptedBytes = Base64.getDecoder().decode(v2);        
        decrypted = cipher.doFinal(decryptedBytes);
        String v3 = new String(decrypted);
        ivSpec = new IvParameterSpec(dst[3]);  
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        decryptedBytes = Base64.getDecoder().decode(v3);        
        decrypted = cipher.doFinal(decryptedBytes);
        return new String(decrypted);
    }
    
    public static String toHex(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString().trim();
    }
    
    public static String hexToStr(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }
    
    private static byte[] generateKey(String key) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");  
        return digest.digest(key.getBytes());
    }
}