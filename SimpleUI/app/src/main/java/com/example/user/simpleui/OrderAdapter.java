package com.example.user.simpleui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 2016/7/13.
 */
public class OrderAdapter extends BaseAdapter{

    //區域變數
    List<Order> orders;
    LayoutInflater layoutInflater;

    //建構子
    public OrderAdapter(Context context,List<Order> orders)
    {
        this.orders = orders;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {//幾筆
        return orders.size();
    }

    @Override
    public Object getItem(int position) {//第幾筆
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {//資料庫訂單編號
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {//顯示

        //如果之前沒有給itemview
        if(convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.listview_order_item,null);

        }


        TextView noteTextView = (TextView)convertView.findViewById(R.id.notetextView);
        TextView storeInfoTextView = (TextView)convertView.findViewById(R.id.storetextView);
        TextView drinkNameTextView = (TextView)convertView.findViewById(R.id.DrinkNamwtextView);

        Order order = orders.get(position);
        noteTextView.setText(order.note);
        storeInfoTextView.setText(order.storeInfo);
        drinkNameTextView.setText(order.drinkName);

        return convertView;
    }
}
