package com.curtidosbadia.badiapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.curtidosbadia.badiapp.model.User;

import org.json.JSONException;

public class Session {
    private SharedPreferences prefs;

    public Session(Context ctx) {
        prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    }


    public void setUser(User user) throws JSONException {
        prefs.edit().putString("user", user.toJSON()).commit();
    }
    public void removeUser(){
        prefs.edit().remove("user").commit();
    }

    public User getUser() throws JSONException{
        String user_json = prefs.getString("user","");
        if(!user_json.equals("")) {
            User object = new User(user_json);
            return object;
        }
        return null;
    }
}
