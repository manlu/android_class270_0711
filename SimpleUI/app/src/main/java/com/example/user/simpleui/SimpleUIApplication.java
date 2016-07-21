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
        ParseObject.registerSubclass(Drink.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("76ee57f8e5f8bd628cc9586e93d428d5")
                .server("http://parseserver-ps662-env.us-east-1.elasticbeanstalk.com/parse/")
                //.clientKey("YUIKKpqWDxjr8lj4lcMq0l5WkYYeUIL4OFURycps")
                .enableLocalDataStore()
                .build());
    }
    //.enableLocalDataStore()開啟local端的database功能
}