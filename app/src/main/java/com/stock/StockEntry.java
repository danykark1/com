package com.stock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sign.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import app.ProductDetail;
import app.ProductDetails;
import app.ProductDetailsSubmited;

public class StockEntry extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Button btn_date_picker;
    Calendar calendar;
    DatePickerDialog datePickerDialog;
    int Year, Month, Day;
    private static final String TAG = StockEntry.class.getSimpleName();
    private ProgressDialog pDialog;
    private ProductDetails prod_details;
    private List<String> brand_list = new ArrayList<>();
    private List<String> model_list = new ArrayList<>();
    List p_detail_list;
    StringRequest strReq;


    private Boolean isOkClicked;

    MaterialBetterSpinner brand, model, memory_size, processor, battery, camera, display;
    TextInputEditText brach_name, product_price, selling_price, product_status;
    Button submit;


    TextView imei, dateSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_entry);


        brand = (MaterialBetterSpinner) findViewById(R.id.brand_spinner);
        model = (MaterialBetterSpinner) findViewById(R.id.model_spinner);
        imei = (TextView) findViewById(R.id.imei_view);
        dateSelected = (TextView) findViewById(R.id.selectedDate);
        brach_name = (TextInputEditText) findViewById(R.id.id_branch_name);
        product_price = (TextInputEditText) findViewById(R.id.product_price);
        selling_price = (TextInputEditText) findViewById(R.id.selling_price);
        product_status = (TextInputEditText) findViewById(R.id.product_status);
        submit = (Button)findViewById(R.id.btn_submit);
        btn_date_picker = (Button) findViewById(R.id.date_picker);


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        Intent imeiIntent = getIntent();
        Bundle b = imeiIntent.getExtras();

        if (b != null) {
            String imeiNo = (String) b.get("imeiNo");
            imei.setText(imeiNo);
        }


        final String tag_string_req = "Details";
        Log.d("am testi", imei.getText().toString());

        if(strReq !=null){

        }
                strReq = new StringRequest(Request.Method.GET,
                AppConfig.P_DETAILS_LOGIN, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d(TAG, "product details  Response: " + response);
                //hideDialog();
                Gson gson = new Gson();
                prod_details = gson.fromJson(response.toString(), ProductDetails.class);
                Log.d(TAG, "List details: " + prod_details.getBrands());
                setP_detail_list(prod_details.getBrands());
                for (Object obj : prod_details.getBrands()) {
                    ProductDetail individual_prd = new ProductDetail();
                    individual_prd = gson.fromJson(obj.toString(), ProductDetail.class);
                    brand_list.add(individual_prd.getP_brand());
                    model_list.add(individual_prd.getP_model());

                }
                ArrayAdapter<String> brand_dataAdapter = new ArrayAdapter<String>(StockEntry.this,
                        android.R.layout.simple_spinner_item, brand_list);
                brand_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                brand.setAdapter(brand_dataAdapter);

                ArrayAdapter<String> model_dataAdapter = new ArrayAdapter<String>(StockEntry.this,
                        android.R.layout.simple_spinner_item, model_list);
                model_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                model.setAdapter(model_dataAdapter);



//                submitDetails(brand,model, brach_name, product_price, selling_price, product_status);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


        ArrayAdapter<String> brand_dataAdapter = new ArrayAdapter<String>(StockEntry.this,
                android.R.layout.simple_spinner_item, brand_list);
        brand_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brand.setAdapter(brand_dataAdapter);

        ArrayAdapter<String> model_dataAdapter = new ArrayAdapter<String>(StockEntry.this,
                android.R.layout.simple_spinner_item, model_list);
        model_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        model.setAdapter(model_dataAdapter);



        calendar = Calendar.getInstance();

        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
//        Log.d("year", Year)

        btn_date_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePickerDialog = DatePickerDialog.newInstance(StockEntry.this, Year, Month + 1, Day);

                datePickerDialog.setThemeDark(false);

                datePickerDialog.showYearPickerFirst(false);

                datePickerDialog.setAccentColor(Color.parseColor("#009688"));

                datePickerDialog.setTitle("Select Date From DatePickerDialog");

                datePickerDialog.show(getFragmentManager(), "DatePickerDialog");

                onDateSet(datePickerDialog, Year, Month + 1, Day);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String p_brands, p_mode, p_branch_name, p_product_price, p_sellingprice, p_status,dateEntered;

                p_brands = brand.getText().toString();
                p_mode = model.getText().toString();
                p_branch_name = brach_name.getText().toString();
                p_product_price = product_price.getText().toString();
                p_sellingprice = selling_price.getText().toString();
                p_status = product_status.getText().toString();
                dateEntered = dateSelected.getText().toString();

                //Json String Object
                String productJsonTosubmit = getJsonString(p_brands,p_mode,p_product_price,p_sellingprice,dateEntered);

                Log.e(TAG, "Request completed onclick: "+productJsonTosubmit);

//                String jj = p_brands + " "+p_mode+" "+p_branch_name+" "+p_product_price+
//                        " "+p_sellingprice+" "+p_status+" "+dateEntered;
//
//                Toast.makeText(StockEntry.this, jj, Toast.LENGTH_LONG).show();
//
//                Log.d("values", p_brands + " "+p_mode+" "+p_branch_name+" "+p_product_price+
//                        " "+p_sellingprice+" "+p_status+" "+dateEntered);


                StringRequest product_details = new StringRequest(Request.Method.POST, AppConfig.P_DETAILS_LOCAL,
                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                // response
                                Log.d("Local Response", response);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error Local", error.toString());
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams()
                    {

                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("imei", "wtwetwqet");
                        params.put("buying_price", "500");
                        params.put("selling_price", "800");
                        params.put("entry_date", "12/11/2019");

                        return params;
                    }
                };
                AppController.getInstance().addToRequestQueue(product_details);
            }
        });

    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        isOkClicked = Boolean.TRUE;
        String date = dayOfMonth + "-" + monthOfYear + "-" + year;

        Toast.makeText(StockEntry.this, date, Toast.LENGTH_LONG).show();
        dateSelected.setText(date);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public List getP_detail_list() {
        return p_detail_list;
    }

    public void setP_detail_list(List p_detail_list) {
        this.p_detail_list = p_detail_list;
    }
    public String getJsonString(String brand, String model, String buying_price, String selling_price, String dateEntered){
        ProductDetailsSubmited prodct_details = new ProductDetailsSubmited();
        prodct_details.setBrand(brand);
        prodct_details.setModel(model);
        prodct_details.setBuying_price(buying_price);
        prodct_details.setSelling_price(selling_price);
        prodct_details.setDate_entered(dateEntered);

        List product_list = getP_detail_list();
        Gson gson = new Gson();
        for (Object obj : product_list) {
            ProductDetail individual_prd = new ProductDetail();
            individual_prd = gson.fromJson(obj.toString(), ProductDetail.class);

            if(prodct_details.getModel().equals(individual_prd.getP_model())){
                prodct_details.setId(individual_prd.getP_id());
            }

        }
        Log.e(TAG, "Product Id matching model: " + prodct_details.getId());
        String productJsonString = gson.toJson(prodct_details);
        return productJsonString;
    }
}
