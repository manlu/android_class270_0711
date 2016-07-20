package com.example.user.simpleui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.logging.Handler;

public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Intent intent = getIntent();//拿到intent所帶來的資訊
        String note = intent.getStringExtra("note");
        String menuResults = intent.getStringExtra("menuResults");
        String storeInfo = intent.getStringExtra("storeInfo");

        TextView noteTextView = (TextView)findViewById(R.id.noteTextView);
        TextView menuResultsTextView = (TextView)findViewById(R.id.menuResultsTextView);
        TextView storeInfoTextView = (TextView)findViewById(R.id.storeInfoTextView);

        noteTextView.setText(note);
        storeInfoTextView.setText(storeInfo);

        List<String> menuResultList = Order.getMenuResultList(menuResults);

        String text = "";
        if(menuResultList != null) {
            for (String menuResult : menuResultList) {//串好字串們
                text += menuResult + "\n";
            }
        }
        menuResultsTextView.setText(text);//顯示
    }
    //網路連線，執行緒
    public static class GeoCodingTask extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {//做網路連線
            String address = params[0];
            double[] latlng = Utils.getLatLngFromGoogleMapAPI(address);
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {//改UI上的原件
            super.onPostExecute(bitmap);
        }
    }
}
