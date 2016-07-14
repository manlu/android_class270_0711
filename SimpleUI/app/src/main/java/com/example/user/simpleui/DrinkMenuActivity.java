package com.example.user.simpleui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class DrinkMenuActivity extends AppCompatActivity {

    TextView totalTextView;
    ListView drinkMenuListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu);
        Log.d("Debug","DrinkMenuActivity OnCreate");

        totalTextView = (TextView)findViewById(R.id.tatalTextView);
        drinkMenuListView = (ListView)findViewById(R.id.drinkMenuListView);

    }


    protected void onStart(){
        super.onStart();
        Log.d("Debug", "DrinkMenuActivity onStart");
    }

    protected void onResume(){
        super.onResume();
        Log.d("Debug", "DrinkMenuActivity onResume");

    }

    protected void onPause(){
        super.onPause();
        Log.d("Debug","DrinkMenuActivity onPause");
    }

    protected void onStop(){
        super.onStop();
        Log.d("Debug","DrinkMenuActivity onStop");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.d("Debug","DrinkMenuActivity onDestroy");
    }

    protected void onRestart(){
        super.onRestart();
        Log.d("Debug", "DrinkMenuActivity onRestart");
    }
}
