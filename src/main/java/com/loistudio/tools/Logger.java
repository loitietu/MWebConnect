package com.loistudio.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;  
import java.time.format.DateTimeFormatter;

import com.loistudio.file.FolderExample;

public class Logger {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";  
    private static final String ANSI_RED = "\u001B[31m";   
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    
    private static String logFile = null;
    private static int logLevel = 0;
  
    public static void info(String msg) {
        log(msg, ANSI_BLUE, "INFO");
    }

    public static void error(String msg) {
        log(msg, ANSI_RED, "ERROR"); 
    }

    public static void warning(String msg) {
        log(msg, ANSI_YELLOW, "WARNING");
    }
    
    public static void debug(String msg) {
        log(msg, ANSI_GREEN, "DEBUG");
    }

    public static void log(String path, int level) {
        logFile = path;
        logLevel = level;
    }

    private static void log(String msg, String color, String level) {
        LocalDateTime currentTime = LocalDateTime.now();
        String timestamp = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String logMsg = String.format("%s%s%s [%s%s%s] %s", ANSI_BLUE, timestamp, ANSI_RESET, color, level, ANSI_RESET, msg); 
        String logFileMsg = String.format("%s [%s] %s", timestamp, level, msg);
        System.out.println(logMsg);
        if (logFile != null && getLevelNumber(level) >= logLevel) {
            FolderExample.writeAddFile(logFile, logFileMsg + "\n");
        }
    }

    private static int getLevelNumber(String level) {
        switch (level) {
          case "ERROR":
            return 2;
          case "WARNING": 
            return 1;
          case "INFO":
            return 0;
          case "DEBUG":
            return -1;
          default:
            return 0;
        }
    }
}
