package com.example.user.simpleui;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by user on 2016/7/14.
 */
@ParseClassName("Drink")
public class Drink extends ParseObject{
//    private String name;
//    private int mPrice = 0;
//    private int lPrice = 0;

    public int getlPrice() {
        return getInt("lPrice");
    }

    public void setlPrice(int lPrice) {
        put("lPrice",lPrice);
    }

    public int getmPrice() {
        return getInt("mPrice");
    }

    public void setmPrice(int mPrice) {
        put("mPrice",mPrice);
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name",name);
    }

    public ParseFile getImage(){return getParseFile("image");}//image為網路上設定的欄位名稱


    int imageld;

        public JSONObject getJsonObject(){
            JSONObject jsonObject = new JSONObject();
            try {
            jsonObject.put("name", getName());
            jsonObject.put("lPrice", getlPrice());
            jsonObject.put("mPrice", getmPrice());
        } catch (JSONException e) {
            e.printStackTrace();
        }
                return jsonObject;
    }

    public static Drink newInstanceWithData(String data)
    {
        Drink drink = new Drink();
        try {
            JSONObject jsonObject = new JSONObject(data);
            drink.setName(jsonObject.getString("name"));
            drink.setmPrice(jsonObject.getInt("mPrice"));
            drink.setlPrice(jsonObject.getInt("lPrice"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return drink;
    }
    //0720

    public static ParseQuery<Drink> getQuery(){return ParseQuery.getQuery(Drink.class);}
    public static void syncDrinksFromRemote(final FindCallback<Drink> callback)
    {
        Drink.getQuery().findInBackground(new FindCallback<Drink>() {
            @Override
            public void done(final List<Drink> objects, ParseException e) {
                if(e == null) {
                    Drink.unpinAllInBackground("Drink", new DeleteCallback() {//將local端資料刪除
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Drink.pinAllInBackground("Drink", objects);//載回資料存進local
                            }
                        }
                    });
                    callback.done(objects, e);//資料放進app
                }else{
                    Drink.getQuery().fromLocalDatastore().findInBackground(callback);//如果載回資料錯誤，從local端拿資料
                }
            }
        });

    }
}
