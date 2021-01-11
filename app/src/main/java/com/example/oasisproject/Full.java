package com.example.oasisproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class Full extends AppCompatActivity {
    private ArrayList<FullItemData> Fullitemdata;
    DbOpenHelper dbHelper;
    SQLiteDatabase db;
    final SortName sortName = new SortName();
    final SortUntil sortUntil = new SortUntil();
    final SortType sortType = new SortType();
    ItemInfoDialog itemInfoDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullitemlist);


        final Spinner sortStandard = (Spinner) findViewById(R.id.spinnerSortStandard);
        ArrayAdapter sortAdapter = ArrayAdapter.createFromResource(getBaseContext(), R.array.sort_standard, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortStandard.setAdapter(sortAdapter);

        dbHelper = new DbOpenHelper(this);
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(ItemDataBases.SELECT, null);

        Fullitemdata = new ArrayList<FullItemData>();

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = simpleDate.format(mDate);
        String split[] = getTime.split("-");

        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);

        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            int number = cursor.getInt(2);
            int until_year = cursor.getInt(3);
            int until_month = cursor.getInt(4);
            int until_day = cursor.getInt(5);
            String until = "" + (until_year-year) + "년" + (until_month-month) + "개월" + (until_day- day) + "일 남음";
            String type = cursor.getString(9);

            Fullitemdata.add(new FullItemData(name, number, until, type));
        }

        Collections.sort(Fullitemdata, sortName);

        final ListView ListViewItem = (ListView) findViewById(R.id.listviewFull);
        final FullItemAdapter fullItemAdapter = new FullItemAdapter(getBaseContext(), Fullitemdata);

        ListViewItem.setAdapter(fullItemAdapter);

        ListViewItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FullItemData text = (FullItemData) parent.getAdapter().getItem(position);

                itemInfoDialog = new ItemInfoDialog(Full.this, OnButtonDelete ,OnButtonYes);
                itemInfoDialog.setCanceledOnTouchOutside(true);
                itemInfoDialog.setCancelable(true);
                itemInfoDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                itemInfoDialog.show();

                TextView name = (TextView) itemInfoDialog.findViewById(R.id.textViewName);
                TextView type = (TextView) itemInfoDialog.findViewById((R.id.textViewType));
                TextView until = (TextView) itemInfoDialog.findViewById(R.id.textViewUntilDate);
                TextView buy = (TextView) itemInfoDialog.findViewById(R.id.textViewBuyDate);

                db = dbHelper.getReadableDatabase();

                Cursor cursor = db.rawQuery("SELECT until_year, until_month, until_day, year, month, day, type from buyItems where name = '" + text.getName() + "'", null);

                String untilDate = "", buyDate = "", typeString = "";

                while(cursor.moveToNext()){
                    untilDate = cursor.getInt(0) + "-" + cursor.getInt(1) + "-" + cursor.getInt(2);
                    buyDate = cursor.getInt(3) + "-" + cursor.getInt(4) + "-" + cursor.getInt(5);
                    typeString = cursor.getString(6);
                }

                name.setText(text.getName());
                type.setText(typeString);
                until.setText(untilDate);
                buy.setText(buyDate);

            }

            private View.OnClickListener OnButtonDelete = new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    TextView tv = (TextView) itemInfoDialog.findViewById(R.id.textViewName);
                    db = dbHelper.getWritableDatabase();

                    db.execSQL("DELETE FROM buyItems WHERE name = '" + tv.getText().toString() + "'");

                    Fullitemdata.clear();

                    itemInfoDialog.dismiss();

                    db = dbHelper.getReadableDatabase();
                    Cursor cursor = db.rawQuery(ItemDataBases.SELECT, null);

                    long now = System.currentTimeMillis();
                    Date mDate = new Date(now);
                    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
                    String getTime = simpleDate.format(mDate);
                    String split[] = getTime.split("-");

                    int year = Integer.parseInt(split[0]);
                    int month = Integer.parseInt(split[1]);
                    int day = Integer.parseInt(split[2]);

                    while (cursor.moveToNext()) {
                        String name = cursor.getString(0);
                        int number = cursor.getInt(2);
                        int until_year = cursor.getInt(3);
                        int until_month = cursor.getInt(4);
                        int until_day = cursor.getInt(5);
                        String type = cursor.getString(9);
                        String until = "" + (until_year-year) + "년" + (until_month-month) + "월" + (until_day- day) + "일 남음";

                        Fullitemdata.add(new FullItemData(name, number, until, type));
                    }

                    Collections.sort(Fullitemdata, sortName);

                    fullItemAdapter.notifyDataSetChanged();

                }
            };

            private View.OnClickListener OnButtonYes = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemInfoDialog.dismiss();
                }
            };
        });

        sortStandard.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sort = sortStandard.getItemAtPosition(position).toString();
                Log.v("알림", sort);
                if(sort.equals("이름순")){
                    Collections.sort(Fullitemdata, sortName);
                    fullItemAdapter.notifyDataSetChanged();
                }
                else if(sort.equals("유통기한순")){
                    Collections.sort(Fullitemdata, sortUntil);
                    fullItemAdapter.notifyDataSetChanged();
                }
                else if(sort.equals("항목별")){
                    Collections.sort(Fullitemdata, sortType);
                    fullItemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
