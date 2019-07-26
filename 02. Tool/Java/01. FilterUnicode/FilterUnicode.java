
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        ArrayList<String> dictionary = readDict("dict.txt");
        // load tất cả các link từ folder - chỗ này nhập folder nhé
       //String folderPath = "F:\\course\\09 - SUMMER2019\\Capstone_\\data TuDA\\DataTest"; //03. SortByCategory
        String folderPath = "F:\\course\\09 - SUMMER2019\\Capstone\\trunk\\02. Tool\\Java\\data";
        ArrayList<String> allLinkFile = listAllFiles(folderPath);
        FileManager fileManager = new FileManager(allLinkFile, dictionary, folderPath);
        fileManager.export();
    }

    public static class FileManager {

        public ArrayList<MyFile> fileContainer = new ArrayList<>();
        ArrayList<String> dictionary;
        String folderPath;

        public FileManager(ArrayList<String> allLinkFile, ArrayList<String> dictionary, String folderPath) {
            fileContainer = new ArrayList<>();
            this.dictionary = dictionary;
            this.folderPath = folderPath;
            //getCharList();
            getAllMyFile(allLinkFile);
        }

        public void getAllMyFile(ArrayList<String> allFile) {
            for (String item : allFile) {
                MyFile myFile = new MyFile();
                myFile.filePath = item;
                myFile.dictionary = this.dictionary;
                myFile.setContents(readData(item));
                myFile.filterFile();
                fileContainer.add(myFile);
            }
        }

//        public void getCharList() {
//            String spChar = "áàảạãâấậẫầẩăắằẳẵặđéèẻẽẹêếềểễệíìỉĩịóòõỏỗọốồổỗộớờởỡợúùủũụứừửữựýỳỷỹỵ";
//            ArrayList<String> chars = new ArrayList<>();
//            for (char c : spChar.toCharArray()) {
//                chars.add(String.valueOf(c));
//            }
//            this.charList = chars;
//        }
        public void export() {
            //String newFolderPath = folderPath + "\\Result";
            //String newFolderPath = "Result";
            String newFolderPath = "";
            //File dir = new File(newFolderPath);
            //if (!dir.exists()) {
            //    dir.mkdir();
            //}
            for (MyFile myFile : fileContainer) {
                myFile.exportFile(newFolderPath);
            }
        }
    }

    public static class MyFile {

        public String filePath;
        public ArrayList<String> contents;
        public ArrayList<String> dictionary;
        //public ArrayList<String> charList;
        String charList = "aáàảạãâấậẫầẩăắằẳẵặbcdđeéèẻẽẹêếềểễệfghiíìỉĩịjklmnoóòõỏỗọôốồổỗộơớờởỡợpqrstuúùủũụưứừửữựvxyýỳỷỹỵ.,?-/: 0123456789zw";
        String toanList = "aáàảạãâấậẫầẩăắằẳẵặbcdđeéèẻẽẹêếềểễệfghiíìỉĩịjklmnoóòõỏỗọôốồổỗộơớờởỡợpqrstuúùủũụưứừửữựvxyýỳỷỹỵzw";
        String alphabet = "áàảạãâấậẫầẩăắằẳẵặđéèẻẽẹêếềểễệíìỉĩịóòõỏọôồốổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵ";
        public ArrayList<String> alphabetList;
        //2 dau gach ngang
        String pattern1 = "^\\s*\\d{1,2}-\\d{1,2}-\\d{2,4}[\\.,\\?]? ";
        String pattern2 = " \\d{1,2}-\\d{1,2}-\\d{2,4}[\\.|\\?]$";
        String pattern3 = " \\d{1,2}-\\d{1,2}-\\d{2,4}[\\.,\\?]? ";
        //1 dau gach ngang
        String pattern4 = "^\\s*\\d{1,2}-\\d{1,2}[\\.,\\?]? ";
        String pattern5 = " \\d{1,2}-\\d{1,2}[\\.,\\?]? ";
        String pattern6 = " \\d{1,2}-\\d{1,2}[\\.|\\?]$";

        String pattern_4 = "^\\s*\\d{1,2}-\\d{4}[\\.,\\?]? ";
        String pattern_5 = " \\d{1,2}-\\d{4}[\\.,\\?]? ";
        String pattern_6 = " \\d{1,2}-\\d{4}[\\.|\\?]$";
        //2 dau gach cheo
        String pattern7 = "^\\s*\\d{1,2}/\\d{1,2}/\\d{2,4}[\\.,\\?]? ";
        String pattern8 = " \\d{1,2}/\\d{1,2}/\\d{2,4}[\\.|\\?]$";
        String pattern9 = " \\d{1,2}/\\d{1,2}/\\d{2,4}[\\.,\\?]? ";
        //1 dau gach cheo
        String pattern10 = "^\\s*\\d{1,2}/\\d{1,2}[\\.,\\?]? ";
        String pattern11 = " \\d{1,2}/\\d{1,2}[\\.,\\?]? ";
        String pattern12 = " \\d{1,2}/\\d{1,2}[\\.|\\?]$";

        String pattern_10 = "^\\s*\\d{1,2}/\\d{4}[\\.,\\?]? ";
        String pattern_11 = " \\d{1,2}/\\d{4}[\\.,\\?]? ";
        String pattern_12 = " \\d{1,2}/\\d{4}[\\.|\\?]$";
        //1 dau hai cham
        String pattern13 = "^\\s*\\d{1,2}:\\d{1,2}[\\.,\\?]? ";
        String pattern14 = " \\d{1,2}:\\d{1,2}[\\.,\\?]? ";
        String pattern15 = " \\d{1,2}:\\d{1,2}[\\.|\\?]?$";

        public void exportFile(String FolderPath) {
            //String newPath = FolderPath + "\\" + getFileName();
            String newPath = getFileName();
            File file = new File(newPath);
            if (file.exists()) {
                file.delete();
            }
            try {
                file.createNewFile();
                HashSet<String> hSetcontents = new HashSet<>(contents);
                for (String content : hSetcontents) {
                    //content = content.replace("..", ".");
                    content = content.replaceAll("([^\\.|\\?])$", "$1.");
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
            //System.out.println("Export " + f.getName() + "...");
            return f.getName();
        }

        public void setContents(ArrayList<String> contents) {
            this.contents = new ArrayList<>();
            for (String content : contents) {
                if (content.equals("")) {
                    continue;
                }

//                content = content.replace("...", ".");
//                content = content.replace("..", ".");
//                content = content.replace(" .", ".");
//                content = content.replace(" ,", ",");
//                content = content.replace(" ?", "?");
//                content = content.replaceAll("(\\d)\\.(\\d)", "$1$2");
//                content = content.replaceAll("\\.(\\w)", ". $1");
                for (String chars : specChar()) {
                    content = content.replace(chars, " ");
                }
                content = content.replaceAll("\\s+", " ");
                for (char alphabet : content.toCharArray()) {
                    if (!charList.contains(String.valueOf(alphabet))) {
                        continue;
                    }
                }
                this.contents.add(content);
            }
        }

        public void filterFile() {
            ArrayList<String> newContents = new ArrayList<>();
            boolean blCheck = true;
            for (String line : contents) {
                blCheck = true;
                //System.out.println(getFileName() + ": " + line);
                line = checkLine(line);
                if (line.equals("")) {
                    continue;
                }
                line = line.replaceAll("\\s*%\\s*", "% ");
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
                    line = line.replace(" ,", ",");
                    line = line.replace(" .", ".");
                    line = line.replace(" ?", "?");
                    line = line.replaceAll("\\s+", " ");
                    newContents.add(line);
                }
            }
            this.contents = newContents;
        }

        public String checkLine(String line) {
            String paragraph = "";
            if (line.trim().equals("")) {
                return "";
            }
            line = threeDotProcess(line);
            line = line.replaceAll("(\\d+)\\.(\\d+)", "$1~$2");
            line = line.replaceAll("([dr|ms|mrs|mr])\\.", "$1~");
            line = line.replaceAll("\\?", "> ?");
            String pattern = "\\.|\\?";
            String[] splitLine = line.split(pattern);
            for (String sentence : splitLine) {
                sentence = sentence.replaceAll("~", ".");
                String[] splitSentence = sentence.split("\\s+");
                if (splitSentence.length >= 5) {
                    if (checkDateTime(sentence)) {
                        sentence = sentence.replaceAll(">", "?");
                        paragraph += sentence.trim().replace("___", "...").replaceAll("\\.+$", ".");
                        if (paragraph.charAt(paragraph.length() - 1) != '.') {
                            paragraph += ". ";
                        }
                    }
                }
            }
            paragraph = paragraph.replaceAll("\\?\\s*\\.", "?");
            paragraph = paragraph.replaceAll(",\\s*\\.", ",");
            paragraph = paragraph.trim();
            return paragraph;
        }

        public String threeDotProcess(String line) {
            line = line.replace(" ..", "..");
            line = line.replace(" …", "…");
            line = line.replaceAll("\\.+ ", "…");
            line = line.replace("… ", "…");
//            if (line.contains("..")) {
//                int index = line.indexOf("..");
//                for (int i = index; i < line.length(); i++) {
//                    if (line.charAt(i) != '.') {
//                        if (!toanList.contains(String.valueOf(line.charAt(i)))) {
//                            line = line.replaceAll("(\\.)+", "$1 ");
//                        } else {
//                            line = line.replaceAll("(\\.)+", "... ");
//                        }
//                        System.out.println(line.charAt(i));
//                        System.out.println(line);
//                        break;
//                    }
//                }
//            }
            if (line.contains("…")) {
                int index = line.indexOf("…");
                for (int i = index; i < line.length(); i++) {
                    if (line.charAt(i) == '…') {
                        if (i != line.length() - 1) {
                            if (!toanList.contains(String.valueOf(line.charAt(i+1)))) {
                                //System.out.println(line.charAt(i+1));
                                //line = line.replaceAll("…", ". ");
                                line = line.replaceFirst("…", ". ");
                                //line.charAt(i) = '.';
                            } else {
                                line = line.replaceFirst("…", "_ ");
                            }
                        } else {
                            line = line.replaceAll("…", ". ");
                        }

                        //System.out.println(line);

                    }
                }
            }
            return line.replace("_", "___");
        }

        public boolean checkDateTime(String sentence) {
            int dashNumber = getNumberOfTimeAppear(sentence, '-');
            int slashNumber = getNumberOfTimeAppear(sentence, '/');
            int colonNumber = getNumberOfTimeAppear(sentence, ':');
            if (dashNumber > 0) {
                if (!checkDash(sentence, dashNumber)) {
                    return false;
                }
            }
            if (slashNumber > 0) {
                if (!checkSlash(sentence, slashNumber)) {
                    return false;
                }
            }
            if (colonNumber > 0) {
                if (!checkColon(sentence, colonNumber)) {
                    return false;
                }
            }
            return true;
        }

        public boolean checkDash(String sentence, int dashNumber) {
            int count = 0;

            int match = matchCount(sentence, pattern1);
            if (match > 0) {
                count = match * 2;
            }
            match = matchCount(sentence, pattern2);
            if (match > 0) {
                count = match * 2;
            }
            match = matchCount(sentence, pattern3);
            if (match > 0) {
                count = match * 2;
            }
            match = matchCount(sentence, pattern4);
            if (match > 0) {
                count = match;
            }
            match = matchCount(sentence, pattern5);
            if (match > 0) {
                count = match;
            }
            match = matchCount(sentence, pattern6);
            if (match > 0) {
                count = match;
            }
            match = matchCount(sentence, pattern_4);
            if (match > 0) {
                count = match;
            }
            match = matchCount(sentence, pattern_5);
            if (match > 0) {
                count = match;
            }
            match = matchCount(sentence, pattern_6);
            if (match > 0) {
                count = match;
            }
            if (dashNumber == count) {
                return true;
            }
            return false;
        }

        public boolean checkSlash(String sentence, int slashNumber) {
            int count = 0;
            int match = matchCount(sentence, pattern7);
            if (match > 0) {
                count = match * 2;
            }
            match = matchCount(sentence, pattern8);
            if (match > 0) {
                count = match * 2;
            }
            match = matchCount(sentence, pattern9);
            if (match > 0) {
                count = match * 2;
            }
            match = matchCount(sentence, pattern10);
            if (match > 0) {
                count = match;
            }
            match = matchCount(sentence, pattern11);
            if (match > 0) {
                count = match;
            }
            match = matchCount(sentence, pattern12);
            if (match > 0) {
                count = match;
            }
            match = matchCount(sentence, pattern_10);
            if (match > 0) {
                count = match;
            }
            match = matchCount(sentence, pattern_11);
            if (match > 0) {
                count = match;
            }
            match = matchCount(sentence, pattern_12);
            if (match > 0) {
                count = match;
            }
            if (slashNumber == count) {
                return true;
            }
            return false;
        }

        public boolean checkColon(String sentence, int colonNumber) {
            int count = 0;
            int match = matchCount(sentence, pattern13);
            if (match > 0) {
                count = match * 2;
            }
            match = matchCount(sentence, pattern14);
            if (match > 0) {
                count = match * 2;
            }
            match = matchCount(sentence, pattern15);
            if (match > 0) {
                count = match * 2;
            }
            if (colonNumber == count) {
                return true;
            }
            return false;
        }

        public int matchCount(String sentence, String pattern) {
            int count = 0;
            Pattern p1 = Pattern.compile(pattern);
            Matcher m1 = p1.matcher(sentence);
            while (m1.find()) {
                count++;
            }
            return count;
        }

//        public int matchCount(Matcher m) {
//            int count = 0;
//            while (m.find()) {
//                count++;
//            }
//            return count;
//        }
        public int getNumberOfTimeAppear(String sentence, char character) {
            int count = 0;
            if (sentence.contains(String.valueOf(character))) {
                for (int i = 0; i < sentence.length(); i++) {
                    if (character == sentence.charAt(i)) {
                        count++;
                    }
                }
            }
            return count;
        }

        public boolean checkWord(String word) {
            try {
                Double num = Double.parseDouble(word);
                return true;
            } catch (NumberFormatException e) {

            }
            if (word.contains("-") || word.contains("/")) {
                return true;
            }
            if (word.contains("%")) {
                Pattern p = Pattern.compile("\\d+\\s*%");
                Matcher m = p.matcher(word);
                if (m.matches()) {
                    return true;
                } else {
                    return false;
                }
            }
            if (word.contains(".") || word.contains(",")) {
                if (word.matches("\\d+[\\.|,]\\d+")) {
                    return true;
                }
            }
            if (word.contains("...")) {
                String newWord = word.replace("...", "");
                //System.out.println("");
            }

            if (word.charAt(word.length() - 1) == '.' || word.charAt(word.length() - 1) == ',' || word.charAt(word.length() - 1) == '?') {
                if (word.toLowerCase().equals("dr.") || word.toLowerCase().equals("mr.") || word.toLowerCase().equals("mrs.") || word.toLowerCase().equals("ms.")) {
                    return true;
                }
                String removeCharWord = word.replace(String.valueOf(word.charAt(word.length() - 1)), "");
                if (!deepCheckWord(removeCharWord)) {
                    return false;
                }
                return true;
            }

            if (!deepCheckWord(word)) {
                return false;
            }

            return true;
        }

        public boolean deepCheckWord(String word) {
            for (char c : word.toLowerCase().toCharArray()) {
                if (c == '﻿') {
                    continue;
                }
                if (!charList.contains(String.valueOf(c))) {
                    return false;
                }
                if (alphabet.contains(String.valueOf(c))) {
                    if (dictionary.contains(word.trim().toLowerCase())) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            return true;
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
        //chars.add("%");
        chars.add("^");
        chars.add("&");
        chars.add("*");
        chars.add("(");
        chars.add(")");
        //chars.add("-");
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
        // chars.add("/");
        //chars.add("…");
        chars.add("”");
        return chars;

    }
}
