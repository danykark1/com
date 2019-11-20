package com.example.sign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.cardview.widget.CardView;

import com.stock.StockEntry;

import activity.LoginActivity;
import helper.SessionManager;


public class Menu extends AppCompatActivity {

    private CardView btnstock;
    private CardView btnLogout;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnstock = (CardView) findViewById(R.id.stockid);
        btnLogout = (CardView)findViewById(R.id.btnLogout);

        btnstock.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        Barcode_scanner.class);
                startActivity(i);
                finish();
            }
        });

//        btnstock.setOnClickListener(new View.OnClickListener() {
////////
////////            public void onClick(View view) {
////////
////////
//////////                Intent i = new Intent(getApplicationContext(),
//////////                        Barcode_scanner.class);
//////////                startActivity(i);
//////////                finish();
////////            }
////////        });

        session = new SessionManager(getApplicationContext());


        btnLogout.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });


    }
    private void logoutUser() {
        session.setLogin(false);
        // Launching the login activity
        Intent intent = new Intent(Menu.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
