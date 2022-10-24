package com.curtidosbadia.badiapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class RelatedProduct {
    private String title;
    private String image;
    private String url;

    public RelatedProduct(){
        title = "";
        image = "";
        url = "";
    }

    public RelatedProduct(JSONObject obj) throws JSONException{
        title = obj.getString("title");
        image = obj.getString("image");
        url = obj.getString("url");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
