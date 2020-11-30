package parser.models;

public class Category {
    private String categoryName;
    private String categoryUrl;

    public Category(String categoryName, String categoryUrl) {
        this.categoryName = categoryName;
        this.categoryUrl = categoryUrl;
    }

    public String getCategoryUrl() {
        return categoryUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
