package com.loistudio.file;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Base64;
import java.math.BigInteger;

import com.loistudio.tools.AseEncrypt;
import com.loistudio.tools.DigitalSignature;

public class MclStatic {

    private String filename;
    private String className;
    private String key;
    private String publicPath;
    private String privatePath;
    private Map<String, Object> data = new HashMap<>();

    public MclStatic() {
        this.filename = null;
        this.className = null;
        this.key = "LOIStudio";
        this.publicPath = "key/public.key";
        this.privatePath = "key/private.key";
        this.data.put("consts", new HashMap<String, Object>());
        this.data.put("classes", new HashMap<String, Object>());
    }
    
    public void setKey(String key, String publicPath, String privatePath) {
        this.key = key;
        this.publicPath = publicPath;
        this.privatePath = privatePath;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public String getKey() {
        return this.key;
    }

    public void open(String path) throws Exception {
        if (path == null || path.trim().equals("")) {
            throw new IllegalArgumentException("File path cannot be empty");  
        }
        String paths = this.publicPath;
        int lastIndex = paths.lastIndexOf("/");
        String dir = paths.substring(0, lastIndex);
        FolderExample.makeDirs(dir); 
        File privateFile = new File(this.privatePath);
        File publicFile = new File(this.publicPath);
        DigitalSignature sign = new DigitalSignature();
        if (!privateFile.exists()) {
            FileOutputStream fos = new FileOutputStream(this.privatePath);
            fos.write(sign.initPrivateKey().getEncoded());
            fos.close();
        }
        if (!publicFile.exists()) {
            FileOutputStream fos = new FileOutputStream(this.publicPath);
            fos.write(sign.initPublicKey().getEncoded());
            fos.close();
        }
        this.filename = path;
        File file = new File(this.filename);
        if (!file.exists()) {
            PrintWriter writer = new PrintWriter(file);
            writer.close();
        } else {
            this.data = readData();
        }
    }

    public void setConst(String constName, String value) throws Exception {
        if (constName == null || constName.trim().equals("")) {
            throw new IllegalArgumentException("Constant name is empty");
        } else if (value == null || value.trim().equals("")) {
            throw new IllegalArgumentException("Constant value is empty");
        }
        if (!this.data.containsKey("consts")) {
            this.data.put("consts", new HashMap<String, Object>());
        }
        Map<String, Object> consts = (Map<String, Object>)this.data.get("consts");
        if (!consts.containsKey(constName)) {
            consts.put(constName, new HashMap<String, Object>());
        }
        Map<String, Object> constItem = (Map<String, Object>)consts.get(constName);
        constItem.put("value", value);
    }

    public void addClass(String className) throws Exception {
        if (className == null || className.trim().equals("")) {
            throw new IllegalArgumentException("Class name is empty");
        }
        if (!this.data.containsKey("classes")) {
            this.data.put("classes", new HashMap<String, Object>());
        }
        Map<String, Object> classes = (Map<String, Object>)this.data.get("classes");
        if (!classes.containsKey(className)) {
            classes.put(className, new HashMap<String, Object>());
        }
    }

    public void set(String className, String key, String value) throws Exception {
        if (className == null || className.trim().equals("")) {
            throw new IllegalArgumentException("Class name is empty");
        } else if (key == null || key.trim().equals("")) {
            throw new IllegalArgumentException("Class key is empty");
        } else if (value == null || value.trim().equals("")) {
            throw new IllegalArgumentException("Class value is empty");
        }
        if (!this.data.containsKey("classes")) {
            this.data.put("classes", new HashMap<String, Object>());
        }
        Map<String, Object> classes = (Map<String, Object>)this.data.get("classes");
        if (!classes.containsKey(className)) {
            classes.put(className, new HashMap<String, Object>());
        }
        Map<String, Object> classItem = (Map<String, Object>)classes.get(className);
        if (!classItem.containsKey(key)) {
            classItem.put(key, new HashMap<String, Object>());
        }
        Map<String, Object> keyItem = (Map<String, Object>)classItem.get(key);
        keyItem.put("value", value);
    }

    public String get(String className, String key) throws Exception {
        if (className == null || className.trim().equals("")) {
            throw new IllegalArgumentException("Class name is empty");
        } else if (key == null || key.trim().equals("")) {
            throw new IllegalArgumentException("Class key is empty");
        }
        if (this.data.containsKey("classes")) {
            Map<String, Object> classes = (Map<String, Object>)this.data.get("classes");
            if (classes.containsKey(className)) {
                Map<String, Object> classItem = (Map<String, Object>)classes.get(className);
                if (classItem.containsKey(key)) {
                    Map<String, Object> keyItem = (Map<String, Object>)classItem.get(key);
                    return (String)keyItem.get("value");
                }
            }
        }
        return null;
    }

    public String getConst(String constName) throws Exception {
        if (constName == null || constName.trim().equals("")) {
            throw new IllegalArgumentException("Constant name is empty");
        }
        if (this.data.containsKey("consts")) {
            Map<String, Object> consts = (Map<String, Object>)this.data.get("consts");
            if (consts.containsKey(constName)) {
                Map<String, Object> constItem = (Map<String, Object>)consts.get(constName);
                return (String)constItem.get("value");
            }
        }
        return null;
    }

    public void deleteClass(String className) throws Exception {
        if (className == null || className.trim().equals("")) {
            throw new IllegalArgumentException("Class name is empty");
        }
        if (this.data.containsKey("classes")) {
            Map<String, Object> classes = (Map<String, Object>)this.data.get("classes");
            classes.remove(className);
        }
    }

    public void deleteKey(String className, String key) throws Exception {
        if (className == null || className.trim().equals("")) {
            throw new IllegalArgumentException("Class name is empty");
        } else if (key == null || key.trim().equals("")) {
            throw new IllegalArgumentException("Class key is empty");
        }
        if (this.data.containsKey("classes")) {
            Map<String, Object> classes = (Map<String, Object>)this.data.get("classes");
            if (classes.containsKey(className)) {
                Map<String, Object> classItem = (Map<String, Object>)classes.get(className);
                classItem.remove(key);
            }
        }
    }  

    public void deleteConst(String constName) throws Exception {
        if (constName == null || constName.trim().equals("")) {
            throw new IllegalArgumentException("Constant name is empty");
        }
        if (this.data.containsKey("consts")) {
            Map<String, Object> consts = (Map<String, Object>)this.data.get("consts");
            consts.remove(constName);
        }
    }

    public void saveData() throws Exception {
        new Thread(() -> {
            try {
                StringBuilder output = new StringBuilder();
                if (this.data.containsKey("consts")) {
                    Map<String, Object> consts = (Map<String, Object>)this.data.get("consts");
                    for (String constName : consts.keySet()) {
                        Map<String, Object> constItem = (Map<String, Object>)consts.get(constName);
                        output.append("CONST ").append(constName).append(" ITEMS ").append(constItem.get("value")).append("\n");
                    }
                }
                if (this.data.containsKey("classes")) { 
                    Map<String, Object> classes = (Map<String, Object>)this.data.get("classes");
                    for (String className : classes.keySet()) {
                        output.append("CLASS ").append(className).append(" STATIC {\n");
                        Map<String, Object> classItem = (Map<String, Object>)classes.get(className);
                        for (String key : classItem.keySet()) {
                            Map<String, Object> keyItem = (Map<String, Object>)classItem.get(key);
                            output.append("    KEY ").append(key).append(" ITEM ").append(keyItem.get("value")).append("\n");
                        }
                        output.append("}\n");
                    }
                }
                BigInteger file = base64ToDecimal(AseEncrypt.encryptTripleASE192(AseEncrypt.encryptTripleDES(AseEncrypt.toHex(output.toString()), this.key), this.key));
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(this.filename));
                out.writeObject(DigitalSignature.signData(output.toString(), this.privatePath));
                out.writeObject(file);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public Map<String, Object> readData() throws Exception {
        Thread t = new Thread(() -> {
            try {
                File file = new File(this.filename);
                if (file.exists() && file.length() > 0) {
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.filename));
                    byte[] singdata = (byte[]) in.readObject();
                    String files = AseEncrypt.hexToStr(AseEncrypt.decryptTripleDES(AseEncrypt.decryptTripleASE192(decimalToBase64((BigInteger) in.readObject()), this.key), this.key));
                    if (DigitalSignature.verifySignature(files, singdata, this.publicPath)) {
                        Scanner scanner = new Scanner(files);
                        in.close();
                        while (scanner.hasNextLine()) {
                            String line = scanner.nextLine();
                            if (line.startsWith("CONST")) {
                                String[] parts = line.split(" ");
                                String constName = parts[1];
                                String value = parts[3];
                                Map<String, Object> constItem = new HashMap<>();
                                constItem.put("value", value);
                                ((Map<String, Object>)this.data.get("consts")).put(constName, constItem);
                            } else if (line.startsWith("CLASS")) {
                                this.className = line.split(" ")[1];
                                Map<String,Object> classItem = new HashMap<>();  
                                ((Map<String,Object>)this.data.get("classes")).put(className, classItem);
                            } else if (line.startsWith("    KEY")) {
                                String[] parts = line.split(" ");
                                String key = parts[5];
                                String value = parts[7];
                                Map<String, Object> keyItem = new HashMap<>();
                                keyItem.put("value", value);
                                Map<String, Object> classData = (Map<String, Object>)this.data.get("classes");
                                Map<String, Object> classItem = (Map<String, Object>)classData.get(className);
                                classItem.put(key, keyItem);
                                ((Map<String,Object>)this.data.get("classes")).put(className, classItem);
                            }
                        }
                        scanner.close();
                    } else {
                        throw new DigitalSignatureException("Inconsistent digital signatures.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t.start();
        t.join();
        return data;
    }
    
    public void close() throws Exception {
        saveData();
    }

    public String generateMixed(int n) {
        String chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz123456789";
        StringBuilder sb = new StringBuilder(n);
        Random random = new Random(System.nanoTime());
        for(int i = 0; i < n; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
    
    public BigInteger base64ToDecimal(String base64Str) {
        byte[] decodedBytes = Base64.getDecoder().decode(base64Str);
        BigInteger base10 = new BigInteger(decodedBytes);
        return base10;
    }
    
    public static String decimalToBase64(BigInteger num) {
        byte[] bytes = num.toByteArray();
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    public BigInteger hexToDecimal(String hex) {
        BigInteger decimal = new BigInteger(hex, 16);
        return decimal;
    }
    
    public String decimalToHex(String decimals) {
        BigInteger decimal = new BigInteger(decimals);
        String hex = decimal.toString(16);
        return hex;
    }
}

class DigitalSignatureException extends RuntimeException {
    public DigitalSignatureException(String message) {
        super(message);
    }
}