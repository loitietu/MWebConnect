package com.loistudio.tools;

import java.util.*;

public class ProgressBar {

    private int totalSize;
    private int downloaded;
    private int lastPrintLen;

    public ProgressBar(int totalSize) {
        this.totalSize = totalSize;
    }

    public synchronized void update(int newDownloaded) {
        downloaded += newDownloaded;
        printProgress();
    }

    private void printProgress() {
        int completePercent = (int)(downloaded * 100.0 / totalSize);
        int completeLen = completePercent * 50 / 100;

        String progressBar = 
          "=" + String.join("", Collections.nCopies(completeLen, "=")) +
          ">" + String.join("", Collections.nCopies(50 - completeLen, " "));

        System.out.print("\r" + progressBar + " " + completePercent + "%");

        if(completeLen > lastPrintLen) {
          lastPrintLen = completeLen;
          System.out.flush();
        }
    }
}