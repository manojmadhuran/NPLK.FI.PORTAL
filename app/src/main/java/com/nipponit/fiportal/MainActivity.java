package com.nipponit.fiportal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import com.nipponit.fiportal.db.*;

public class MainActivity extends AppCompatActivity {

    ListView lstRequest;
    Button btnsearch;
    Spinner spYear, spMonth, spStatus;
    TextView cusname, enhancement, status,lblcount;

    ArrayList<requestModel> datamodel;
    requestAdapter adapter;

    int Role = 0;//REP = 1, ASM = 2, RM = 3

    private static final int REQUEST_READ_PHONE_STATE=0;
    private static String SimSerial,sales_code,TargetDate,Month;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstRequest = (ListView)findViewById(R.id.lstRequestList);
        btnsearch = (Button) findViewById(R.id.btnsearch);
        spYear = (Spinner) findViewById(R.id.sp_year);
        spMonth = (Spinner) findViewById(R.id.sp_month);
        spStatus = (Spinner) findViewById(R.id.sp_status);
        lblcount = (TextView) findViewById(R.id.lblcount);

        //get SIM serial
        SimSerial = getSerial();

        //set current year/month
        Calendar calendar =Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int yp =-1;
        switch (year){
            case 2020:
                yp=0;
                break;
            case 2021:
                yp=1;
                break;
            case 2022:
                yp=2;
                break;
            case 2023:
                yp=3;
                break;
        }
        spYear.setSelection(yp);
        int month = calendar.get(Calendar.MONTH);
        int mp = -1;
        switch (month){
            case 0:
                mp = 0;
                break;
            case 1:
                mp = 1;
                break;
            case 2:
                mp = 2;
                break;
            case 3:
                mp = 3;
                break;
            case 4:
                mp = 4;
                break;
            case 5:
                mp = 5;
                break;
            case 6:
                mp = 6;
                break;
            case 7:
                mp = 7;
                break;
            case 8:
                mp = 8;
                break;
            case 9:
                mp = 9;
                break;
            case 10:
                mp = 10;
                break;
            case 11:
                mp = 11;
                break;
        }
        spMonth.setSelection(mp);
        new download_request().execute();

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new download_request().execute();
            }
        });


        lstRequest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ReqID = adapter.getItem(position).getReqid();
                String cusName = adapter.getItem(position).getCusname();
                String Name = adapter.getItem(position).getName(); //REP, ASM, RM name
                int role = adapter.getItem(position).getRole();
                String Rtype = adapter.getItem(position).getType();//Limit Enhancement & Order Releasing

                Intent intent = new Intent(MainActivity.this, commentActivity.class);
                intent.putExtra("ReqID",ReqID);
                intent.putExtra("cusName",cusName);
                intent.putExtra("Name",Name);
                intent.putExtra("Role", role);
                intent.putExtra("RType",Rtype);

                startActivityForResult(intent,1);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.createreq,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_request:
                //Intent intent = new Intent(MainActivity.this,createRequest.class);
                //startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            //get SIM serial
            SimSerial = getSerial();
            new download_request().execute();
        }
    }

    class download_request extends AsyncTask<Integer,Integer,String>{

        ProgressDialog prgdialog;
        boolean isok=false;
        String YEAR="",MONTH="",STATUS="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgdialog= new ProgressDialog(MainActivity.this);


            YEAR = spYear.getSelectedItem().toString();
            MONTH = spMonth.getSelectedItem().toString();
            STATUS = spStatus.getSelectedItem().toString();

            datamodel = new ArrayList<>();
            prgdialog.setMessage("Downloading request data,Please wait..!");
            prgdialog.setCancelable(false);
            prgdialog.show();
        }

        @Override
        protected String doInBackground(Integer... integers) {

            dbconnection connection = new dbconnection();
            datamodel = connection.DownloadRequest(SimSerial, YEAR, MONTH.split("-")[1].trim(), STATUS);

            return "Done";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(datamodel != null) {
                adapter = new requestAdapter(getApplicationContext(), datamodel);
                lstRequest.setAdapter(adapter);
                int totRequest = adapter.getCount();
                lblcount.setText("Total "+ totRequest + " Request(s)");
            }
            prgdialog.dismiss();
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
