<<<<<<< HEAD
package com.loistudio.file;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.zip.*;

public class FolderExample {
    public static void makeDir(String path) {
        File folder = new File(path);
        folder.mkdir();
    }

    public static void makeDirs(String path) {
        File folder = new File(path);
        folder.mkdirs();
    }
    
    public static String readFile(String path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            StringBuilder content = new StringBuilder();
            while((line = br.readLine()) != null) {
                content.append(line);
                content.append("\n"); 
            }
            br.close();
            return content.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeFile(String fileName, String content) {
        try {
            File file = new File(fileName);
            FileWriter writer = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(content);
            bufferedWriter.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void writeAddFile(String fileName, String content) {
        try {
            File file = new File(fileName);
            FileWriter writer = new FileWriter(fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(content);
            bufferedWriter.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unzip(String zipFilePath, String destDir) {
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);

            for (Enumeration entries = zipFile.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();

                if (entry.isDirectory()) {
                    // 目录则创建目录
                    new File(destDir + "/" + entry.getName()).mkdir();
                    continue;
                }

                // 解压文件内容
                InputStream in = zipFile.getInputStream(entry);
                File output = new File(destDir + "/" + entry.getName());
                output.getParentFile().mkdirs();

                FileOutputStream fout = new FileOutputStream(output);

                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    fout.write(buffer, 0, len);
                }

                in.close();
                fout.close();

            }
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void deleteFolder(String path) {
        try {
            File folder = new File(path);
            if(folder.isDirectory()) {
                for (File file : folder.listFiles()) {
                    if(file.isFile()) { 
                        file.delete();
                    } else if(file.isDirectory()) {
                        deleteFolder(path + "/" + file.getName());
                    }
                }
            }
            folder.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        file.delete();
    }

    public static void copy(String path, String targetPath) {
        try {
            Files.copy(Paths.get(path), Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void copyBinaryFile(InputStream is, String osPath) {
        try {
            OutputStream os = new FileOutputStream(osPath);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
=======
package com.loistudio.file;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.zip.*;

public class FolderExample {
    public static void makeDir(String path) {
        File folder = new File(path);
        folder.mkdir();
    }

    public static void makeDirs(String path) {
        File folder = new File(path);
        folder.mkdirs();
    }
    
    public static String readFile(String path) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;
            StringBuilder content = new StringBuilder();
            while((line = br.readLine()) != null) {
                content.append(line);
                content.append("\n"); 
            }
            br.close();
            return content.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeFile(String fileName, String content) {
        try {
            File file = new File(fileName);
            FileWriter writer = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(content);
            bufferedWriter.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void writeAddFile(String fileName, String content) {
        try {
            File file = new File(fileName);
            FileWriter writer = new FileWriter(fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(content);
            bufferedWriter.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unzip(String zipFilePath, String destDir) {
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);

            for (Enumeration entries = zipFile.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();

                if (entry.isDirectory()) {
                    // 目录则创建目录
                    new File(destDir + "/" + entry.getName()).mkdir();
                    continue;
                }

                // 解压文件内容
                InputStream in = zipFile.getInputStream(entry);
                File output = new File(destDir + "/" + entry.getName());
                output.getParentFile().mkdirs();

                FileOutputStream fout = new FileOutputStream(output);

                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    fout.write(buffer, 0, len);
                }

                in.close();
                fout.close();

            }
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void deleteFolder(String path) {
        try {
            File folder = new File(path);
            if(folder.isDirectory()) {
                for (File file : folder.listFiles()) {
                    if(file.isFile()) { 
                        file.delete();
                    } else if(file.isDirectory()) {
                        deleteFolder(path + "/" + file.getName());
                    }
                }
            }
            folder.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        file.delete();
    }

    public static void copy(String path, String targetPath) {
        try {
            Files.copy(Paths.get(path), Paths.get(targetPath), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void copyBinaryFile(InputStream is, String osPath) {
        try {
            OutputStream os = new FileOutputStream(osPath);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
>>>>>>> 27418a63daf9f27bd58e6546e4a591d083ed45d3
