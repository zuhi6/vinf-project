package parser;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import parser.models.Article;
import parser.models.Category;
import parser.models.Redirect;

public class Parser {



    public static Set<String> articleUrls = new HashSet<String>();


    public static void main(String[] args) {
            Gson gson = new Gson();
        try {
            PrintWriter writerClean = new PrintWriter(new FileWriter("C:\\Users\\Michal Zajic\\workspaces\\vinf-maven\\data\\result.json", true));
            writerClean.print("");
            writerClean.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        getArticleNames();


        for (String url: articleUrls) {
            Article article = getArticleInfo(url);
            String json = gson.toJson(article);
            System.out.println(json);
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Michal Zajic\\workspaces\\vinf-maven\\data\\result.json", true));
                writer.write(json + "\n");
                writer.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }



    }

    private static void getArticleNames() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("C:\\Users\\Michal Zajic\\workspaces\\vinf-maven\\data\\article_categories_sk.nq"), "UTF-8"));
            String line;
            while(true) {
                line = reader.readLine();
                if(line == null) {
                    break;
                }
                String articleUrl = parseArticleUrl(line, 0);
                articleUrls.add(articleUrl);
            }
            reader.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        try {
            reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("C:\\Users\\Michal Zajic\\workspaces\\vinf-maven\\data\\redirects_sk.nq"), "UTF-8"));
            String line;
            while(true) {
                line = reader.readLine();
                if(line == null) {
                    break;
                }
                String articleUrl = parseArticleUrl(line, 2);
                articleUrls.add(articleUrl);
            }
            reader.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        try {
            reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("C:\\Users\\Michal Zajic\\workspaces\\vinf-maven\\data\\interlanguage_links_sk.nq"), "UTF-8"));
            String line;
            while(true) {
                line = reader.readLine();
                if(line == null) {
                    break;
                }
                String articleUrl = parseArticleUrl(line, 0);
                articleUrls.add(articleUrl);
            }
            reader.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }
    private static String parseArticleUrl(String line, int indexToParse) {
        return decodeUnicode(line.replaceAll("[<>]", "").split(" ")[indexToParse]);
    }

    private static Article getArticleInfo(String articleUrl) {
        String[] articleArray = articleUrl.split("/");
        String articleName = articleArray[articleArray.length - 1].replaceAll("_", " ");
        Article article = new Article(articleName, articleUrl);
        BufferedReader reader;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("C:\\Users\\Michal Zajic\\workspaces\\vinf-maven\\data\\article_categories_sk.nq"), "UTF-8"));
            String line;
            while(true) {
                line = reader.readLine();
                if(line == null) {
                    break;
                }
                Category category = parseCategory(articleUrl, line);
                if (category != null) {
                    article.addCategory(category);
                }
            }

        }
        catch(IOException e) {
            e.printStackTrace();
        }

        try {
            reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("C:\\Users\\Michal Zajic\\workspaces\\vinf-maven\\data\\redirects_sk.nq"), "UTF-8"));
            String line;
            while(true) {
                line = reader.readLine();
                if(line == null) {
                    break;
                }
                Redirect redirect = parseRedirect(articleUrl, line);
                if (redirect != null) {
                    article.addRedirect(redirect);
                }
            }

        }
        catch(IOException e) {
            e.printStackTrace();
        }

        try {
            reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("C:\\Users\\Michal Zajic\\workspaces\\vinf-maven\\data\\interlanguage_links_sk.nq"), "UTF-8"));
            String line;
            while(true) {
                line = reader.readLine();
                if(line == null) {
                    break;
                }
                HashMap<String,String > language = parseInterlanguage(articleUrl, line);
                if (language != null) {
                    article.addLanguage(language.get("languageName"), language.get("languageUrl"));
                }
            }

        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return article;
    }
    private static Category parseCategory(String articleUrl, String line) {
        String[] stringArray = line.replaceAll("[<>]", "").split(" ");

        String currentArticleUrl = decodeUnicode(stringArray[0]);

        if(!currentArticleUrl.equals(articleUrl)) {
            return null;
        }

        String categoryUrl = decodeUnicode(stringArray[2]);

        String categoryName;
        String[] categoryArray = categoryUrl.split("/");
        String[] categoryNameArray = categoryArray[categoryArray.length - 1].split(":");
        if(categoryNameArray.length > 1) {
            categoryName = categoryNameArray[1].replaceAll("_", " ");
        }
        else {
            categoryName = categoryNameArray[0].replaceAll("_", " ");
        }

        return new Category(categoryName, categoryUrl);
    }

    private static Redirect parseRedirect(String articleUrl, String line) {

        String[] stringArray = line.replaceAll("[<>]", "").split(" ");

        String currentArticleUrl = decodeUnicode(stringArray[2]);

        if(!currentArticleUrl.equals(articleUrl)) {
            return null;
        }

        String redirectUrl = decodeUnicode(stringArray[0]);
        String[] redirectArray = redirectUrl.split("/");
        String redirectName = redirectArray[redirectArray.length - 1].replaceAll("_", " ");

        return new Redirect(redirectName, redirectUrl);
    }

    private static HashMap<String, String> parseInterlanguage(String articleUrl, String line) {
        String[] stringArray = line.replaceAll("[<>]", "").split(" ");

        String currentArticleUrl = decodeUnicode(stringArray[0]);

        if(!currentArticleUrl.equals(articleUrl)) {
            return null;
        }
        String interlanguageUrl = decodeUnicode(stringArray[2]);

        String interlanguageName = interlanguageUrl.split("/")[2].split("\\.")[0];


        if (!interlanguageName.equals("cs") && !interlanguageName.equals("ru") && !interlanguageName.equals("pl")
                && !interlanguageName.equals("hu") && !interlanguageName.equals("de") && !interlanguageName.equals("dbpedia")) {

            return null;
        }

        if(interlanguageName.equals("dbpedia")) {
            interlanguageName = "en";
        }

        HashMap<String, String> interlanguage = new HashMap<String, String>();
        interlanguage.put("interlanguageName", interlanguageName);
        interlanguage.put("interlanguageUrl", interlanguageUrl);

        return interlanguage;

    }

    public static String decodeUnicode(String line) {
        while(true) {
            Pattern p = Pattern.compile("\\\\u[0-9A-Fa-f]{4}"); //
            Matcher m = p.matcher(line);
            if (m.find()) {
                String hexNum = m.group().split("u")[1] ;
                int hexToInt = Integer.parseInt(hexNum, 16);
                char[] intToChar = Character.toChars(hexToInt);
                line = line.replaceFirst("\\\\u[0-9A-Fa-f]{4}", String.valueOf(intToChar));
            } else {
                break;
            }
        }
        return line;
    }
    
}