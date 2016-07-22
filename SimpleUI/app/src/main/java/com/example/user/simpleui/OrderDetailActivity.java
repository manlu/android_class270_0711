package com.example.user.simpleui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.Manifest;
import java.util.logging.Handler;

public class OrderDetailActivity extends AppCompatActivity implements GeoCodingTask.GeoCodingResponse, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener, RoutingListener {

    final static int ACCESS_FINE_LOCATION_REQUEST_CODE = 1;
    GoogleMap googleMap;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    Marker marker;
    List<Polyline> polylines = new ArrayList<>();
    LatLng storeLocation;
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
        if (googleMap != null)
        {
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlng, 17);
//            googleMap.animateCamera(cameraUpdate);
            MarkerOptions markerOptions = new MarkerOptions().position(latlng).title("台灣大學").snippet("Hello Google Map");
            googleMap.addMarker(markerOptions);

            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    CameraUpdate cp = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 21);
                    googleMap.moveCamera(cp);
                    return false;
                }
            });

            //googleMap.moveCamera(cameraUpdate);
            storeLocation = latlng;
            createGoogleAPIClient();
        }
    }
    //0722
    private void createGoogleAPIClient(){
        if(googleApiClient == null){//是否存在
            googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_REQUEST_CODE);
            }
            return;
        }

        createLocationRequest();
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        LatLng start = new LatLng(25.0186348,121.5398379);
        if(location != null){//拿出目前的經緯度
            start = new LatLng(location.getLatitude(),location.getLongitude());
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 17));

        Routing routing = new Routing.Builder().travelMode(AbstractRouting.TravelMode.WALKING)
                .waypoints(start,storeLocation).withListener(this).build();
        routing.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ACCESS_FINE_LOCATION_REQUEST_CODE == requestCode){
            if(permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)  && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                onConnected(null);
            }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void createLocationRequest(){
        if(locationRequest == null)
        {
            locationRequest = new LocationRequest();
            locationRequest.setInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        LatLng currentLatLng = new LatLng(location.getLatitude(),location.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,17));
        if (marker == null) {
            MarkerOptions markerOptions = new MarkerOptions().position(currentLatLng).title("台灣大學").snippet("Hello Google Map");
            marker = googleMap.addMarker(markerOptions);
        }
        else
        {
            marker.setPosition(currentLatLng);
        }
    }

    protected void onStart(){
        super.onStart();
        if(googleApiClient != null){
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(googleApiClient != null){
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> routs, int i) {
        if(polylines.size() > 0){//清空地圖上原本的路線
            for(Polyline polyline : polylines){
                polyline.remove();
            }
            polylines.clear();
        }
        //增加新路線
        for(int index=0;index<routs.size();index++){
            List<LatLng> points = routs.get(index).getPoints();//拿出路線上的所有轉彎點
            //畫出線段(設定)
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.addAll(points);
            polylineOptions.color(Color.GREEN);
            polylineOptions.width(10);//線段寬度
            //畫出線斷(顯示)
            Polyline polyline = googleMap.addPolyline(polylineOptions);
            polylines.add(polyline);
        }
    }

    @Override
    public void onRoutingCancelled() {

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
