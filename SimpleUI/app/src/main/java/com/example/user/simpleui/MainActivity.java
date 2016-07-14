package com.example.user.simpleui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    RadioGroup radioGroup;
    ListView listView;
    Spinner spinner;

    String selectedTea = "black tea";

    List<Order> orders = new ArrayList<>();//等等要放進訂單的位置


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.storetextView);
        editText = (EditText)findViewById(R.id.editText);//()強制轉型
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        listView = (ListView)findViewById(R.id.listView);
        spinner = (Spinner)findViewById(R.id.spinner);

        editText.setOnKeyListener(new View.OnKeyListener() {//當使用者按下enter監聽
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) { //onkey當使用者打鍵盤後啟用的事件
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)//ACTION_DOWN是偵測按下的那個瞬間
                {
                    submit(v);//呼叫submit
                    return true;//攔截，注解掉後還是會空一行
                }
                return false;
            }
        });

        //radiogroup監聽，如果勾選項目改變觸發事件
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
                selectedTea = radioButton.getText().toString();
            }
        });

        setupListView();
        setupSpinner();

        Log.d("Debug","MainActivity OnCreate");

    }

    public void setupListView()
    {
        //item直接寫在程式碼中
        //String[] data = new String[]{"black tea","green tea","1","2","3","4","5"};
        //轉換器
        //ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        OrderAdapter adapter = new OrderAdapter(this,orders);
        listView.setAdapter(adapter);
    }

    public void setupSpinner()
    {
        //讀取寫在array.xml檔中的item
        String[] data  = getResources().getStringArray(R.array.storeInfos);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,data);
        spinner.setAdapter(adapter);
    }

    public void submit(View view)
    {
        String text = editText.getText().toString();

        textView.setText(text);

        Order order = new Order();
        order.note = text;
        order.drinkName = selectedTea;
        order.storeInfo = (String)spinner.getSelectedItem();

        orders.add(order);

        setupListView();//更新

        editText.setText("");//清空editText

    }

    public void goToMenu(View view){
        Intent intent = new Intent();//intent企圖跑到另一Activity
        intent.setClass(this,DrinkMenuActivity.class);
        startActivity(intent);
    }

    protected void onStart(){
        super.onStart();
        Log.d("Debug", "MainActivity onStart");
    }

    protected void onResume(){
        super.onResume();
        Log.d("Debug", "MainActivity onResume");

    }

    protected void onPause(){
        super.onPause();
        Log.d("Debug","MainActivity onPause");
    }

    protected void onStop(){
        super.onStop();
        Log.d("Debug","MainActivity onStop");
    }

    protected void onDestroy(){
        super.onDestroy();
        Log.d("Debug","MainActivity onDestroy");
    }

    protected void onRestart(){
        super.onRestart();
        Log.d("Debug","MainActivity onRestart");
    }


}
