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

        public int totalNumber()
        {//顯示總杯數
                if(menuResults != null || menuResults.equals(""))
                {
                        return 0;
                }

                try {
                        JSONArray jsonArray = new JSONArray(menuResults);
                        int totalNumber = 0;
                        for(int i=0;i<jsonArray.length();i++)
                        {
                                String data = jsonArray.getString(i);//拿出訂單裡的每種飲料
                                DrinkOrder drinkOrder = DrinkOrder.newInstanceWithData(data);
                                totalNumber += drinkOrder.lNumber+drinkOrder.mNumber;
                        }
                        return totalNumber;
                } catch (JSONException e) {
                        e.printStackTrace();
                }
                return 0;//錯誤回傳0
        }
}
