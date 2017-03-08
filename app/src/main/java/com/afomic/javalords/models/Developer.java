package com.afomic.javalords.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by afomic on 06-Mar-17.
 * this a model class which represent the developer object
 */

public class Developer {
    private String mUsername;
    private String mProfileImageURL;
    private String mProfileURL;
    public Developer(String username,String profileImageURL,String profileURL){
        mUsername=username;
        mProfileImageURL=profileImageURL;
        mProfileURL=profileURL;

    }
    public Developer(JSONObject developer)throws JSONException{
        mUsername=developer.optString("login");
        mProfileURL=developer.optString("url");
        mProfileURL=developer.optString("avatar_url");
    }
    public String getUsername(){
        return mUsername;
    }
    public void setUsername(String username){
        mUsername=username;
    }
    public String getProfileImageURL(){
        return mProfileImageURL;
    }
    public void setProfileImageURL(String url){
        mProfileImageURL=url;
    }
    public String getProfileURL(){
        return mProfileURL;
    }
    public void setProfileURL(String url){
        mProfileURL=url;
    }

}
