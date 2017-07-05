package com.comp.iitb.vialogue.Network;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by DELL on 5/10/2017.
 */

public class NetworkCalls {

    private OkHttpClient mOKHttpClient;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public NetworkCalls() {
        mOKHttpClient = new OkHttpClient();
    }

    public Response doPostRequest(String url, String json) throws IOException {
        System.out.println("doPostRequest : url : " + url);
        System.out.println("doPostRequest : body : " + json.toString());
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = mOKHttpClient.newCall(request).execute();
        return response;
    }

    public Response doGetRequest(String url) throws IOException {
        System.out.println("doGetRequest : url : " + url);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = mOKHttpClient.newCall(request).execute();
        return response;
    }
}
