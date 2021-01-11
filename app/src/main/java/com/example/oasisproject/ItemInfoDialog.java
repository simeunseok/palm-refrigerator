package com.example.oasisproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class ItemInfoDialog extends Dialog {
    private Context context;
    private Button buttonYes, buttonDelete;

    private View.OnClickListener mOnButtonDelete;
    private View.OnClickListener mOnButtonYes;

    public ItemInfoDialog(@NonNull Context context, View.OnClickListener onButtonDelete ,View.OnClickListener onButtonYes) {
        super(context);
        this.context=context;
        this.mOnButtonDelete = onButtonDelete;
        this.mOnButtonYes = onButtonYes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iteminfodialog);

        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        buttonYes = (Button) findViewById(R.id.buttonYes);

        buttonDelete.setOnClickListener(mOnButtonDelete);
        buttonYes.setOnClickListener(mOnButtonYes);

    }
}
