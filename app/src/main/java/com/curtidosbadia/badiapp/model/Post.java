package com.curtidosbadia.badiapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Post {
    private String image;
    private String date;
    private String title;
    private String excerpt;
    private String content;
    private String url;

    public Post(){
        image = "";
        date = "";
        title = "";
        excerpt = "";
        content = "";
        url = "";
    }

    public Post(JSONObject obj) throws JSONException{
        image = obj.getString("image");
        date = obj.optString("date");
        title = obj.getString("title");
        excerpt = obj.getString("excerpt");
        content = obj.optString("content");
        url = obj.getString("url");
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String toJSON(){
        try{
            JSONObject obj = new JSONObject();

            obj.put("image", this.image);
            obj.put("date", this.date);
            obj.put("title", this.title);
            obj.put("content", this.content);
            obj.put("excerpt", this.excerpt);
            obj.put("url", this.url);

            return obj.toString();
        }catch (JSONException exc){
            exc.printStackTrace();
        }
        return "";
    }
}
