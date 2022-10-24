package com.curtidosbadia.badiapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Product {
    private String id;
    private String image;
    private String name;
    private String url;
    private String status;
    private String qr_logo;

    public Product() {
        this.id = "";
        this.image = "";
        this.name = "";
        this.url = "";
        this.status = "";
        this.qr_logo = "";
    }

    public Product(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        this.id = obj.getString("product_id");
        this.image = obj.getString("product_image");
        this.name = obj.getString("product_name");
        this.url = obj.getString("product_url");
        this.status = obj.getString("product_status");
        this.qr_logo = obj.getString("product_qr_logo");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQr_logo() {
        return qr_logo;
    }

    public void setQr_logo(String qr_logo) {
        this.qr_logo = qr_logo;
    }

    @Override
    public String toString(){
        return id + " - " + name;
    }
}
