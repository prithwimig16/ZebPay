package com.example.prithwi.zebpay.utils;

import android.graphics.Typeface;

/**
 * Created by Prithwi on 07/02/18.
 */

public class Config {

    public String STARTUP_API =//"http://appdev.clamhub.com/TablespaceStartup/master";//dev

    "http://www.nobroker.in/api/v1/property/filter/region/ChIJLfyY2E4UrjsRVq4AjI7zgRY/?lat_lng=12.9279232,77.6271078&rent=0,500000&travelTime=30&pageNo=1";//prod





    public boolean debugMode = true;



    public static Typeface OpenSans_Bold;
    public static Typeface OpenSans_Regular;
    public static Typeface OpenSans_Light;

    public boolean isInternetAvailable;

    private static Config _instance = null;

    public static Config getSharedInstance()
    {
        if(_instance==null)
        {
            _instance = new Config();
            _instance.isInternetAvailable = true;



        }
        return _instance;
    }




}





