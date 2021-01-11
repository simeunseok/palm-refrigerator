package com.example.oasisproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

// 재료 추가하는 다이어로그
public class AddItemDialog extends Dialog {
    private Context context;
    private Button buttonAdd;
    private Spinner typeSpinner;
    private TextView textType;

    private View.OnClickListener mOnAddItem;

    public AddItemDialog(@NonNull Context context, View.OnClickListener OnAddItem) {
        super(context);
        this.context=context;
        this.mOnAddItem=OnAddItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.additemdialog);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(mOnAddItem);
        textType = (TextView) findViewById(R.id.textType);
        typeSpinner = (Spinner) findViewById(R.id.spinnerType);

        ArrayAdapter typeAdapter = ArrayAdapter.createFromResource(context, R.array.type_array, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemType = typeSpinner.getItemAtPosition(position).toString();
                textType.setText(itemType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }
}
