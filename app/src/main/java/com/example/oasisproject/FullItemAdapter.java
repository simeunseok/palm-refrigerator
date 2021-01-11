package com.example.oasisproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// 전체보기 리스트 어뎁터
public class FullItemAdapter extends BaseAdapter {

    private Context mContext = null;
    private LayoutInflater mLayoutInflater = null;
    private ArrayList<FullItemData> itemdata;
    DbOpenHelper dbHelper;
    SQLiteDatabase db;


    public FullItemAdapter(Context context, ArrayList<FullItemData> itemdata){
        mContext = context;
        this.itemdata =itemdata;
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
        View view = mLayoutInflater.inflate(R.layout.showitemset, null);

        TextView textName = (TextView)view.findViewById(R.id.textItemName);;
        TextView textType  = (TextView)view.findViewById(R.id.textItemType);
        TextView textNumber = (TextView)view.findViewById(R.id.textItemNumber);
        TextView textUntil  = (TextView)view.findViewById(R.id.textItemUntil);
        ImageView warningUntil = (ImageView)view.findViewById(R.id.warningUntil);

        textName.setText(itemdata.get(position).getName());
        textType.setText(itemdata.get(position).getType());
        textNumber.setText(""+itemdata.get(position).getNumber());
        textUntil.setText(itemdata.get(position).getUntil());

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = simpleDate.format(mDate);
        String splitNow[] = getTime.split("-");

        dbHelper = new DbOpenHelper(mContext);
        db =dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT until_year, until_month, until_day from buyItems where name = '" + itemdata.get(position).getName() + "'", null);

        int until_year = 0, until_month = 0, until_day = 0;

        while(cursor.moveToNext()){
            until_year = cursor.getInt(0);
            until_month = cursor.getInt(1);
            until_day = cursor.getInt(2);
        }


        int lastYear = until_year - Integer.parseInt(splitNow[0]);
        int lastMonth = until_month - Integer.parseInt(splitNow[1]);
        int lastDay = until_day - Integer.parseInt(splitNow[2]);

        if(lastYear == 0 && lastMonth == 0 && lastDay <= 3){
            warningUntil.setImageResource(R.drawable.redcircle);
        }
        else if (lastYear == 0 && lastMonth == 0 && lastDay <= 7){
            warningUntil.setImageResource(R.drawable.yellowcircle);
        }
        else{
            warningUntil.setImageResource(R.drawable.greencircle);
        }

        return view;
    }
}
