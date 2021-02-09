package com.nipponit.fiportal;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nipponit.fiportal.db.dbconnection;

import java.util.ArrayList;
import java.util.List;

public class enhanceActivity extends AppCompatActivity {

    TextView txtcusname;
    EditText txtcrdlimit,txtcomment;
    Spinner spreason;
    Button btnrequest;

    List<String> datamodel;
    reasonAdapter adapter;
    private static String ROLE = "Role",REQUEST_TYPE = "reqType",NAME = "Name", name = "", REQUEST_ID = "ReqID",
            CUSTOMER_NAME = "cusName", CUSTOMER_CODE = "cusCode",
            ReqID = "",cusName="",
            cusCode = "",RTYPE = "RType", rtype = "";
    private static int reqType = 0;
    private static int role = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enhance);

        reqType = getIntent().getIntExtra(REQUEST_TYPE,0);
        // reqType = 1 => create by CO, reqType = 2 => create by REP
        if (reqType == 1){
            ReqID = getIntent().getStringExtra(REQUEST_ID);
            cusName = getIntent().getStringExtra(CUSTOMER_NAME);
            name = getIntent().getStringExtra(NAME);
            role = getIntent().getIntExtra(ROLE,0);
            rtype = getIntent().getStringExtra(RTYPE);
            cusCode = "0";
        }else if(reqType == 2){
            cusCode = getIntent().getStringExtra(CUSTOMER_CODE);
            cusName = getIntent().getStringExtra(CUSTOMER_NAME);
            rtype = getIntent().getStringExtra(RTYPE);
            name = getIntent().getStringExtra(NAME);
            ReqID = "0";
            role = 2;
        }


        TextView lblcrdlimit = (TextView)findViewById(R.id.lblcrdlimittxt);
        txtcusname = (TextView) findViewById(R.id.txtcusname);

        txtcrdlimit = (EditText) findViewById(R.id.txtcrdlimit);
        txtcomment = (EditText) findViewById(R.id.txtreq_comment);
        spreason = (Spinner)findViewById(R.id.spnReason);

        btnrequest =(Button)findViewById(R.id.btnRequest);

        txtcusname.setText(cusName);

        btnrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reasonID = spreason.getSelectedItem().toString().split("-")[0].trim();
                String crdlimit = txtcrdlimit.getText().toString();
                String comment = txtcomment.getText().toString();
                if(!crdlimit.equals("") && !comment.equals("")) {
                    new SendRequest().execute(reasonID, crdlimit, comment);
                }else{
                    Toast.makeText(enhanceActivity.this, "All fields mandatory", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(!rtype.equals("LE")){
            lblcrdlimit.setText("REQUEST FOR CUSTOMER ORDER RELEASING");
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)lblcrdlimit.getLayoutParams();
            params.topMargin = 20;
            lblcrdlimit.setTextColor(Color.parseColor("#e74c3c"));
            txtcrdlimit.setVisibility(View.INVISIBLE);
        }

        new LoadReason().execute();
    }

    class SendRequest extends AsyncTask<String,String,String>{
        ProgressDialog prgdialog;
        String result = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgdialog= new ProgressDialog(enhanceActivity.this);
            prgdialog.setMessage("Request processing,Please wait..!");
            prgdialog.setCancelable(false);
            prgdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String rid = strings[0];
            String crdlimit = strings[1];
            String comment = strings[2];

            dbconnection dbconnection = new dbconnection();
            result = dbconnection.RequestingData(reqType,cusCode,ReqID,rid,crdlimit,comment,role,name,1,"","","");
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),"DONE",Toast.LENGTH_SHORT).show();
            prgdialog.dismiss();
            finish();
        }
    }

    class LoadReason extends AsyncTask<String, String, String>{
        ProgressDialog prgdialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            datamodel = new ArrayList<>();

            prgdialog= new ProgressDialog(enhanceActivity.this);
            prgdialog.setMessage("Downloading data,Please wait..!");
            prgdialog.setCancelable(false);
            prgdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            dbconnection connection =  new dbconnection();
            datamodel = connection.DownloadReasonData();
            return "Done";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_row,datamodel);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spreason.setAdapter(dataAdapter);
            prgdialog.dismiss();
        }
    }


}
