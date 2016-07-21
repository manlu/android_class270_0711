package com.example.user.simpleui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.logging.Handler;

public class OrderDetailActivity extends AppCompatActivity implements GeoCodingTask.GeoCodingResponse{

    GoogleMap googleMap;

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
        ImageView staticMapImageView = (ImageView)findViewById(R.id.googleMapIimageView);
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
        //0721
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.mapFragment);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;
                (new GeoCodingTask(OrderDetailActivity.this)).execute("台北市大安區羅斯福路四段一號");
            }
        });


    }
    //0721
    @Override
    public void reponseWithGeoCodingResults(LatLng latlng) {
        if(googleMap != null){
            final CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 17);
            //googleMap.animateCamera(cameraUpdate);
            MarkerOptions markerOptions = new MarkerOptions().position(latlng).title("台灣大學").snippet("hello google map");
            googleMap.addMarker(markerOptions);

            //使用者點選mark
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    CameraUpdate cp = CameraUpdateFactory.newLatLngZoom(marker.getPosition(),21);//放大21倍
                    googleMap.animateCamera(cp);//animate移動動畫
                    return false;
                }
            });
            googleMap.moveCamera(cameraUpdate);//動畫取消
        }
    }

    //網路連線，執行緒
//    public static class GeoCodingTask extends AsyncTask<String,Void,Bitmap>{
//        WeakReference<ImageView> imageViewWeakReference;
//        @Override
//        protected Bitmap doInBackground(String... params) {//做網路連線
//            String address = params[0];
//            double[] latlng = Utils.getLatLngFromGoogleMapAPI(address);
//            return Utils.getStaticMap(latlng);
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {//改UI上的元件
//            super.onPostExecute(bitmap);
//            if(imageViewWeakReference.get() != null && bitmap != null){
//                ImageView imageView = imageViewWeakReference.get();
//                imageView.setImageBitmap(bitmap);
//            }
//        }
//
//        public GeoCodingTask(OrderDetailActivity imageView){
//            this.imageViewWeakReference = new WeakReference<ImageView>(imageView);
//        }
//    }
}
