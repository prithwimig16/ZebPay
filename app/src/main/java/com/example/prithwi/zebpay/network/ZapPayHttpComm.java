package com.example.prithwi.zebpay.network;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.prithwi.zebpay.utils.Utils;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prithwi on 07/02/18.
 */
public class ZapPayHttpComm {


    public static final int USER_SERVICE=1010;

    RequestQueue volleyRequestQueue;


    public static final String GENERIC_ERROR_MESSAGE = "There was some problem in connecting server.\nPlease check your internet connection.";



    public static final int INVALID_ACCESS_TOKEN = 401;

    private Context context;
   private ZapPayHttpCommCallback callback;
    private int tag;


    private ZapPayHttpComm() {
    }

    private JSONObject values;
    private String finalUrl;

    public ZapPayHttpComm(Context context) {
        this.context = context;
        this.callback = (ZapPayHttpCommCallback) context;
        //this.finalUrl = "https://api.stackexchange.com/2.2/users?page=100&pagesize=100&order=desc&sort=reputation&site=stackoverflow";
        this.values = new JSONObject();


    }

    public ZapPayHttpComm(Context context, ZapPayHttpCommCallback fragment) {
        this.context = context;
        this.callback = fragment;
        //this.finalUrl ="https://api.stackexchange.com/2.2/users?page=100&pagesize=100&order=desc&sort=reputation&site=stackoverflow";
        this.values = new JSONObject();

    }

  

    public static ZapPayHttpComm getNewInstance(Context context)
    {
        return new ZapPayHttpComm(context);
    }

    public static ZapPayHttpComm getNewInstance(Context context, ZapPayHttpCommCallback fragment)
    {
        return new ZapPayHttpComm(context,fragment);
    }

    private void processConnection() {


        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, this.finalUrl, this.values, new Response.Listener<JSONObject>()
        {

                    @Override
                    public void onResponse(JSONObject response) {

                        Utils.consoleLog(getClass(), response.toString());
//                        try {
//                            boolean status = response.getBoolean("status");


                            if(callback!=null) {
                                callback.onSuccess(true, tag, response);
                            }
//                        } catch (JSONException e) {
//                            Utils.consoleLog(getClass(),e.getLocalizedMessage());
//                            if(callback!=null) {
//                                callback.onFailure(null, tag);
//                            }
//                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        Utils.consoleLog(getClass(), error.toString());
                        JSONObject rootObj = new JSONObject();
                        JSONObject errorObj = new JSONObject();
                        try {
                            errorObj.put("error_code", "400");
                            errorObj.put("error_msg", error.toString());
                            rootObj.put("error",errorObj);
                        } catch (JSONException e) {

                            if(callback!=null) {
                                callback.onFailure(null, tag);
                            }
                        }
                        if(error.networkResponse!=null) {
                            if (callback != null) {

                                callback.onFailure(rootObj, tag);
                            }
                        }
                        else {
                            if (callback != null) {

                                callback.onFailure(null, tag);
                            }
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();


                return headers;
            }
        };
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        Utils.consoleLog(ZapPayHttpComm.class,jsObjRequest.toString());
        Utils.volleyRequestQueue.add(jsObjRequest);
    }



    public void callUserService(int page,int pageSize)
    {
        try
        {

            this.tag = USER_SERVICE;
            this.finalUrl = "https://api.stackexchange.com/2.2/users?page="+page+"&pagesize="+pageSize+"&order=desc&sort=reputation&site=stackoverflow";
            this.processConnection();
        }
        catch(Exception e)
        {

        }
    }




}
