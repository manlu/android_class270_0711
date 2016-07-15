package com.example.user.simpleui;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2016/7/15.
 */
public class DrinkOrder {
    Drink drink;
    int lNumber = 0;
    int mNumber = 0;
    String sugar = "正常";
    String ice = "正常";
    String note = "";

    public DrinkOrder(Drink drink)
    {//建構子
        this.drink = drink;
    }

    public String toData()
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("drink",drink.getJsonObject().toString());
            jsonObject.put("lNumber",lNumber);
            jsonObject.put("mNumber",mNumber);
            jsonObject.put("sugar",sugar);
            jsonObject.put("ice",ice);
            jsonObject.put("note",note);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static  DrinkOrder newInstanceWithData(String data)
    {//jsonObject不一定為data形式-->try-catch
        try {
            JSONObject jsonObject = new JSONObject(data);
            Drink drink = Drink.newInstanceWithData(jsonObject.getString("drink"));
            DrinkOrder drinkOrder = new DrinkOrder(drink);
            drinkOrder.lNumber = jsonObject.getInt("lNumber");
            drinkOrder.mNumber = jsonObject.getInt("mNumber");
            drinkOrder.sugar = jsonObject.getString("sugar");
            drinkOrder.ice = jsonObject.getString("ice");
            drinkOrder.note = jsonObject.getString("note");
            return drinkOrder;//成功傳回訂單
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;//沒有成功傳回null
    }
}
