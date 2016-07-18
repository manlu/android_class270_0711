package com.example.user.simpleui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DrinkMenuActivity extends AppCompatActivity implements DrinkOrderDialog.OnDrinkOrderListener{

    TextView totalTextView;
    ListView drinkMenuListView;

    String[] names = {"冬瓜紅茶","玫瑰鹽奶蓋紅茶","珍珠紅茶拿鐵","紅茶拿鐵"};
    int[] mPrices = {25,35,45,35};
    int[] lPrices = {35,45,55,45};
    int[] imagelds = {R.drawable.drink1,R.drawable.drink2,R.drawable.drink3,R.drawable.drink4};

    List<Drink> drinks = new ArrayList<>();
    List<DrinkOrder> orders = new ArrayList<>();

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

        //使用者按下button後觸發事件
        drinkMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//一種interface，此物件順便將他實作
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DrinkAdapter drinkAdapter = (DrinkAdapter) parent.getAdapter();
                Drink drink = (Drink) drinkAdapter.getItem(position);
                showDrinkOrderDialog(drink);
            }
        });


    }

    public void showDrinkOrderDialog(Drink drink)
    {
        DrinkOrder drinkOrder = new DrinkOrder(drink);
        for(DrinkOrder order : orders)
        {
            if(order.drink.name.equals(drink.name))
            {//傳近來的飲料名稱=現有訂單的飲料名稱
                drinkOrder = order;//覆蓋
                break;
            }
        }
        //new出訂單


        FragmentManager fragmentManager = getFragmentManager();

        FragmentTransaction ft = fragmentManager.beginTransaction();

        //將layout切換
        DrinkOrderDialog dialog = DrinkOrderDialog.newInstance(drinkOrder);
        Fragment prev = getFragmentManager().findFragmentByTag("DrinkOrderDialog");
        if(prev != null){
            ft.remove(prev);
        }
        //ft.replace(R.id.root,dialog);//DrinkMenuActivity和DrinkOrderDialog import的Fragment資料夾不相同-->改
        ft.addToBackStack(null);//可以支援back鍵
        //ft.commit();

        dialog.show(ft,"DrinkOrderDialog");

    }

    public void updateTotal(){
        int total = 0;
        for(DrinkOrder order: orders){
            total += order.mNumber*order.drink.mPrice+order.lNumber*order.drink.lPrice;
        }//中杯價錢加總

        totalTextView.setText(String.valueOf(total));
    }

    public void done(View view)
    {
        Intent intent = new Intent();

        //資料格式：一個key值一個value -> mPrice:35 (JASON)

        JSONArray jsonArray = new JSONArray();

        for(DrinkOrder order : orders)
        {
            String data = order.toData();
            jsonArray.put(data);
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

    @Override
    public void onDrinkOrderFinish(DrinkOrder drinkOrder) {
        Boolean flag = false;
        for(int index = 0 ; index < orders.size() ; index++)
        {
            if(orders.get(index).drink.name.equals(drinkOrder.drink.name))
            {//看訂單中是否已經有這種飲料
                orders.set(index,drinkOrder);
                flag = true;
                break;
            }
        }

        //沒有就新增飲料
        if(!flag)
            orders.add(drinkOrder);

        updateTotal();
    }
}
