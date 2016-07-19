package com.example.user.simpleui;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetFileCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_CODE_DRINK_MENU_ACTIVITY = 0;

    TextView textView;
    EditText editText;
    RadioGroup radioGroup;
    ListView listView;
    Spinner spinner;

    String selectedTea = "black tea";

    //0718
    String menuResults = "";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;//更改資料


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

        //0718
        sharedPreferences = getSharedPreferences("setting", MODE_APPEND);//MODE_APPEND資料累加不覆蓋
        editor = sharedPreferences.edit();//有權限更改資料

        editText.setText(sharedPreferences.getString("editText", ""));

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String text = editText.getText().toString();
                editor.putString("editText", text);
                editor.commit();
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)//ACTION_DOWN是偵測按下的那個瞬間
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
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                selectedTea = radioButton.getText().toString();
            }
        });

//        String history = Utils.readFile(this,"history");//讀出之前的history
//        String[] datas = history.split("\n");//用換行來分割新的訂單
//        for(String data : datas)
//        {
//            Order order = Order.newInstanceWithData(data);
//            if(order != null) {
//                orders.add(order);
//            }
//        }
        setupListView();
        setupSpinner();

//        //0719
//        ParseObject parseObject = new ParseObject("Test");//寫進test這個資料夾 back{4}app
//        parseObject.put("foo", "bar");
//        parseObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null)
//                    Toast.makeText(MainActivity.this, "上傳成功", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Test");
//        query.findInBackground(new FindCallback<ParseObject>() {
//            @Override
//            public void done(List<ParseObject> objects, ParseException e) {
//                if(e == null)
//                {
//                    for (ParseObject object : objects)
//                    {
//                        Toast.makeText(MainActivity.this, object.getString("foo"), Toast.LENGTH_LONG).show();
//                    }
//                }
//
//            }
//        });




        Log.d("Debug", "MainActivity OnCreate");

    }

    public void setupListView()
    {
        //item直接寫在程式碼中
        //String[] data = new String[]{"black tea","green tea","1","2","3","4","5"};
        //轉換器
        //ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        //0719
        Order.getOrdersFromRemote(new FindCallback<Order>() {
            @Override
            public void done(List<Order> objects, ParseException e) {//回傳網路上的訂單
                orders = objects;
                OrderAdapter adapter = new OrderAdapter(MainActivity.this,orders);
                listView.setAdapter(adapter);
            }
        });
        
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
        order.setNote(text);
        order.setMenuResults(menuResults);//包含所有的訂購資訊的menuResults
        order.setStoreInfo((String) spinner.getSelectedItem());
        order.saveInBackground();//上傳

        orders.add(order);

        Utils.writeFile(this, "history", order.toData() + "\n");

        setupListView();//更新

        editText.setText("");//清空editText

        menuResults = "";//清空

    }

    public void goToMenu(View view){
        Intent intent = new Intent();//intent企圖跑到另一Activity
        intent.setClass(this,DrinkMenuActivity.class);
        startActivityForResult(intent,REQUEST_CODE_DRINK_MENU_ACTIVITY);//從哪頁回來
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_DRINK_MENU_ACTIVITY)
        {
            if(resultCode == RESULT_OK)
            {
                Toast.makeText(this,"訂購完成",Toast.LENGTH_SHORT).show();
                //0718
                menuResults = (data.getStringExtra("results"));
            }
        }
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
