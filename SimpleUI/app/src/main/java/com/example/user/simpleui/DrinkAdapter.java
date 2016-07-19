package com.example.user.simpleui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/7/14.
 */
public class DrinkAdapter extends BaseAdapter {

    LayoutInflater inflater;
    List<Drink> drinkList = new ArrayList<>();

    public DrinkAdapter(Context context,List<Drink> drinks)
    {
        drinkList = drinks;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return drinkList.size();
    }

    @Override
    public Object getItem(int position) {
        return drinkList.get(position);//第幾項
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.listview_drink_item,null);
            holder = new Holder();
            holder.drinkName = (TextView)convertView.findViewById(R.id.drinkNameTextView);
            holder.imageView = (ImageView)convertView.findViewById(R.id.imageView);
            holder.mPriceTextView = (TextView)convertView.findViewById(R.id.mPriceTextView);
            holder.lPriceTextView = (TextView)convertView.findViewById(R.id.lPriceTextView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (Holder)convertView.getTag();
        }

        //將設定好的名字圖片價錢丟進相對位置
        Drink drink = drinkList.get(position);
        holder.drinkName.setText(drink.getName());
        holder.mPriceTextView.setText(String.valueOf(drink.getmPrice());
        holder.lPriceTextView.setText(String.valueOf(drink.getlPrice());
        //原本設定為int顯示時須強制轉型成String(TextView)
        holder.imageView.setImageResource(drink.imageld);

        return convertView;
    }

    class Holder{
        ImageView imageView;
        TextView mPriceTextView;
        TextView lPriceTextView;
        TextView drinkName;
    }

    //最後記得DrinkMenuActivity的setupDrinkMenuListView()
}
