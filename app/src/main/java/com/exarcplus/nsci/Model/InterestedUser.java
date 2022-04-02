package com.exarcplus.nsci.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InterestedUser {
    @SerializedName("member_id")
    @Expose
    private String member_id;
    @SerializedName("result")
    @Expose
    private String result;

    @SerializedName("image")
    @Expose
    private String image;

    public String getId() {
        return member_id;
    }

    public void setId(String id) {
        this.member_id = id;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
