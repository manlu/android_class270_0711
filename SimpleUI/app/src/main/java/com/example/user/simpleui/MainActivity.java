package com.example.user.simpleui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    RadioGroup radioGroup;

    String selectedSex = "男";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);//()強制轉型
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);

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
                if(checkedId == R.id.maleRadioButton){
                    selectedSex = "男";
                }
                else if(checkedId == R.id.femaleRadioButton){
                    selectedSex = "女";
                }
            }
        });
    }

    public void submit(View view)
    {
        String text = editText.getText().toString();

        text = text + "  性別：" + selectedSex;
        textView.setText(text);

        editText.setText("");//清空editText

    }

}
