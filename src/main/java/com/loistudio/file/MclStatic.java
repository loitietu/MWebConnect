package com.loistudio.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class MclStatic {

    private String filename;
    private String className;

    public MclStatic() {
        this.filename = null;
        this.className = null;
    }

    public void open(String path) throws FileNotFoundException {
        this.filename = path;
        File file = new File(this.filename);
        if (!file.exists()) {
            PrintWriter writer = new PrintWriter(file);
            writer.close();
        }
    }

    public void setConst(String constName, String value) throws FileNotFoundException {
        Map<String, Object> data = readData();
        if (!data.containsKey("consts")) {
            data.put("consts", new HashMap<String, Object>());
        }
        Map<String, Object> consts = (Map<String, Object>)data.get("consts");
        if (!consts.containsKey(constName)) {
            consts.put(constName, new HashMap<String, Object>());
        }
        Map<String, Object> constItem = (Map<String, Object>)consts.get(constName);
        constItem.put("value", value);
        saveData(data);
    }

    public void addClass(String className) throws FileNotFoundException {
        Map<String, Object> data = readData();
        if (!data.containsKey("classes")) {
            data.put("classes", new HashMap<String, Object>());
        }
        Map<String, Object> classes = (Map<String, Object>)data.get("classes");
        if (!classes.containsKey(className)) {
            classes.put(className, new HashMap<String, Object>());
        }
        saveData(data);
    }

    public void set(String className, String key, String value) throws FileNotFoundException {
        Map<String, Object> data = readData();
        if (!data.containsKey("classes")) {
            data.put("classes", new HashMap<String, Object>());
        }
        Map<String, Object> classes = (Map<String, Object>)data.get("classes");
        if (!classes.containsKey(className)) {
            classes.put(className, new HashMap<String, Object>());
        }
        Map<String, Object> classItem = (Map<String, Object>)classes.get(className);
        if (!classItem.containsKey(key)) {
            classItem.put(key, new HashMap<String, Object>());
        }
        Map<String, Object> keyItem = (Map<String, Object>)classItem.get(key);
        keyItem.put("value", value);
        saveData(data);
    }

    public String get(String className, String key) throws FileNotFoundException {
        Map<String, Object> data = readData();
        if (data.containsKey("classes")) {
            Map<String, Object> classes = (Map<String, Object>)data.get("classes");
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

    public String getConst(String constName) throws FileNotFoundException {
        Map<String, Object> data = readData();
        if (data.containsKey("consts")) {
            Map<String, Object> consts = (Map<String, Object>)data.get("consts");
            if (consts.containsKey(constName)) {
                Map<String, Object> constItem = (Map<String, Object>)consts.get(constName);
                return (String)constItem.get("value");
            }
        }
        return null;
    }

    public void deleteClass(String className) throws FileNotFoundException {
        Map<String, Object> data = readData();
        if (data.containsKey("classes")) {
            Map<String, Object> classes = (Map<String, Object>)data.get("classes");
            classes.remove(className);
        }
        saveData(data);
    }

    public void deleteKey(String className, String key) throws FileNotFoundException {
        Map<String, Object> data = readData();
    
        if (data.containsKey("classes")) {
            Map<String, Object> classes = (Map<String, Object>)data.get("classes");
            if (classes.containsKey(className)) {
                Map<String, Object> classItem = (Map<String, Object>)classes.get(className);
                classItem.remove(key);
            }
        }
        saveData(data);
    }  

    public void deleteConst(String constName) throws FileNotFoundException {
        Map<String, Object> data = readData();
        if (data.containsKey("consts")) {
            Map<String, Object> consts = (Map<String, Object>)data.get("consts");
            consts.remove(constName);
        }
        saveData(data);
    }

    public void saveData(Map<String, Object> data) throws FileNotFoundException {
        StringBuilder output = new StringBuilder();
        if (data.containsKey("consts")) {
            Map<String, Object> consts = (Map<String, Object>)data.get("consts");
            for (String constName : consts.keySet()) {
                Map<String, Object> constItem = (Map<String, Object>)consts.get(constName);
                output.append("CONST ").append(constName).append(" ITEMS ").append(constItem.get("value")).append("\n");
            }
        }
        if (data.containsKey("classes")) { 
            Map<String, Object> classes = (Map<String, Object>)data.get("classes");
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
        PrintWriter writer = new PrintWriter(new File(this.filename));
        writer.write(output.toString().getBytes().toString());
        writer.close();
    }

    public Map<String, Object> readData() throws FileNotFoundException {
        Map<String, Object> data = new HashMap<>();
        data.put("consts", new HashMap<String, Object>());
        data.put("classes", new HashMap<String, Object>());
        File file = new File(this.filename);
        if (file.exists()) {
            Scanner scanner = new Scanner(file); 
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("CONST")) {
                    String[] parts = line.split(" ");
                    String constName = parts[1];
                    String value = parts[3];
                    Map<String, Object> constItem = new HashMap<>();
                    constItem.put("value", value);
                    ((Map<String, Object>)data.get("consts")).put(constName, constItem);
                } else if (line.startsWith("CLASS")) {
                    this.className = line.split(" ")[1];
                } else if (line.startsWith("    KEY")) {
                    String[] parts = line.split(" ");
                    String key = parts[1];
                    String value = parts[3];
                    Map<String, Object> keyItem = new HashMap<>();
                    keyItem.put("value", value);
                    Map<String, Object> classData = (Map<String, Object>)data.get("classes");
                    Map<String, Object> classItem = (Map<String, Object>)classData.get(className);
                    classItem.put(key, keyItem);
                }
            }
            scanner.close();
        }
        return data;
    }

    public String generateMixed(int n) {
        String chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";
        StringBuilder sb = new StringBuilder(n);
        Random random = new Random(System.nanoTime());
        for(int i = 0; i < n; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

}