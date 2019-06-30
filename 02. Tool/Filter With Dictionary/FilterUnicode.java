
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
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
public class FilterUnicode {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //load từ điển - cho link từ điển vào
        ArrayList<String> dictionary = readDict("F:\\course\\09 - SUMMER2019\\Capstone_\\Tool\\VietnameseDictionary\\VietnameseDictionary\\bin\\Debug\\dict.txt");
        // load tất cả các link từ folder - chỗ này nhập folder nhé
        String folderPath = "F:\\course\\09 - SUMMER2019\\Capstone_\\data TuDA";
        ArrayList<String> allLinkFile = listAllFiles(folderPath);
        FileManager fileManager = new FileManager(allLinkFile, dictionary, folderPath);
        fileManager.export();
    }

    public static class FileManager {

        public ArrayList<MyFile> fileContainer = new ArrayList<>();
        ArrayList<String> dictionary;
        ArrayList<String> charList;
        String folderPath;

        public FileManager(ArrayList<String> allLinkFile, ArrayList<String> dictionary, String folderPath) {
            fileContainer = new ArrayList<>();
            this.dictionary = dictionary;
            this.folderPath = folderPath;
            getCharList();
            getAllMyFile(allLinkFile);
        }

        public void getAllMyFile(ArrayList<String> allFile) {
            for (String item : allFile) {
                MyFile myFile = new MyFile();
                myFile.filePath = item;
                myFile.dictionary = this.dictionary;
                myFile.charList = this.charList;
                myFile.setContents(readData(item));
                myFile.filterFile();
                fileContainer.add(myFile);
            }
        }

        public void getCharList() {
            String spChar = "aáàảạãâấậẫầẩăắằẳẵặbcdđeéèẻẽẹêếềểễệfghiíìỉĩịjklmnoóòõỏỗọôốồổỗộơớờởỡợpqrstuúùủũụưứừửữựvxyýỳỷỹỵ.,?-/: 0123456789zw";
            ArrayList<String> chars = new ArrayList<>();
            for (char c : spChar.toCharArray()) {
                chars.add(String.valueOf(c));
            }
            this.charList = chars;
        }

        public void export() {
            String newFolderPath = folderPath + "\\Result";
            File dir = new File(newFolderPath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            for (MyFile myFile : fileContainer) {
                myFile.exportFile(newFolderPath);
            }
        }
    }

    public static class MyFile {

        public String filePath;
        public ArrayList<String> contents;
        public ArrayList<String> dictionary;
        public ArrayList<String> charList;

        public void exportFile(String FolderPath) {
            String newPath = FolderPath + "\\" + getFileName();
            File file = new File(newPath);
            if (file.exists()) {
                file.delete();
            }
            try {
                file.createNewFile();
                HashSet<String> hSetcontents = new HashSet(contents);
                for (String content : hSetcontents) {
                    content = content.replace("..", ".");
                    content = content.replaceAll("[^\\.|\\?]$", ".");
                    try {
                        Files.write(Paths.get(newPath), (content + "\n").getBytes(), StandardOpenOption.APPEND);
                    } catch (IOException ex) {
                        Logger.getLogger(FilterUnicode.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(FilterUnicode.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        public String getFileName() {
            File f = new File(filePath);
            System.out.println("Export " + f.getName() + "...");
            return f.getName();
        }

        public void setContents(ArrayList<String> contents) {
            this.contents = new ArrayList<>();

            for (String content : contents) {
                if (content.equals("")) {
                    continue;
                }

                content = content.replace("...", ".");
                content = content.replace("..", ".");
                content = content.replace(" .", ".");
                content = content.replace(" ,", ",");
                content = content.replace(" ?", "?");
                content = content.replaceAll("\\.(\\w)", ". $1");
                for (String chars : specChar()) {
                    content = content.replace(chars, " ");
                }
                content = content.replaceAll("\\s+", " ");
                for (char alphabet : content.toCharArray()) {
                    if (!charList.contains(String.valueOf(alphabet))) {
                        continue;
                    }
                }
                this.contents.add(content.toLowerCase());
            }
        }

        public void filterFile() {
            ArrayList<String> newContents = new ArrayList<>();
            boolean blCheck = true;
            for (String line : contents) {
                blCheck = true;
                String[] lineSplit = line.split("\\s+");
                for (String word : lineSplit) {
                    if (word.equals("")) {
                        continue;
                    }
                    if (!checkWord(word)) {
                        blCheck = false;
                        break;
                    }
                }
                if (blCheck) {
                    newContents.add(line);
                }
            }
            this.contents = newContents;
        }

        public boolean checkWord(String word) {
            try {
                Double num = Double.parseDouble(word);
                return true;
            } catch (NumberFormatException e) {

            }
            if (word.charAt(word.length() - 1) == '.' || word.charAt(word.length() - 1) == ',' || word.charAt(word.length() - 1) == '?') {
                String removeCharWord = word.replace(String.valueOf(word.charAt(word.length() - 1)), "");
                if (dictionary.contains(removeCharWord)) {
                    return true;
                }
                return false;
            }

            if (dictionary.contains(word)) {
                return true;
            }
            return false;
        }
    }

    public static ArrayList<String> readDict(String filePath) {
        ArrayList<String> dictionary = new ArrayList<>();
        dictionary = scannerRead(filePath);
        return dictionary;
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
            Logger.getLogger(FilterUnicode.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
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

    public static ArrayList<String> specChar() {
        ArrayList<String> chars = new ArrayList<>();
        chars.add("!");
        chars.add("@");
        chars.add("#");
        chars.add("$");
        chars.add("%");
        chars.add("^");
        chars.add("&");
        chars.add("*");
        chars.add("(");
        chars.add(")");
        chars.add("-");
        chars.add("_");
        chars.add("+");
        chars.add("=");
        chars.add("{");
        chars.add("}");
        chars.add("[");
        chars.add("]");
        chars.add("\\");
        chars.add("|");
        chars.add(":");
        chars.add(";");
        chars.add("\"");
        chars.add("'");
        chars.add("<");
        chars.add(">");
        chars.add("/");
        chars.add("…");
        return chars;

    }
}
