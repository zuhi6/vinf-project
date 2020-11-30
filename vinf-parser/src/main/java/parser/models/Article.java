package parser.models;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Article {
    private String articleName;
    private String articleUrl;
    public LinkedList<Category> categories = new LinkedList<Category>();
    public LinkedHashMap<String, String> languages = new LinkedHashMap<String, String>();
    public LinkedList<Redirect> redirects = new LinkedList<Redirect>();

    public Article(String articleName, String articleUrl) {
        this.articleName = articleName;
        this.articleUrl = articleUrl;
    }

    public String getArticleName() {
        return articleName;
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }

    public void addLanguage(String language, String languageUrl) {
        this.languages.put(language, languageUrl);
    }

    public void addRedirect(Redirect redirect) {
        this.redirects.add(redirect);
    }

    public String getArticleUrl() {
        return articleUrl;
    }
}
