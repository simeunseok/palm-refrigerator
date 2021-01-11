package com.example.oasisproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// 냉장고
// 안씀
public class Refrigerator extends AppCompatActivity {
    ImageButton refrigerator;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refrigerator);
        refrigerator = (ImageButton) findViewById(R.id.imageButtonRefrigerator);

        refrigerator.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), clickRefrigerator.class);
                startActivity(intent);
            }
        });
    }

}
