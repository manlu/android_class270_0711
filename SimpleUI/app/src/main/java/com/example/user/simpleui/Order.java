package com.example.user.simpleui;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by user on 2016/7/13.
 */

@ParseClassName("Order")//server才會認得這個Order物件名稱

public class Order extends ParseObject {
//        private String note;
//        private String menuResults;
//        private String storeInfo;

        public String getNote(){
                return getString("note");
        }

        public  void setNote(String note){
                put("note",note);
        }

        public void setMenuResults(String menuResults) {
                put("menuResult",menuResults);
        }

        public String getMenuResults() {
                String menuResults = getString("menuResults");
                if(menuResults == null)
                {
                        return "";
                }
                return menuResults;
        }

        public void setStoreInfo(String storeInfo) {
                put("storeInfo",storeInfo);
        }

        public String getStoreInfo() {
                return getString("StoreInfo");
        }

        public String toData()
        {//0718轉換資料型態放進JSONObject
                JSONObject jsonObject = new JSONObject();
                try {
                        jsonObject.put("note",getNote());
                        jsonObject.put("menuResults",getMenuResults());
                        jsonObject.put("storeInfo",getStoreInfo());
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
                        order.setNote(jsonObject.getString("note"));
                        order.setMenuResults(jsonObject.getString("menuResults"));
                        order.setStoreInfo(jsonObject.getString("storeInfo"));
                        return order;
                } catch (JSONException e) {
                        e.printStackTrace();
                }

                return null;
        }

        public int totalNumber()
        {
                if(getMenuResults() == null || getMenuResults().equals(""))
                {
                        return 0;
                }

                try {
                        JSONArray jsonArray = new JSONArray(getMenuResults());
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

        public static void getOrdersFromRemote(final FindCallback<Order>callback)
        {//下載訂單
                getQuery().findInBackground(new FindCallback<Order>() {
                        @Override
                        public void done(List<Order> objects, ParseException e) {
                                if(e == null){
                                        Order.pinAllInBackground("Order", objects);//把載回的資料欑在local端
                                }else{
                                        Order.getQuery().fromLocalDatastore().findInBackground(callback);
                                }
                                callback.done(objects, e);//在執行使用者新增訂單
                        }
                });
        }

        public static ParseQuery<Order> getQuery()
        {
                return ParseQuery.getQuery(Order.class);
        }
}