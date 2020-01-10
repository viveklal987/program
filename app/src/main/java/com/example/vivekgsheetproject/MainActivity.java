package com.example.vivekgsheetproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    EditText editTextSiteNo,editTextEmployeeId,editTextVehicleNo,editTextMobileNo,editTextKmEntry;
    Button buttonAddItem;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        editTextSiteNo = (EditText)findViewById(R.id.btn_SiteNo);
        editTextEmployeeId = (EditText)findViewById(R.id.btn_EmployeeId);
        editTextVehicleNo = (EditText)findViewById(R.id.btn_VehicleNo);
        editTextMobileNo = (EditText)findViewById(R.id.btn_MobileNo);
        editTextKmEntry = (EditText)findViewById(R.id.btn_KmEntry);


        buttonAddItem = (Button)findViewById(R.id.btn_add_item);
        buttonAddItem.setOnClickListener(this);


    }

    //This is the part where data is transafeered from Your Android phone to Sheet by using HTTP Rest API calls

    private void   addItemToSheet() {

        final ProgressDialog loading = ProgressDialog.show(this,"Adding Item","Please wait");
        final String SiteNo = editTextSiteNo.getText().toString().trim();
        final String EmployeeId = editTextEmployeeId.getText().toString().trim();

        final String VehicleNo = editTextVehicleNo.getText().toString().trim();
        final String MobileNo = editTextMobileNo.getText().toString().trim();
        final String KmEntry =editTextKmEntry.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbxteODThV9UZ3zfmCu10jD1UUA0KIBI7Z6lfA0RpAMH2Dg-Ap8/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loading.dismiss();
                        Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();

                //here we pass params
                parmas.put("action","addItem");
                parmas.put("SiteNo",SiteNo);
                parmas.put("EmployeeId",EmployeeId);
                parmas.put("VehicleNo",VehicleNo);
                parmas.put("MobileNo",MobileNo);
                parmas.put("KmEntry",KmEntry);
                parmas.put("KmOut",new Integer(00).toString());

                return parmas;

            }
        };

        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }

    @Override
    public void onClick(View v) {

        if(v==buttonAddItem){
            addItemToSheet();


            //Define what to do when button is clicked

        }

    }



}