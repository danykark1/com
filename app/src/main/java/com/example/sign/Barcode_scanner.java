package com.example.sign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.zxing.Result;
import com.stock.StockEntry;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Barcode_scanner extends AppCompatActivity {
    private ZXingScannerView scannerView;
//    private TextView scanerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        scannerView = new ZXingScannerView(this);
        scannerView.setResultHandler(new ZXingScannerResultHandler());
        setContentView(scannerView);
        scannerView.startCamera();
    }


    @Override
    public void onPause(){
        super.onPause();
        scannerView.stopCamera();
    }

    class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler{

        @Override
        public void handleResult(Result rawResult) {
            String resultCode = rawResult.getText();
//            Toast.makeText(Barcode_scanner.this,resultCode, Toast.LENGTH_SHORT).show();
//            setContentView(R.layout.activity_barcode_scanner);
            scannerView.stopCamera();
            Intent intent = new Intent(Barcode_scanner.this, StockEntry.class);
            intent.putExtra("imeiNo", resultCode);
            startActivity(intent);

        }
    }


}
