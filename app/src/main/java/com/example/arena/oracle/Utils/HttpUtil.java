package com.example.arena.oracle.Utils;


import android.app.DownloadManager;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by macbook on 2017/4/26.
 */

public class HttpUtil {

    public static String host = "http://10.18.14.253/8080/Onlinexam";
    public static String actionType_Login = "userLogin";
    public static String actionType_paperList = "get_paperList";
    public static String actionType_paperDetail = "get_paperDetial";
    public static String actionType_history = "get_history";
    public static String actionType_savePaper = "savePaper";

    //okhttp get
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();



    //post方法
    //post的参数体
    //RequestBody requestBody = new FormBody.Builder()
    //        .add("name", "1")
    //        .add("password", "2")
    //        .build();
    //Request requestPost = new Request.Builder().url("url").post(requestBody).build();


    //用host+?actionType=actionType&...
    public static String get(String url){
        //默认get方法
        Request request = new Request.Builder().get().url(url).build();
        try(Response response = okHttpClient.newCall(request).execute()){
            return response.body().string();
        }
        catch (Exception e){
            Log.d("exception", e.toString());
        }
        return null;
    }


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String post(String url, String json){

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        try (Response response = okHttpClient.newCall(request).execute()){
            return response.body().string();
        }
        catch (Exception e){
            Log.d("exception", e.toString());
        }
        return null;

    }
}
