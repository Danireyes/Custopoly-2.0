package com.iplusplus.custopoly;

import android.app.Application;
import android.content.Context;

import com.iplusplus.custopoly.model.ThemeHandler;


public class Custopoly extends Application {

    private static Context context;

    public void onCreate(){
        super.onCreate();
        Custopoly.context = getApplicationContext();
        ThemeHandler.getInstance(); //Create singleton instance
    }

    public static Context getAppContext() {
        return Custopoly.context;
    }
}

