
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
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
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Filter filter = new Filter();
        ArrayList<String> list = filter.readText("G:\\Bak\\VNE05072016Part001.txt");
        ArrayList<Article> articles = filter.getlistArticles(list);
        ArrayList<String> listCategory = filter.getListCategory(articles);
        filter.writeToCategoryFile(listCategory, articles);
        //filter.WriteFile(list, "C:\\Users\\Toan\\Desktop\\output.txt");
    }

    public static class Article {

        public String title = "";
        public String content = "";
        public String category = "";

        public Article() {
            title = "";
            content = "";
            category = "";
        }

        public Article(ArrayList<String> articleLine) {
            getTitle(articleLine);
            getContent(articleLine);
            getCategory(articleLine);
        }

        public void getTitle(ArrayList<String> articleLine) {
            boolean titleFlg = false;
            for (String line : articleLine) {
                if (line.contains("<title>")) {
                    titleFlg = true;
                    continue;
                }
                if (titleFlg && !line.contains("</title>")) {
                    title += line.trim() + "\n";
                    continue;
                }
                if (line.contains("</title>")) {
                    titleFlg = false;
                    title = title.trim();
                    return;
                }
            }
        }

        public void getContent(ArrayList<String> articleLine) {
            boolean contentFlg = false;
            for (String line : articleLine) {
                if (line.contains("<content>")) {
                    contentFlg = true;
                    continue;
                }
                if (!line.contains("</content>") && contentFlg) {
                    if (!line.contains("Ảnh:") && !line.contains("<") && !line.contains(">") && !line.contains("\\")) {
                        content += line.trim() + "\n";
                    }
                    continue;
                }
                if (line.contains("</content>")) {
                    contentFlg = false;
                    content = content.trim();
                    return;
                }
            }
        }

        public void getCategory(ArrayList<String> articleLine) {
            boolean categoryFlg = false;
            for (String line : articleLine) {
                if (line.contains("<category>")) {
                    categoryFlg = true;
                    continue;
                }
                if (!line.contains("</category>") && categoryFlg) {
                    category += line.trim() + "\n";
                    continue;
                }
                if (line.contains("</category>")) {
                    categoryFlg = false;
                    category = category.trim();
                    return;
                }
            }
        }

    }

    public static class Filter {

        public String path;
        public String content;

        public void writeToCategoryFile(ArrayList<String> listCategory, ArrayList<Article> articles) {
            for (Article article : articles) {
                for (String category : listCategory) {
                    if (article.category.trim().equals(category.trim())) {
                        try {
                            //exception handling left as an exercise for the reader
                            Files.write(Paths.get(category.trim() + ".txt"), article.content.getBytes(), StandardOpenOption.APPEND);
                        } catch (IOException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }

        public ArrayList<String> getListCategory(ArrayList<Article> articles) {
            ArrayList<String> listCategory = new ArrayList<>();
            for (Article article : articles) {
                if (!listCategory.contains(article.category.trim())) {
                    listCategory.add(article.category.trim());
                }
            }
            //create file
            File file = null;
            for (String category : listCategory) {
                file = new File(category.trim() + ".txt");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return listCategory;
        }

        public ArrayList<Article> getlistArticles(ArrayList<String> list) {
            Article article = null;
            boolean articleFlg = false;
            boolean contentFlg = false;
            boolean categoryFlg = false;
            ArrayList<Article> articles = new ArrayList<>();
            ArrayList<String> articleLine = new ArrayList<>();
            for (String line : list) {
                System.out.println(line);
                if (line.contains("<article>")) {

                    articleLine = new ArrayList<>();
                    articleFlg = true;
                    continue;
                }
                if (articleFlg && !line.contains("</article>")) {
                    if (!line.contains("Ảnh:")) {
                        articleLine.add(line);
                    }
                }
                if (line.contains("</article>")) {
                    article = new Article(articleLine);
                    articles.add(article);
                }

            }
            return articles;
        }

        public ArrayList<String> readText(String fileName) {
            ArrayList<String> list = new ArrayList<>();
            try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    list.add(line);
                }
            } catch (IOException e) {
                System.err.format("IOException: %s%n", e);
            }
            return list;
        }

        private void WriteFile(ArrayList<String> list, String output) {
            FileWriter writer = null;
            File file = null;
            try {
                file = new File(output);
                if (!file.exists()) {
                    file.createNewFile();
                }
                writer = new FileWriter(output);
                for (String str : list) {
                    writer.write(str + "\n");
                }
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    writer.close();
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

}
