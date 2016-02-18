package com.azzdorfrobotics.android.legstep.repositories;

import android.util.Log;

import com.azzdorfrobotics.android.legstep.BaseActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created on 17.02.2015
 *
 * @author Mykola Tychyna (iMykolaPro)
 */
public class Repository {
    public final static String API_URL = "http://77.88.216.154:7880";
    public final static String API_URL_ALTERNATIVE = "http://62.80.183.154:7880";

    protected String apiUrl(String postfix) {
        return API_URL + postfix;
    }


    protected void post(String url, RequestParams params, AsyncHttpResponseHandler handler) {
        AsyncHttpClient http = new AsyncHttpClient();
        url = apiUrl(url);

        Log.d("REQUEST", "POST " + url + " with params: " + params.toString());
        http.post(url, params, handler);
    }

    protected void put(String url, RequestParams params, AsyncHttpResponseHandler handler) {
        AsyncHttpClient http = new AsyncHttpClient();
        url = apiUrl(url);

        Log.d("REQUEST", "PUT " + url + " with params: " + params.toString());
        http.put(url, params, handler);
    }

    protected void get(String url, RequestParams params, AsyncHttpResponseHandler handler) {
        AsyncHttpClient http = new AsyncHttpClient();
        url = apiUrl(url);

        Log.d("REQUEST", "GET " + url + " with params: " + params.toString());
        http.get(url, params, handler);
    }

    protected void delete(String url, AsyncHttpResponseHandler handler) {
        AsyncHttpClient http = new AsyncHttpClient();
        url = apiUrl(url);

        Log.d("REQUEST", "DELETE " + url);
        http.delete(BaseActivity.getContext(), url, handler);
    }
}
