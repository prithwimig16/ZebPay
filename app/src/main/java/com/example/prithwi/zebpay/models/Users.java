package com.example.prithwi.zebpay.models;

import org.json.JSONObject;

/**
 * Created by Prithwi on 28/03/18.
 */

public class Users {

    protected String name;
    protected String reputation;
    protected String imageUrl;

    public String getName() {
        return name;
    }

    public String getReputation() {
        return reputation;
    }

    public String getImageUrl() {
        return imageUrl;
    }
  public Users(JSONObject jsonObject){
        this.name=jsonObject.optString("display_name");
        this.reputation=jsonObject.optString("reputation");
        this.imageUrl=jsonObject.optString("profile_image");
 }

}
