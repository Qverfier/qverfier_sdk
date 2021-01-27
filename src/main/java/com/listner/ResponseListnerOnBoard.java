package com.listner;

import org.json.JSONObject;

public interface ResponseListnerOnBoard {

    public abstract void onSuccess(JSONObject data);

    public abstract void onError(JSONObject data);
    public abstract void onProgress(JSONObject data);
}