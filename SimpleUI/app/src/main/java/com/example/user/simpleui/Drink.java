package com.example.user.simpleui;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

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

}
