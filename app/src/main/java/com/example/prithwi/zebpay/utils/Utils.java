package com.example.prithwi.zebpay.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.android.volley.RequestQueue;

import com.example.prithwi.zebpay.network.ZapPayHttpComm;

import org.json.JSONException;
import org.json.JSONObject;

//import com.google.android.gms.ads.identifier.AdvertisingIdClient;
//import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
//import com.google.android.gms.common.GooglePlayServicesRepairableException;
//import com.purpleyo.tstemperature.networks.PyoHttpComm;
//
//import org.OpenUDID.OpenUDID_manager;

/**
 * Created by Prithwi on 07/02/18.
 */

public class Utils {

    public static String ODID_STRING_PATH = "zgkpad";
    public static final int REQUEST_READ_PHONE_STATE = 1;
    public static final int SHOW_ITEMS_PER_PAGE=21;

    public static RequestQueue volleyRequestQueue;
    private static Utils _singletonObj = null;

    ProgressDialog progressDialog;

    public static Utils getInstance() {
        if (_singletonObj == null) {
            _singletonObj = new Utils();
        }
        return _singletonObj;
    }
    public static void consoleLog(Class classobj, String msg) {
        if (Config.getSharedInstance().debugMode == true) {
            Log.d(classobj.getCanonicalName(), msg);
        }
    }




    public static void alert(String message, Context context) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("ZabPay");

        alertDialog.setMessage(message);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();

    }





    public static void displayServerErrorMessage(JSONObject jsonObject, Context context) {
        if (jsonObject != null) {
            try {
                JSONObject errorObj = jsonObject.getJSONObject("error");
                String errorCode = errorObj.getString("error_code");
                String errorMessage = errorObj.getString("error_msg");
                String displayMessage = errorMessage;
                if (Config.getSharedInstance().debugMode == true) {
                    displayMessage += "\n";
                    displayMessage += errorCode;
                }
                Utils.alert(displayMessage, context);

            } catch (JSONException e) {
                Utils.consoleLog(context.getClass(), e.getLocalizedMessage());
                Utils.alert(ZapPayHttpComm.GENERIC_ERROR_MESSAGE, context);
            }
        } else {
            Utils.alert(ZapPayHttpComm.GENERIC_ERROR_MESSAGE, context);
        }
    }





    public void hideLoading() {
        try {


            if (this.progressDialog != null && this.progressDialog.isShowing()) {
                this.progressDialog.dismiss();

            }
            this.progressDialog = null;
        } catch (Exception e) {
            Utils.consoleLog(Utils.class, e.getLocalizedMessage());
        }
    }

    public void displayLoading(Context context) {

        this.displayLoading(context,"Loading. Please wait...", ProgressDialog.STYLE_SPINNER);

    }


    public void displayLoading(Context context, String message, int style) {

        try {
            if (this.progressDialog != null && this.progressDialog.isShowing()) {
                return;
            }
            this.progressDialog = new ProgressDialog(context);
            this.progressDialog.setProgressStyle(style);
            this.progressDialog.setMessage(message);

            this.progressDialog.setIndeterminate(false);
            this.progressDialog.setCanceledOnTouchOutside(false);
            this.progressDialog.show();

        } catch (Exception e) {
            Utils.consoleLog(Utils.class, e.getLocalizedMessage());
        }
    }
}




