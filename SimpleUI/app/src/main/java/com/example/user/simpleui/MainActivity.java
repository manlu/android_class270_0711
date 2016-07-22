package com.example.user.simpleui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
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
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.storetextView);
        editText = (EditText) findViewById(R.id.editText);//()強制轉型
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        listView = (ListView) findViewById(R.id.listView);
        spinner = (Spinner) findViewById(R.id.spinner);

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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order order = (Order) parent.getAdapter().getItem(position);
                gotoDetail(order);
            }
        });
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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void setupListView() {
        //item直接寫在程式碼中
        //String[] data = new String[]{"black tea","green tea","1","2","3","4","5"};
        //轉換器
        //ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);

        //0719
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);//檢查網路連線狀況
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        FindCallback<Order> callback = new FindCallback<Order>() {
            @Override
            public void done(List<Order> objects, ParseException e) {
                if (e == null) {//更新listView
                    orders = objects;
                    OrderAdapter adapter = new OrderAdapter(MainActivity.this, orders);
                    listView.setAdapter(adapter);
                }
            }
        };
        if (networkInfo == null || networkInfo.isConnected()) {//如果不可連線
            Order.getQuery().fromLocalDatastore().findInBackground(callback);//local端拿資料
        } else {
            Order.getOrdersFromRemote(callback);
        }
//        Order.getOrdersFromRemote(new FindCallback<Order>() {//可以使用生成變數callback的方法 Findcallback<Order> callback
//            @Override
//            public void done(List<Order> objects, ParseException e) {//回傳網路上的訂單
//                orders = objects;
//                OrderAdapter adapter = new OrderAdapter(MainActivity.this,orders);
//                listView.setAdapter(adapter);
//            }
//        });

        OrderAdapter adapter = new OrderAdapter(this, orders);
        listView.setAdapter(adapter);
    }

    public void setupSpinner() {
        //讀取寫在array.xml檔中的item(作業4)
        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("StoreInfo");
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                List<String> storeInfos = new ArrayList<String>();
                for(ParseObject object : objects)
                {
                    String storeInfo = object.getString("name") + "," + object.getString("address");
                    storeInfos.add(storeInfo);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, storeInfos);
                spinner.setAdapter(adapter);
            }
        });
//        String[] data  = getResources().getStringArray(R.array.storeInfos);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,data);
//        spinner.setAdapter(adapter);
    }

    public void submit(View view) {
        String text = editText.getText().toString();

        textView.setText(text);

        Order order = new Order();
        order.setNote(text);
        order.setMenuResults(menuResults);//包含所有的訂購資訊的menuResults
        order.setStoreInfo((String) spinner.getSelectedItem());

        order.pinInBackground("Order");//給class name，存在local端
        //order.saveEventually();//當使用者有網路時上傳
        order.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                setupListView();
            }
        });

        orders.add(order);

        Utils.writeFile(this, "history", order.toData() + "\n");

        //setupListView();//更新

        editText.setText("");//清空editText

        menuResults = "";//清空

    }

    public void goToMenu(View view) {
        Intent intent = new Intent();//intent企圖跑到另一Activity
        intent.setClass(this, DrinkMenuActivity.class);
        startActivityForResult(intent, REQUEST_CODE_DRINK_MENU_ACTIVITY);//從哪頁回來
    }

    public void gotoDetail(Order order) {
        Intent intent = new Intent();
        intent.setClass(this, OrderDetailActivity.class);
        intent.putExtra("note", order.getNote());
        intent.putExtra("storeInfo", order.getStoreInfo());
        intent.putExtra("menuResults", order.getMenuResults());
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_DRINK_MENU_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "訂購完成", Toast.LENGTH_SHORT).show();
                //0718
                menuResults = (data.getStringExtra("results"));
            }
        }
    }


    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Log.d("Debug", "MainActivity onStart");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.user.simpleui/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    protected void onResume() {
        super.onResume();
        Log.d("Debug", "MainActivity onResume");

    }

    protected void onPause() {
        super.onPause();
        Log.d("Debug", "MainActivity onPause");
    }

    protected void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.user.simpleui/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        Log.d("Debug", "MainActivity onStop");
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.d("Debug", "MainActivity onDestroy");
    }

    protected void onRestart() {
        super.onRestart();
        Log.d("Debug", "MainActivity onRestart");
    }


}
