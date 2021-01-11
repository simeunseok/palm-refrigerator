package com.example.oasisproject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

// 재료 입력 방법을 선택하는 다이어로그
public class SelectDialog extends Dialog {
    private Context context;
    private Button btnTakePicture, btnUploadPicture, btnWrite;

    private View.OnClickListener mOnTakePicture;
    private View.OnClickListener mOnLoadPicture;
    private View.OnClickListener mOnWrite;

    public SelectDialog(@NonNull Context context, View.OnClickListener OnTakePicture, View.OnClickListener OnLoadPicture, View.OnClickListener OnWrite){
        super(context);
        this.context = context;
        this.mOnTakePicture = OnTakePicture;
        this.mOnLoadPicture = OnLoadPicture;
        this.mOnWrite = OnWrite;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectinsertdialog);

        btnTakePicture = (Button) findViewById(R.id.buttonTakePicture);
        btnUploadPicture = (Button) findViewById(R.id.buttonUploadPicture);
        btnWrite = (Button) findViewById(R.id.buttonWrite);

        btnTakePicture.setOnClickListener(mOnTakePicture);
        btnUploadPicture.setOnClickListener(mOnLoadPicture);
        btnWrite.setOnClickListener(mOnWrite);
    }
}
