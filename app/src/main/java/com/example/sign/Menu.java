package com.example.sign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.cardview.widget.CardView;


public class Menu extends AppCompatActivity {

    private CardView btnstock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnstock = (CardView) findViewById(R.id.stockid);

        btnstock.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        Stock.class);
                startActivity(i);
                finish();
            }
        });


    }
}
