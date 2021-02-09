package com.nipponit.fiportal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.nipponit.fiportal.db.dbconnection;

import java.util.ArrayList;
import java.util.List;

public class createRequest extends AppCompatActivity {
    dbconnection connection;
    ListView lstcustomers;
    ArrayList<CustomerModel> customerModel = new ArrayList<>();
    customerAdapter adapter;
    EditText txtcusname;
    private static final int REQUEST_READ_PHONE_STATE=0;
    private static String SimSerial,RTYPE = "RType",REQUEST_TYPE = "reqType",CUSTOMER_CODE = "cusCode",CUSTOMER_NAME = "cusName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request);
        lstcustomers = (ListView) findViewById(R.id.lstcustomer);
        txtcusname = (EditText) findViewById(R.id.txtCusSearch);

        new DownloadCustomers().execute();
        ////get SIM serial
        SimSerial = getSerial();



        txtcusname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    adapter.getFilter().filter(s.toString());
                }catch (Exception ex){

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        lstcustomers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cusCode = adapter.getItem(position).getCcode();
                String cusName = adapter.getItem(position).getCname();
                Intent intent = new Intent(createRequest.this,enhanceActivity.class);
                intent.putExtra(REQUEST_TYPE,2);
                intent.putExtra(CUSTOMER_NAME,cusName);
                intent.putExtra(CUSTOMER_CODE,cusCode);
                intent.putExtra(RTYPE,"LE");
                startActivity(intent);
            }
        });

    }


    class DownloadCustomers extends AsyncTask<Integer, Integer, Integer>{

        ProgressDialog prgdialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgdialog= new ProgressDialog(createRequest.this);
            customerModel = new ArrayList<>();
            prgdialog.setMessage("Downloading customer data,Please wait..!");
            prgdialog.setCancelable(false);
            prgdialog.show();
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            connection = new dbconnection();
            customerModel = connection.DownloadCustomers(SimSerial);
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(customerModel != null){
                adapter = new customerAdapter(getApplicationContext(),customerModel);
                lstcustomers.setAdapter(adapter);
            }prgdialog.dismiss();
        }
    }

    private String getSerial(){
        try{
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
            } else {
                //TODO
            }
            TelephonyManager tpm = (TelephonyManager)getSystemService(getApplicationContext().TELEPHONY_SERVICE);
            SimSerial = tpm.getSimSerialNumber().toString();
            return SimSerial;

        }catch (Exception ex){
            Log.w("error",ex.getMessage());
            return "";
        }
    }

}
