package com.example.user.simpleui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DrinkMenuActivity extends AppCompatActivity {

    TextView totalTextView;
    ListView drinkMenuListView;

    String[] names = {"冬瓜紅茶","玫瑰鹽奶蓋紅茶","珍珠紅茶拿鐵","紅茶拿鐵"};
    int[] mPrices = {25,35,45,35};
    int[] lPrices = {35,45,55,45};
    int[] imagelds = {R.drawable.drink1,R.drawable.drink2,R.drawable.drink3,R.drawable.drink4};

    List<Drink> drinks = new ArrayList<>();
    List<Drink> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu);
        Log.d("Debug","DrinkMenuActivity OnCreate");
        setData();

        totalTextView = (TextView)findViewById(R.id.tatalTextView);
        drinkMenuListView = (ListView)findViewById(R.id.drinkMenuListView);

        setupDrinkMenuListView();

    }

    private void setData()
    {
        for(int i = 0 ; i < names.length ; i++)
        {
            Drink drink = new Drink();
            drink.name = names[i];
            drink.mPrice = mPrices[i];
            drink.lPrice = lPrices[i];
            drink.imageld = imagelds[i];
            drinks.add(drink);
        }
    }

    private void setupDrinkMenuListView()
    {
        DrinkAdapter adapter = new DrinkAdapter(this,drinks);
        drinkMenuListView.setAdapter(adapter);

        drinkMenuListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //使用者按下button後觸發事件
        //drinkMenuListView.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //public void onClick(View v) {
        //        DrinkAdapter drinkAdapter = (DrinkAdapter)parent.getAdapter();
        //        Drink drink = drinkAdapter.getItem(position);
        //        orders.add(drink);
        //        updateTotal();
        //    }
        //});
    }

    public void updateTotal(){
        int total = 0;
        for(Drink drink: orders){
            total += drink.mPrice;
        }//中杯價錢加總

        totalTextView.setText(String.valueOf(total));
    }

    public void done(View view)
    {
        Intent intent = new Intent();

        //資料格式：一個key值一個value -> mPrice:35 (JASON)

        JSONArray jsonArray = new JSONArray();

        for(Drink drink : orders)
        {
            JSONObject jsonObject = drink.getJasonObject();
            jsonArray.put(jsonObject);
        }

        intent.putExtra("results",jsonArray.toString());

        setResult(RESULT_OK,intent);//請intent帶回資訊
        finish();
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
