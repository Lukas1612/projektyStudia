package sample;

public class Size {

    String label;
    int width;
    int height;
    String source;
    String url;
    String media;

    public Size(String label, int width, int height, String source, String url, String media) {
        this.label = label;
        this.width = width;
        this.height = height;
        this.source = source;
        this.url = url;
        this.media = media;
    }

    public Size() {
    }

    public String getLabel() {
        return label;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getSource() {
        return source;
    }

    public String getUrl() {
        return url;
    }

    public String getMedia() {
        return media;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    @Override
    public String toString() {
        return "Size{" +
                "label='" + label + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", source='" + source + '\'' +
                ", url='" + url + '\'' +
                ", media='" + media + '\'' +
                '}';
    }
}
