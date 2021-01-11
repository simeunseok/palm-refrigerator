package com.example.oasisproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

// 재료 리스트뷰 어뎁터
public class ItemAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<ItemData> itemdata;

    public ItemAdapter(Context context, ArrayList<ItemData> itemdata){
        mContext = context;
        this.itemdata = itemdata;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return itemdata.size();
    }

    @Override
    public Object getItem(int position) {
        return itemdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.inputitemset,null);

        TextView textName = (TextView) view.findViewById(R.id.textName);
        TextView textBuydate = (TextView) view.findViewById(R.id.textBuydate);
        TextView textUntil = (TextView) view.findViewById(R.id.textNumber);
        TextView textMoney = (TextView) view.findViewById(R.id.textMoney);

        textName.setText(itemdata.get(position).getName());
        textBuydate.setText("" + itemdata.get(position).getYear() + "/" + itemdata.get(position).getMonth() + "/" + itemdata.get(position).getDay());
        textUntil.setText("" + itemdata.get(position).getNumber());
        textMoney.setText("" + itemdata.get(position).getMoney());

        return view;
    }
}
