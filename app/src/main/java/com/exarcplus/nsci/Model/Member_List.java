package com.exarcplus.nsci.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Member_List {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private List<Member_Data> data = null;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Member_Data> getData() {
        return data;
    }

    public void setData(List<Member_Data> data) {
        this.data = data;
    }
}
