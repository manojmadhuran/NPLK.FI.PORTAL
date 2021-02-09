package com.nipponit.fiportal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nipponit.fiportal.db.dbconnection;

public class asm_rm_Activity extends AppCompatActivity {

    TextView txtcusname;
    EditText txtcrdlimit,txtcomment;
    Button btnrequest;

    private static String ROLE = "Role",NAME = "Name", name = "", REQUEST_ID = "ReqID",CUSTOMER_NAME = "cusName",ReqID = "",cusName="",
            RTYPE = "RType", rtype = "";
    private static int role = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asm_rm_);

        ReqID = getIntent().getStringExtra(REQUEST_ID);
        cusName = getIntent().getStringExtra(CUSTOMER_NAME);
        name = getIntent().getStringExtra(NAME);
        role = getIntent().getIntExtra(ROLE,0);
        rtype = getIntent().getStringExtra(RTYPE);

        TextView lblcrdlimit = (TextView)findViewById(R.id.lblcrdlimittxt);
        txtcrdlimit = (EditText) findViewById(R.id.txtcrdlimit);

        txtcomment = (EditText) findViewById(R.id.txtreq_comment);
        txtcusname = (TextView) findViewById(R.id.txtcusname);
        txtcusname.setText(cusName);

        btnrequest =(Button)findViewById(R.id.btnRequest);
        btnrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = txtcomment.getText().toString();
                String crdlimit = txtcrdlimit.getText().toString();

                if(!comment.equals("") && !crdlimit.equals("")) {
                    new SendRequest().execute(comment, crdlimit);
                }else
                {
                    Toast.makeText(asm_rm_Activity.this, "All fields mandatory.", Toast.LENGTH_SHORT).show();
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

    }

    class SendRequest extends AsyncTask<String,String,String> {
        ProgressDialog prgdialog;
        String result = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgdialog= new ProgressDialog(asm_rm_Activity.this);
            prgdialog.setMessage("Request processing,Please wait..!");
            prgdialog.setCancelable(false);
            prgdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String comment = strings[0];
            String crdlimit = strings[1];

            dbconnection dbconnection = new dbconnection();
            result = dbconnection.RequestingData(1,"0",ReqID,"1",crdlimit,comment,role,name,1,
                    "","","");
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
}
