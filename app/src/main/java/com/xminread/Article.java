package com.xminread;

public class Article {

    String title;
    String url;
    String content;

    public void Article(){
        title = "";
        url = "";
        content = "";
    }

    public void Article(String title, String content){
        this.title = title;
        this.content = content;
    }

    public String getContent() {
        return content;
    }
    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
