package com.company;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        String pathRead = new Scanner(System.in).nextLine();
        String pathWrite = new Scanner(System.in).nextLine();
        File[] allFile = main.allFile(pathRead);
        File newFile = main.newFile(pathWrite);
        String data = "";
        for (File file : allFile) {
            String test = main.getStringByFile(file.getAbsolutePath());
            data += test + "\n";
        }

        try {
            FileWriter fw = new FileWriter(newFile);
            fw.write(data);
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public File[] allFile(String path) {
        File dir = new File(path);
        File[] children = dir.listFiles();
        return children;
    }

    public String getStringByFile(String path) {
        BufferedReader br = null;
        String data = "";
        try {
            br = new BufferedReader(new FileReader(path));
            String textInALine;

            while ((textInALine = br.readLine()) != null) {
                data += textInALine + "\n";
                textInALine = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public File newFile(String fileName) {
        File file = null;
        boolean isCreat = false;
        try {
            file = new File(fileName);
            //Ở đây mình tạo file trong ổ D
            isCreat = file.createNewFile();
            if (isCreat)
                System.out.print("Da tao file thanh cong!");
            else
                System.out.print("Tao file that bai");
            //file.delete();
        } catch (Exception e) {
            System.out.print(e);
        }
        return file;
    }
}
