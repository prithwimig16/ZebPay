package com.example.prithwi.zebpay.network;

import org.json.JSONObject;

/**
 * Created by Prithwi on 07/02/18.
 */

public interface ZapPayHttpCommCallback {

    public void onSuccess(boolean status, int tag, JSONObject jsonResponse);

    public void onFailure(JSONObject error, int tag);
}
