package com.exarcplus.nsci.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Gallery_List {
    @SerializedName("data")
    @Expose
    private List<Gallery_Data> data = null;

    public List<Gallery_Data> getData() {
        return data;
    }

    public void setData(List<Gallery_Data> data) {
        this.data = data;
    }
}
