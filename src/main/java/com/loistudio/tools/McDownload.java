package com.loistudio.tools;

import java.util.*;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;  
import java.time.format.DateTimeFormatter;

import com.loistudio.file.FolderExample;

public class McDownload {
    private ArrayList<String> urlList = new ArrayList<>();
    private String path = "download/";
    
    public void setPath(String path) {
        this.path = path;
    }

    public void add(String url) {
        this.urlList.add(url);
    }
    
    public void remove(String url) {
        this.urlList.remove(url);
    }
    
    public void start() {
        FolderExample.makeDirs(this.path);
        for (String url : urlList) {
            try {
                URL downloadUrl = new URL(url);
                String domain = downloadUrl.getHost();
                InetAddress addr = InetAddress.getByName(domain);
                String ip = addr.getHostAddress();
                LocalDateTime currentTime = LocalDateTime.now();
                String timestamp = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                System.out.println("---" + timestamp + "--- " + url + "\nResolving " + domain + " (" + domain + ")... " + ip);
                HttpURLConnection conn = (HttpURLConnection)downloadUrl.openConnection();
                System.out.println("Connected to: " + domain + " (" + ip + ")");
                int totalSize = conn.getContentLength();
                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    String fileName = url.substring(url.lastIndexOf("/")+1);
                    String savePath = this.path + fileName;
                    System.out.println("Save to: " + savePath);
                    ProgressBar bar = new ProgressBar(totalSize);
                    InputStream inputStream = conn.getInputStream();
                    FileOutputStream fileOutputStream = new FileOutputStream(savePath);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                        bar.update(len);
                    }
                    inputStream.close();
                    fileOutputStream.close();
                    System.out.println("\n" + url + " downloaded successfully!");
                } else {
                    System.out.println("Failed to download " + url);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}