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
        this.privatePath = "key/private.key";
        this.data.put("consts", new HashMap<String, Object>());
        this.data.put("classes", new HashMap<String, Object>());
        this.data.put("array", new HashMap<String, Object>());
    }
    
    public void setKey(String key, String privatePath) {
        this.key = key;
        this.privatePath = privatePath;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public String getKey() {
        return this.key;
    }

    public void open(String path) throws Exception {
<<<<<<< HEAD
        if (path == null || path.trim().isEmpty()) {
=======
        if (path == null || path.trim().equals("")) {
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
            throw new IllegalArgumentException("File path cannot be empty");  
        }
        String paths = this.privatePath;
        int lastIndex = paths.lastIndexOf("/");
        String dir = paths.substring(0, lastIndex);
        FolderExample.makeDirs(dir); 
        File privateFile = new File(this.privatePath);
        DigitalSignature sign = new DigitalSignature();
        if (!privateFile.exists()) {
            FileOutputStream fos = new FileOutputStream(this.privatePath);
            fos.write(sign.initPrivateKey().getEncoded());
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
    
    public void setArray(String arrayName, MclStaticArray array) throws Exception {
        String arrayString = array.toString();
<<<<<<< HEAD
        if (arrayName == null || arrayName.trim().isEmpty()) {
            throw new IllegalArgumentException("Constant name is empty");
        } else if (arrayString == null || arrayString.trim().isEmpty()) {
=======
        if (arrayName == null || arrayName.trim().equals("")) {
            throw new IllegalArgumentException("Constant name is empty");
        } else if (arrayString == null || arrayString.trim().equals("")) {
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
            throw new IllegalArgumentException("Constant value is empty");
        }
        if (!this.data.containsKey("array")) {
            this.data.put("array", new HashMap<String, Object>());
        }
        Map<String, Object> arrays = (Map<String, Object>)this.data.get("array");
        if (!arrays.containsKey(arrayName)) {
            arrays.put(arrayName, new HashMap<String, Object>());
        }
        Map<String, Object> arrayItem = (Map<String, Object>)arrays.get(arrayName);
        arrayItem.put("value", arrayString);
    }

    public void setConst(String constName, String value) throws Exception {
<<<<<<< HEAD
        if (constName == null || constName.trim().isEmpty()) {
            throw new IllegalArgumentException("Constant name is empty");
        } else if (value == null || value.trim().isEmpty()) {
=======
        if (constName == null || constName.trim().equals("")) {
            throw new IllegalArgumentException("Constant name is empty");
        } else if (value == null || value.trim().equals("")) {
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
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
<<<<<<< HEAD
        if (className == null || className.trim().isEmpty()) {
=======
        if (className == null || className.trim().equals("")) {
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
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
<<<<<<< HEAD
        if (className == null || className.trim().isEmpty()) {
            throw new IllegalArgumentException("Class name is empty");
        } else if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Class key is empty");
        } else if (value == null || value.trim().isEmpty()) {
=======
        if (className == null || className.trim().equals("")) {
            throw new IllegalArgumentException("Class name is empty");
        } else if (key == null || key.trim().equals("")) {
            throw new IllegalArgumentException("Class key is empty");
        } else if (value == null || value.trim().equals("")) {
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
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
<<<<<<< HEAD
        if (className == null || className.trim().isEmpty()) {
            throw new IllegalArgumentException("Class name is empty");
        } else if (key == null || key.trim().isEmpty()) {
=======
        if (className == null || className.trim().equals("")) {
            throw new IllegalArgumentException("Class name is empty");
        } else if (key == null || key.trim().equals("")) {
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
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
<<<<<<< HEAD
        if (constName == null || constName.trim().isEmpty()) {
=======
        if (constName == null || constName.trim().equals("")) {
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
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
    
    public MclStaticArray getArray(String arrayName) throws Exception {
        if (arrayName == null || arrayName.trim().equals("")) {
            throw new IllegalArgumentException("Constant name is empty");
        }
        if (this.data.containsKey("array")) {
            Map<String, Object> arrays = (Map<String, Object>)this.data.get("array");
            if (arrays.containsKey(arrayName)) {
                Map<String, Object> arrayItem = (Map<String, Object>)arrays.get(arrayName);
                return new MclStaticArray((String) arrayItem.get("value"));
            }
        }
        return null;
    }

    public void deleteClass(String className) throws Exception {
<<<<<<< HEAD
        if (className == null || className.trim().isEmpty()) {
=======
        if (className == null || className.trim().equals("")) {
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
            throw new IllegalArgumentException("Class name is empty");
        }
        if (this.data.containsKey("classes")) {
            Map<String, Object> classes = (Map<String, Object>)this.data.get("classes");
            classes.remove(className);
        }
    }

    public void deleteKey(String className, String key) throws Exception {
<<<<<<< HEAD
        if (className == null || className.trim().isEmpty()) {
            throw new IllegalArgumentException("Class name is empty");
        } else if (key == null || key.trim().isEmpty()) {
=======
        if (className == null || className.trim().equals("")) {
            throw new IllegalArgumentException("Class name is empty");
        } else if (key == null || key.trim().equals("")) {
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
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
<<<<<<< HEAD
        if (constName == null || constName.trim().isEmpty()) {
=======
        if (constName == null || constName.trim().equals("")) {
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
            throw new IllegalArgumentException("Constant name is empty");
        }
        if (this.data.containsKey("consts")) {
            Map<String, Object> consts = (Map<String, Object>)this.data.get("consts");
            consts.remove(constName);
        }
    }
    
    public void deleteArray(String arrayName) throws Exception {
<<<<<<< HEAD
        if (arrayName == null || arrayName.trim().isEmpty()) {
=======
        if (arrayName == null || arrayName.trim().equals("")) {
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
            throw new IllegalArgumentException("Constant name is empty");
        }
        if (this.data.containsKey("array")) {
            Map<String, Object> arrays = (Map<String, Object>)this.data.get("array");
            arrays.remove(arrayName);
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
                if (this.data.containsKey("array")) {
                    Map<String, Object> arrays = (Map<String, Object>)this.data.get("array");
                    for (String arrayName : arrays.keySet()) {
                        Map<String, Object> arrayItem = (Map<String, Object>)arrays.get(arrayName);
                        output.append("ARRAYLIST ").append(arrayName).append(" ITEMS ").append(arrayItem.get("value")).append("\n");
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
                    if (DigitalSignature.verifySignature(files, singdata, DigitalSignature.privateKeyToPublicKey(DigitalSignature.getPrivateKey(this.privatePath)))) {
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
                            } else if (line.startsWith("ARRAYLIST")) {
                                String[] parts = line.split(" ");
                                String arrayName = parts[1];
                                String value = parts[3];
                                Map<String, Object> arrayItem = new HashMap<>();
                                arrayItem.put("value", value);
                                ((Map<String, Object>)this.data.get("array")).put(arrayName, arrayItem);
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
<<<<<<< HEAD
        return new BigInteger(decodedBytes);
=======
        BigInteger base10 = new BigInteger(decodedBytes);
        return base10;
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
    }
    
    public static String decimalToBase64(BigInteger num) {
        byte[] bytes = num.toByteArray();
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    public BigInteger hexToDecimal(String hex) {
<<<<<<< HEAD
        return new BigInteger(hex, 16);
=======
        BigInteger decimal = new BigInteger(hex, 16);
        return decimal;
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
    }
    
    public String decimalToHex(String decimals) {
        BigInteger decimal = new BigInteger(decimals);
<<<<<<< HEAD
        return decimal.toString(16);
=======
        String hex = decimal.toString(16);
        return hex;
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
    }
}

class DigitalSignatureException extends RuntimeException {
    public DigitalSignatureException(String message) {
        super(message);
    }
}