package com.example.user.simpleui;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 2016/7/13.
 */
public class Order {
        String note;
        String menuResults;
        String storeInfo;

        public String toData()
        {//0718轉換資料型態放進JSONObject
                JSONObject jsonObject = new JSONObject();
                try {
                        jsonObject.put("note",note);
                        jsonObject.put("menuResults",menuResults);
                        jsonObject.put("storeInfo",storeInfo);
                } catch (JSONException e) {
                        e.printStackTrace();
                }
               return jsonObject.toString();
        }

        public static Order newInstanceWithData(String data)
        {//0718再從JSONObject裡拿出
                JSONObject jsonObject = new JSONObject();
                Order order = new Order();
                try {
                        order.note = jsonObject.getString("note");
                        order.menuResults = jsonObject.getString("menuResults");
                        order.storeInfo = jsonObject.getString("storeInfo");
                        return order;
                } catch (JSONException e) {
                        e.printStackTrace();
                }

                return null;
        }

        public int totalNumber()
        {
                if(menuResults == null || menuResults.equals(""))
                {
                        return 0;
                }

                try {
                        JSONArray jsonArray = new JSONArray(menuResults);
                        int totalNumber = 0;
                        for (int i = 0; i < jsonArray.length() ; i++)
                        {
                                String data = jsonArray.getString(i);
                                DrinkOrder drinkOrder = DrinkOrder.newInstanceWithData(data);
                                totalNumber += drinkOrder.lNumber + drinkOrder.mNumber;
                        }
                        return totalNumber;
                } catch (JSONException e) {
                        e.printStackTrace();
                }

                return 0;
        }
}