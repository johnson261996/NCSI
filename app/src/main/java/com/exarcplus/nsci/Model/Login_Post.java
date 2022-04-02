package com.exarcplus.nsci.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Login_Post {

    @SerializedName("result")
    @Expose
    private String result;

    public String getResult() {
        return result;
    }

    @SerializedName("data")
    @Expose
    private List<Login_Data> data = null;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @SerializedName("token")
    @Expose
    private String token;


    public void setResult(String result) {
        this.result = result;
    }

    public List<Login_Data> getData() {
        return data;
    }

    public void setData(List<Login_Data> data) {
        this.data = data;
    }
}
