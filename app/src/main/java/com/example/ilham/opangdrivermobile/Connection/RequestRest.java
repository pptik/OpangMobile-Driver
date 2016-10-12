package com.example.ilham.opangdrivermobile.Connection;

import android.content.Context;
import android.util.Log;

import com.example.ilham.opangdrivermobile.Setup.ApplicationConstants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;


public class RequestRest extends ConnectionHandler {

    final int DEFAULT_TIMEOUT = 400 * 1000;
    private String TAG_LOGIN = "Login ";
    protected static AsyncHttpClient mClient = new AsyncHttpClient();

    public RequestRest(Context context, IConnectionResponseHandler handler) {
        this.mContext = context;
        this.responseHandler = handler;
    }

    @Override
    public String getAbsoluteUrl(String relativeUrl) {
        return ApplicationConstants.HTTP_URL + relativeUrl;
    }


    public void testConnection(){
        RequestParams params = new RequestParams();
       mClient.addHeader("x-ami-cc", "MOBILE");
        System.setProperty("http.keepAlive", "false");
        get("network.json", params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                 responseHandler.onSuccessJSONObject(response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseBody, Throwable e) {
                super.onFailure(statusCode, headers, responseBody, e);
                responseHandler.onFailure(responseBody);
            }

            @Override
            public void onFinish() {
                super.onFinish();
             }

        }, mClient);
    }

    public void goLogin(String email, String password, String idrole){


        RequestParams params = new RequestParams();
        mClient.addHeader("sessid", "0");
        mClient.addHeader("deviceid", "1234567");
        mClient.addHeader("API-KEY", "SEMUT_ANDROID");
        params.put("Email", email);
        params.put("Password", password);
        params.put("ID_role", idrole);
        params.put("Pushid", "no id");


        post("opank/signin", params, new JsonHttpResponseHandler() {

            //   ProgressDialog dialog;

            @Override
            public void onStart() {
                super.onStart();
                Log.i(TAG_LOGIN, "Sending request");
                //   dialog = ProgressDialog.show(mContext, "Connecting", "Check Connection", true);
            }

            @Override
            public void onSuccess(JSONObject response) {
                super.onSuccess(response);
                Log.i(TAG_LOGIN, "Success");
                responseHandler.onSuccessJSONObject(response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseBody, Throwable e) {
                super.onFailure(statusCode, headers, responseBody, e);
                Log.e(TAG_LOGIN, "Failed");
                responseHandler.onFailure(e.toString());//e.getMessage());
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.i(TAG_LOGIN, "Disconnected");
                //   dialog.dismiss();
            }

        }, mClient);
    }



}
