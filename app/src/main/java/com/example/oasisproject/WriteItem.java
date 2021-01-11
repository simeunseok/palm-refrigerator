package com.example.oasisproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

// 물품 직접 입력해서 추가할 때 쓰는 파일
public class WriteItem extends AppCompatActivity {
    private AddItemDialog addItemDialog;
    private EditText editTextName, editTextMoney, editTextNumber, editTextDay;
    private Spinner typeSpinner;
    private ImageButton addItem;
    private Button done;
    private TextView textType;
    String itemType;
    ArrayList<ItemData> ItemList;
    DbOpenHelper dbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inputitemlist);

        this.InitializeItemDate();

        init_table();

        ListView ListViewItem = (ListView) findViewById(R.id.ListViewItem);
        final ItemAdapter itemAdapter = new ItemAdapter(this, ItemList);

        ListViewItem.setAdapter(itemAdapter);

        addItem = (ImageButton) findViewById(R.id.imageButtonAddItem);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemDialog = new AddItemDialog(WriteItem.this, OnAddItem);


                addItemDialog.setCanceledOnTouchOutside(true);
                addItemDialog.setCancelable(true);
                addItemDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                addItemDialog.show();
            }

            private View.OnClickListener OnAddItem = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editTextName = (EditText) addItemDialog.findViewById(R.id.editTextName);
                    editTextMoney = (EditText) addItemDialog.findViewById(R.id.editTextMoney);
                    editTextNumber = (EditText) addItemDialog.findViewById(R.id.editTextNumber);
                    editTextDay = (EditText) addItemDialog.findViewById(R.id.editTextDay);
                    textType = (TextView) addItemDialog.findViewById(R.id.textType);


                    final String[] date = {editTextDay.getText().toString()};

                    String split[] = date[0].split("/");
                    String name = editTextName.getText().toString();

                    int money = Integer.parseInt(editTextMoney.getText().toString());
                    int number = Integer.parseInt(editTextNumber.getText().toString());
                    int year = Integer.parseInt(split[0]);
                    int month = Integer.parseInt(split[1]);
                    int day = Integer.parseInt(split[2]);

                    itemType = textType.getText().toString();

                    ItemList.add(new ItemData(name, money, number, year, month, day, itemType));
                    addItemDialog.dismiss();
                }

            };

        });

        done = (Button) findViewById(R.id.buttonDone);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDatas();
                finish();
            }
        });
    }

    private void init_table() {
        dbHelper = new DbOpenHelper(this);
    }

    private void InitializeItemDate() {
        ItemList = new ArrayList<ItemData>();
    }

    private void saveDatas() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (int i = 0; i < ItemList.size(); i++) {
            String name = ItemList.get(i).getName();
            int money = ItemList.get(i).getMoney();
            int number = ItemList.get(i).getNumber();
            int until_year = ItemList.get(i).getYear();
            int until_month = ItemList.get(i).getMonth();
            int until_day = ItemList.get(i).getDay();
            String type = ItemList.get(i).getType();
            int year, month, day;

            long now = System.currentTimeMillis();
            Date mDate = new Date(now);

            SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
            String getDate = simpleDate.format(mDate);

            String split[] = getDate.split("-");

            year = Integer.parseInt(split[0]);
            month = Integer.parseInt(split[1]);
            day = Integer.parseInt(split[2]);


            String sqlInsert = ItemDataBases.INSERT +
                    " (" +
                    "'" + name + "', " +
                    money + ", " +
                    number + ", " +
                    until_year + ", " +
                    until_month + ", " +
                    until_day + ", " +
                    year + ", " +
                    month + ", " +
                    day + ", " +
                    "'" + type + "'" +
                    ")";

            db.execSQL(sqlInsert);
            Log.v("알림", "데이터 입력");
        }
    }


}