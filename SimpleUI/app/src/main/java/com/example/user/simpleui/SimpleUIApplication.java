package com.example.user.simpleui;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by user on 2016/7/19.
 */
public class SimpleUIApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Order.class);//認得Order物件，Order物件上傳
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("t63Ab2tWqqfQNVTbpDZuRpHdJ06tYxtDkfzk7qws")
                .server("https://parseapi.back4app.com/")
                .clientKey("YUIKKpqWDxjr8lj4lcMq0l5WkYYeUIL4OFURycps")
                .build());
    }
}