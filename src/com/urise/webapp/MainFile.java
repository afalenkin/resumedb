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
        researchDir(researchDirPath, true);

    }

    // boolean printDirs - print or don't print directories
    public static void researchDir(String dirPath, boolean printDirs) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            return;
        }
        if (dir.isFile()) {
            System.out.println(dir.getName());
            return;
        }
        if (printDirs) {
            System.out.println("FOLDER:  " + dir.getName());
        }
        for (File currentFile : dir.listFiles()
        ) {
            researchDir(currentFile.getAbsolutePath(), printDirs);
        }

    }
}
