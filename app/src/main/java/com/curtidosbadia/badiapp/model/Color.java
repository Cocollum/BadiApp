package com.curtidosbadia.badiapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Color {
    private Integer id;
    private String name;
    private String code;
    private String hex;

    public Color() {
        this.id = 0;
        this.name = "";
        this.code = "";
        this.hex = "";
    }

    public Color(String json) throws JSONException {
        JSONObject obj = new JSONObject(json);
        this.id = obj.getInt("color_id");
        this.name = obj.getString("color_name");
        this.code = obj.getString("color_code");
        this.hex = obj.getString("color_hex");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    @Override
    public String toString(){
        return id + " - " + name;
    }
}
