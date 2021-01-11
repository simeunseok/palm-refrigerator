package com.example.oasisproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class checkItems extends AppCompatActivity {

    private ArrayList<String> itemName;
    DbOpenHelper dbHelper = new DbOpenHelper(this);
    SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkitemlist);

        itemName = new ArrayList<String>();

        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(ItemDataBases.SELECT, null);

        while(cursor.moveToNext()){
            String name = cursor.getString(0);

            itemName.add(name);
        }

        ListView listview = (ListView) findViewById(R.id.listviewSelectItem);
        final checkAdapter adapter = new checkAdapter(getBaseContext(), itemName);

        listview.setAdapter(adapter);

        Button btn = (Button) findViewById(R.id.buttonEnd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                String par = new String("");
                for(int i = 0;i<adapter.checkitems.size();i++){
                    par += adapter.checkitems.get(i);
                    par += " ";
                }

                resultIntent.putExtra("data", par);
                setResult(Code.resultCode, resultIntent);

                finish();
            }
        });
    }
}
