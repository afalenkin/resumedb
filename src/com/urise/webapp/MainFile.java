package com.urise.webapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainFile {
    public static void main(String[] args) {

        String filePath = ".\\.gitignore";
        File gitignore = new File(filePath);
        try {
            System.out.println(gitignore.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException("Error", e);
        }
        File webappDir = new File("D:\\JavaOPS\\BaseJava\\basejava\\src\\com\\urise\\webapp");

        String[] list = webappDir.list();
        if (list != null) {
            for (String name : list) {
                System.out.println(name);
            }
        }

        try (FileInputStream fis = new FileInputStream(filePath)) {
            System.out.println(fis.read());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("--------------------------------\n");

        String researchDirPath = "D:\\JavaOPS\\BaseJava\\basejava";
        researchDir(researchDirPath, true, 0);

    }

    // boolean printDirs - print or don't print directories
    public static void researchDir(String dirPath, boolean printDirs, int recursyLevel) {
        for (int i = 0; i < recursyLevel; i++) {
            System.out.print("\t");
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            return;
        }
        if (dir.isFile()) {
            System.out.println("file: " + dir.getName());
            return;
        }
        if (printDirs) {
            System.out.println("FOLDER:  " + dir.getName());
        }
        File[] content = dir.listFiles();
        if (content == null) {
            return;
        }
        recursyLevel++;
        for (File currentFile : content) {
            researchDir(currentFile.getPath(), printDirs, recursyLevel);
        }

    }
}
