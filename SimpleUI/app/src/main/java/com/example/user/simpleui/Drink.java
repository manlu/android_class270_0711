package com.example.user.simpleui;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2016/7/14.
 */
public class Drink {
    String name;
    int mPrice = 0;
    int lPrice = 0;
    int imageld;

        public JSONObject getJsonObject(){
            JSONObject jsonObject = new JSONObject();
            try {
            jsonObject.put("name", name);
            jsonObject.put("lPrice", lPrice);
            jsonObject.put("mPrice", mPrice);
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
            drink.name = jsonObject.getString("name");
            drink.mPrice = jsonObject.getInt("mPrice");
            drink.lPrice = jsonObject.getInt("lPrice");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return drink;
    }

}
