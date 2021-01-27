package com.Interface;

public interface VolleyResponseListener {
    void onError(String message);

    void onResponse(Object response);
}
