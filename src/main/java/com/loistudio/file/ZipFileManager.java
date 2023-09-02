package com.loistudio.file;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.inputstream.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;

import org.json.JSONObject;

import com.loistudio.tools.DigitalSignature;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ZipFileManager {
    private ZipFile zipFile;

    public void newZipFile(String path) throws ZipException {
        this.zipFile = new ZipFile(path);
    }

    public void open(String path) throws ZipException {
        this.zipFile = new ZipFile(path);
        if (!this.zipFile.isValidZipFile()) {
            throw new ZipException("Invalid zip file");
        }
    }

    public void addFile(String path) throws ZipException {
        this.zipFile.addFile(path);
    }

    public void addFolder(String path) throws ZipException {
        this.zipFile.addFolder(new File(path));
    }

    public void deleteFile(String path) throws ZipException {
        List<FileHeader> headers = this.zipFile.getFileHeaders();
        for (FileHeader header : headers) {
            if (header.getFileName().equals(Paths.get(path).getFileName().toString())) {
                this.zipFile.removeFile(header);
            }
        }
    }

    public void deleteFolder(String path) throws ZipException {
        List<FileHeader> headers = this.zipFile.getFileHeaders();
        for (FileHeader header : headers) {
            if (header.getFileName().startsWith(path)) {
                this.zipFile.removeFile(header);
            }
        }
    }
    
    public ArrayList<String> list() throws ZipException {
        ArrayList<String> fileList = new ArrayList<>();
        List<FileHeader> fileHeaders = zipFile.getFileHeaders();
        for (FileHeader fileHeader : fileHeaders) {
            fileList.add(fileHeader.getFileName());
        }
        return fileList;
    }
    
    public String getFileContent(String filename) throws ZipException, IOException {
        FileHeader fileHeader = this.get(filename);
        InputStream is = zipFile.getInputStream(fileHeader);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }
    
    public byte[] getFileContentByte(String filename) throws ZipException, IOException {
        FileHeader fileHeader = this.get(filename);
        try (ZipInputStream zipInputStream = zipFile.getInputStream(fileHeader)) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public FileHeader get(String filename) throws ZipException {
        return this.zipFile.getFileHeader(filename);
    }
    
    public void mkdir(String dirname) throws ZipException {
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + dirname);
        file.mkdir();
        this.zipFile.addFolder(file);
        file.delete();
    }
    
    public void sign(String keyPath) throws Exception {
        byte[] privKeyBytes = Files.readAllBytes(Paths.get(keyPath));  
        PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(privKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = kf.generatePrivate(privKeySpec);
        PublicKey publicKey = DigitalSignature.privateKeyToPublicKey(privateKey);
        ArrayList<String> fileList = this.list();
        FolderExample.makeDir("DIGITAL-SIGNATURE");
        FileOutputStream fos = new FileOutputStream("DIGITAL-SIGNATURE/public.key"); 
        fos.write(publicKey.getEncoded());
        fos.close();
        JsonObject json = new JsonObject();
        json.open("DIGITAL-SIGNATURE/SIGN.json");
        DigitalSignature sign = new DigitalSignature();
        for (String fileName : fileList) {
            String content = this.getFileContent(fileName);
            String signData = Base64.getEncoder().encodeToString(sign.signData(content, privateKey));
            json.set(fileName, signData);
        }
        this.addFolder("DIGITAL-SIGNATURE");
        FolderExample.deleteFolder("DIGITAL-SIGNATURE");
    }
    
    public boolean verifySignature() throws Exception {
        JSONObject json = new JSONObject(this.getFileContent("DIGITAL-SIGNATURE/SIGN.json"));
        Set<String> keys = json.keySet();
        for(String key : keys) {
            String content = this.getFileContent(key);
            byte[] sign = Base64.getDecoder().decode(json.getString(key));
            byte[] publicKey = this.getFileContentByte("DIGITAL-SIGNATURE/public.key");
            if (!DigitalSignature.verifySignature(content, sign, publicKey)) {
                return false;
            }
        }
        return true;
    }
}