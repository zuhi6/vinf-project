package parser.models;

public class Redirect {
    private String redirectName;
    private String redirecrUrl;

    public Redirect(String redirectName, String redirecrUrl) {
        this.redirectName = redirectName;
        this.redirecrUrl = redirecrUrl;
    }

    public String getRedirecrUrl() {
        return redirecrUrl;
    }

    public String getRedirectName() {
        return redirectName;
    }
}
