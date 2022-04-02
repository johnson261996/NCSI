package com.exarcplus.nsci.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OngoingEvent_List {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("data")
    @Expose
    private List<OngoingEvent_Data> data = null;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<OngoingEvent_Data> getData() {
        return data;
    }

    public void setData(List<OngoingEvent_Data> data) {
        this.data = data;
    }

}
