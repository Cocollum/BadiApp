package com.curtidosbadia.badiapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class OrderLine {
    private String line_id;
    private String article;
    private String obs;
    private String color_name;
    private String color_code;
    private String price;
    private String unity;
    private String quantity;
    private String thickness;

    public OrderLine() {

        this.line_id = "";
        this.article = "";
        this.obs = "";
        this.color_name = "";
        this.color_code = "";
        this.price = "";
        this.unity = "";
        this.quantity = "";
        this.thickness = "";
    }

    public OrderLine(String json_string) throws JSONException {
        JSONObject obj = new JSONObject(json_string);

        this.line_id = obj.getString("line_id");
        this.article = obj.getString("article");
        this.obs = obj.getString("obs");
        this.color_name = obj.getString("color_name");
        this.color_code = obj.getString("color_code");
        this.price = obj.getString("price");
        this.unity = obj.getString("unity");
        this.quantity = obj.getString("quantity");
        this.thickness = obj.getString("thickness");
    }

    public String toJSON(){
        try{
            JSONObject obj = new JSONObject();

            obj.put("line_id", this.line_id);
            obj.put("article", this.article);
            obj.put("obs", this.obs);
            obj.put("color_name", this.color_name);
            obj.put("color_code", this.color_code);
            obj.put("price", this.price);
            obj.put("unity", this.unity);
            obj.put("quantity", this.quantity);
            obj.put("thickness", this.thickness);
            return obj.toString();
        }catch(JSONException exc){
            exc.printStackTrace();
        }
        return "";
    }

    public String getLine_id() {
        return line_id;
    }

    public void setLine_id(String line_id) {
        this.line_id = line_id;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getColor_name() {
        return color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }

    public String getColor_code() {
        return color_code;
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnity() {
        return unity;
    }

    public void setUnity(String unity) {
        this.unity = unity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getThickness() {
        return thickness;
    }

    public void setThickness(String thickness) {
        this.thickness = thickness;
    }
}
