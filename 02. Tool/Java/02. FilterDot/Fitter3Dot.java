
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Toan
 */
public class Fitter3Dot {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String folderPath = "05.CustomData/CustomerData";
        ArrayList<String> allLinkFile = listAllFiles(folderPath);
        FileManage fileManage = new FileManage(allLinkFile, folderPath);
        fileManage.export();
    }

    public static class MyFile {

        public ArrayList<String> contents;
        public String filePath;

        public void setContent(ArrayList<String> contents) {
            System.out.println("Process " + getFileName() + "...");
            this.contents = new ArrayList<>();
            for (String content : contents) {
if ( !Character.isUpperCase( content.charAt(0)) && !Character.isDigit(content.charAt(0))) {
                    continue;
                }
		content = content.replaceAll("Ảnh minh họa", " ");
                content = content.replaceAll("\\.\\.\\.", "");
                content = content.replaceAll("\\.\\s*,", ",");
                content = content.replaceAll("\\.\\s*\\?", "?");
                //mr/dr
                content = content.replaceAll("\\.", " . ");
                content = content.replaceAll("\\?", " ? ");
                content = content.replaceAll(",", " , ");
                content = content.replaceAll("(\\d+)\\s*,\\s*(\\d+)", "$1,$2");
                content = content.replaceAll("(\\d+)\\s*\\.\\s*(\\d+)", "$1.$2");
                content = content.replaceAll("(Dr|Ms|Mrs|Mr|mr|dr|ms|mrs)\\s*\\.", "$1.");
                content = content.replaceAll("\\s+", " ");
                content = content.trim();
		//System.out.println(getFileName() + ": " + content);
                this.contents.add(content);
                //số
            }
        }
        public String getFileName() {
            File f = new File(filePath);
            return f.getName();
        }
        public void export(){
            
            File file = new File(getFileName());
            System.out.println("Export " + getFileName() + "...");
            if (file.exists()) {
                file.delete();
            }
            try {
                file.createNewFile();
                for (String content : contents) {
                    Files.write(Paths.get(getFileName()), (content + "\n").getBytes(), StandardOpenOption.APPEND);
                }
                
            } catch (IOException ex) {
                Logger.getLogger(Fitter3Dot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static class FileManage {

        public ArrayList<MyFile> fileContainer;
        public ArrayList<String> allFilePaths;
        public String folderPath;

        public FileManage(ArrayList<String> allFilePaths, String folderPath) {
            this.allFilePaths = allFilePaths;
            this.folderPath = folderPath;
            getAllMyFile(allFilePaths);
        }

        public void getAllMyFile(ArrayList<String> allFilePaths) {
            fileContainer = new ArrayList<>();
            for (String filePath : allFilePaths) {
                MyFile myFile = new MyFile();
                myFile.filePath = filePath;
                myFile.setContent(readData(filePath));
                fileContainer.add(myFile);
            }
        }
        public void export(){
            for (MyFile myFile : fileContainer) {
                myFile.export();
            }
        }

    }

    public static ArrayList<String> listAllFiles(String fileFolder) {
        File folder = new File(fileFolder);

        File[] fileNames = folder.listFiles();
        ArrayList<String> allFiles = new ArrayList<>();
        for (File file : fileNames) {
            // if directory call the same method again
            if (file.isDirectory()) {
                listAllFiles(file.getAbsolutePath());
            } else if (file.getName().endsWith(".txt")) {
                allFiles.add(file.getAbsolutePath());
                System.out.println(file.getAbsolutePath());
            }
        }
        return allFiles;
    }

    public static ArrayList<String> readData(String filePath) {
        ArrayList<String> data = new ArrayList<>();
        data = scannerRead(filePath);
        return data;
    }

    public static ArrayList<String> scannerRead(String filePath) {
        ArrayList<String> data = new ArrayList<>();
        try {
            data = (ArrayList<String>) Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Logger.getLogger(Fitter3Dot.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

}
