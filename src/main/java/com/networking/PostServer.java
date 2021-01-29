package com.networking;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.Interface.VolleyResponseListener;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public final class PostServer {
    private final int MAX_RETRIES;

    private final Context context;

    public final void commonServiceForGet(String url, JSONObject jobj, final VolleyResponseListener listener) {
        final RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        Log.d("getProspective", "url=" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(0, url, jobj, (Listener)(new Listener() {
            // $FF: synthetic method
            // $FF: bridge method
            public void onResponse(Object var1) {
                this.onResponse((JSONObject)var1);
            }

            public final void onResponse(JSONObject response) {
                try {
                    Log.d("getProspective", "data=" + response);
                    listener.onResponse(response);
                    RequestQueue var10000 = requestQueue;
                    if (requestQueue == null) {

                    }

                    var10000.stop();
                } catch (Exception var3) {
                    var3.printStackTrace();
                }

            }
        }), (ErrorListener)(new ErrorListener() {
            public final void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {
                    Toast.makeText(PostServer.this.getContext(), (CharSequence)" Something went wrong .Please try again.", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("Error.Response", "error=" + error);
                }

            }
        }));
        jsonObjectRequest.setRetryPolicy((RetryPolicy)(new DefaultRetryPolicy(41000, 1, 1.0F)));
        if (requestQueue == null) {

        }

        requestQueue.add((Request)jsonObjectRequest);
    }

    public final void commonServiceForPost(String url, JSONObject jobj, final VolleyResponseListener listener) {

        RequestQueue requestQueue = Volley.newRequestQueue(this.context);
        Log.d("songRequest", "url=" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(1, url, jobj, (Listener)(new Listener() {
            // $FF: synthetic method
            // $FF: bridge method
            public void onResponse(Object var1) {
                this.onResponse((JSONObject)var1);
            }

            public final void onResponse(JSONObject response) {
                try {
                    Log.d("songRequest", "data=" + response);
                    listener.onResponse(response);
                } catch (Exception var3) {
                    var3.printStackTrace();
                }

            }
        }), (ErrorListener)(new ErrorListener() {
            public final void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError) {


                } else {
                    parseVolleyError(error);
                    String responseBody = null;
                    try {
                        responseBody = new String(error.networkResponse.data, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    try {


                        JSONObject data = new JSONObject(responseBody);
                   /* JSONArray errors = data.getJSONArray("errors");
                    JSONObject jsonMessage = errors.getJSONObject(0);*/
                        listener.onResponse(data);
                    }catch (JSONException e){

                        e.printStackTrace();
                    }
                }

//                listener.onResponse(error);

                //  listener.onError("Failed");
                Log.d("Error.Response", "error=" + error);
            }
        }));
        jsonObjectRequest.setRetryPolicy((RetryPolicy)(new DefaultRetryPolicy(11000, 1, 1.0F)));
        if (requestQueue == null) {

        }

        requestQueue.add((Request)jsonObjectRequest);
    }



    public final Context getContext() {
        return this.context;
    }

    public PostServer(Context context) {
        this.context = context;
        this.MAX_RETRIES = 100;
    }
    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            //  JSONObject errors = data.getJSONObject("success");
            // JSONObject jsonMessage = errors.getJSONObject(0);
            String message = data.getString("data");

            Toast.makeText(PostServer.this.getContext(), message, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
        } catch (UnsupportedEncodingException errorr) {
        }
    }
}
