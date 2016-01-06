package com.example.act_sample;

import android.app.Application;

import com.org.appcrashtracker.ACT;

/**
 * Created by Kosh on 05/01/16 11:08 AM
 */
public class App extends Application {

    @Override public void onCreate() {
        super.onCreate();
        ACT.init(this, SecondActivity.class);
    }
}
