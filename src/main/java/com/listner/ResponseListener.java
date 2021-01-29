package com.listner;

import org.json.JSONObject;

public interface ResponseListener {

    public abstract void onSuccess(JSONObject data);

    public abstract void onError(JSONObject data);
}