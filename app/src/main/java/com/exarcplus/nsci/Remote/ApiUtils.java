package com.exarcplus.nsci.Remote;

import com.exarcplus.nsci.LoginActivity;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "http://www.carryyear.com:3017/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
