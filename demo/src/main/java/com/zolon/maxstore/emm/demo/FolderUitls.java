package com.zolon.maxstore.emm.demo;

import java.io.File;

public class FolderUitls {

    public static String listAllNames(String directoryPath) {
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            return "Directory does not exist or is not a folder";
        }

        File[] files = directory.listFiles();

        if (files == null || files.length == 0) {
            return "Directory is empty";
        }

        StringBuilder resultBuilder = new StringBuilder();

        for (File file : files) {
            if (file.isDirectory()) {
                resultBuilder.append("[DIR] ");
            } else {
                resultBuilder.append("[FILE] ");
            }
            resultBuilder.append(file.getName()).append("\n");
        }

        if (resultBuilder.length() > 0) {
            resultBuilder.setLength(resultBuilder.length() - 1);
        }
        return resultBuilder.toString();
    }
}
