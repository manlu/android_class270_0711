package com.example.user.simpleui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by user on 2016/7/18.
 */
public class Utils {
    //工具0718
    public static void writeFile(Context context, String fileName, String content) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_APPEND);

            fileOutputStream.write(content.getBytes());//用bytes存
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String readFile(Context context, String fileName) {
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            byte[] buffer = new byte[2048];//創造buffer字串

            fileInputStream.read(buffer, 0, buffer.length);
            fileInputStream.close();
            return new String(buffer);//buffer(byte)會自動轉為String
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;//發生錯誤
    }
    public static byte[] urlToBytes(String urlString)
    {
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[]  bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1)
            {
                byteArrayOutputStream.write(bytes, 0, len);
            }
            return byteArrayOutputStream.toByteArray();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double[] getLatLngFromGoogleMapAPI(String address)
    {
        try {
            address = URLEncoder.encode(address, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String apiURL = "http://maps.google.com/maps/api/geocode/json?address=" + address;

        byte[] data = Utils.urlToBytes(apiURL);

        if(data == null)
            return null;

        String result = new String(data);

        try {
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.getString("status").equals("OK"))
            {
                JSONObject location = jsonObject.getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONObject("location");//搭配網站資料服用(綠色的字)
                double lat = location.getDouble("lat");
                double lng = location.getDouble("lng");
                return new double[]{lat, lng};
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Bitmap getStaticMap(double[] latlng)
    {
        String center = String.valueOf(latlng[0])+","+String.valueOf(latlng[1]);
        String staticMspURL = "http://map.google.com/maps/api/staticmap?\"+center+\"&size=640x480&zoom=17";//staticmap為地圖圖片格式，size圖大小，zoom放大17倍

        byte[] bytes = Utils.urlToBytes(staticMspURL);
        if(bytes != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            return bitmap;
        }
        return null;
    }
}

