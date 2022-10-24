package com.curtidosbadia.badiapp.model;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private String user_id;
    private String user_name;
    private String user_email;
    private String user_token;
    private String user_type;

    public User(String json_string) throws JSONException {
        JSONObject obj = new JSONObject(json_string);

        this.user_id = obj.getString("user_id");
        this.user_name = obj.getString("user_name");
        this.user_email = obj.getString("user_email");
        this.user_token = obj.getString("user_token");
        this.user_type = obj.getString("user_type");
    }

    public String toJSON(){
        try{
            JSONObject obj = new JSONObject();
            obj.put("user_id", this.user_id);
            obj.put("user_name", this.user_name);
            obj.put("user_email", this.user_email);
            obj.put("user_token", this.user_token);
            obj.put("user_type", this.user_type);

            return obj.toString();
        }catch (JSONException exc){
            exc.printStackTrace();
        }
        return "";
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
