package com.example.user.simpleui;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by user on 2016/8/2.
 */
public class SimpleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                        .applicationId("Qp4uwXEM2nYXFjjelZWPVILGzRlQtTey4fVglYDk")
                        .server("https://parseapi.back4app.com/")
                        .clientKey("DZNGxQrxIrIyFdqs1Oz0QW7R6oZduKWIchvPFDA6")
                        .build());
    }
}
