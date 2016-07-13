package com.example.user.simpleui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    RadioGroup radioGroup;
    ListView listView;
    Spinner spinner;

    String selectedTea = "black tea";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);
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
    }

    public void setupListView()
    {
        //item直接寫在程式碼中
        String[] data = new String[]{"black tea","green tea","1","2","3","4","5"};
        //轉換器
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
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

        editText.setText("");//清空editText

    }

}
