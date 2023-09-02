package com.loistudio.tools;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;

public class JavaCompilerExample {
    public static int compile(String path) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        return compiler.run(null, null, null, path);
    }
    
    public static void compileFolder(String folderPath) {
        try {
            File folder = new File(folderPath);
            if (folder.isDirectory()) {
                for (File file : folder.listFiles()) {
                    if (file.isFile()) {
                        JavaCompilerExample.compile(file.getPath());
                    } else if (file.isDirectory()) {
                        String subFolderPath = folderPath + File.separator + file.getName();
                        JavaCompilerExample.compileFolder(subFolderPath);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}