package com.nipponit.fiportal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.role.RoleManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.nipponit.fiportal.db.dbconnection;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class commentActivity extends AppCompatActivity {

    ListView lstcomments;
    Button btnNeedEnh, btnNoNeedEnh;
    ArrayList<commentModel> datamodel;
    commentAdapter adapter;
    private static String ROLE = "Role", NAME = "Name", name = "", REQUEST_ID = "ReqID",CUSTOMER_NAME = "cusName",ReqID = "",cusName="",
            RTYPE = "RType", rtype = "",REQUEST_TYPE = "reqType";

    String files = "";
    private static int role = 0;
    private int CurLevel = 0, EHS_ = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);



        ReqID = getIntent().getStringExtra(REQUEST_ID);
        cusName = getIntent().getStringExtra(CUSTOMER_NAME);
        name = getIntent().getStringExtra(NAME);
        role = getIntent().getIntExtra(ROLE,0);
        rtype = getIntent().getStringExtra(RTYPE);

        lstcomments = (ListView) findViewById(R.id.lstComments);
        btnNeedEnh = (Button)findViewById(R.id.btn_enhance);
        btnNoNeedEnh = (Button)findViewById(R.id.btn_no_enhance);

        btnNeedEnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;

                if(role > 2){
                    intent = new Intent(commentActivity.this, asm_rm_Activity.class);
                }else{
                    intent = new Intent(commentActivity.this, enhanceActivity.class);
                }

                intent.putExtra(REQUEST_ID,ReqID);
                intent.putExtra(CUSTOMER_NAME,cusName);
                intent.putExtra(NAME,name);
                intent.putExtra(ROLE,role);
                intent.putExtra(RTYPE,rtype);
                intent.putExtra(REQUEST_TYPE, 1);
                startActivityForResult(intent,1);
            }
        });

        btnNoNeedEnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(commentActivity.this, noEnhanceActivity.class);
                intent.putExtra(REQUEST_ID, ReqID);
                intent.putExtra(CUSTOMER_NAME,cusName);
                intent.putExtra(NAME,name);
                intent.putExtra(ROLE,role);
                intent.putExtra(RTYPE,rtype);
                startActivityForResult(intent,1);
            }
        });



        if(role == 2){
            btnNeedEnh.setEnabled(true);
            btnNoNeedEnh.setEnabled(true);
            btnNeedEnh.setText("Need Enhancement");
            btnNoNeedEnh.setText("No Need Enhancement");
        }
        else if(role > 2){
            btnNeedEnh.setEnabled(true);
            btnNeedEnh.setText("Proceed Request");
        }

        new AsyncCommentData().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.requests,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.attachment:
                if( files != null ) {
                    openAttachment();
                }else{
                    Toast.makeText(this, "No attachment", Toast.LENGTH_SHORT).show();
                }
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    public void openAttachment() {

            String baseURL = "http://192.168.101.131/cmp/Content/uploaded/";
            String url = baseURL + files;

            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            // Verify that the intent will resolve to an activity
            if (intent.resolveActivity(getPackageManager()) != null) {
                // Here we use an intent without a Chooser unlike the next example
                startActivity(intent);
            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            new AsyncCommentData().execute();
        }

    }

    class AsyncCommentData extends AsyncTask<Integer,Integer,Integer>{
        ProgressDialog prgdialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            datamodel = new ArrayList<>();

            prgdialog= new ProgressDialog(commentActivity.this);
            prgdialog.setMessage("Downloading comment data,Please wait..!");
            prgdialog.setCancelable(false);
            prgdialog.show();

        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            dbconnection connection =  new dbconnection();
            datamodel = connection.DownloadCommentData(ReqID);
            if( datamodel != null && datamodel.size() > 0) {
                CurLevel = datamodel.get(0).getCurLevel();
                files = datamodel.get(0).getAttachment();

                //EHS_ = Integer.parseInt(datamodel.get(0).getENS());
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if(datamodel != null) {
                adapter = new commentAdapter(getApplicationContext(), datamodel);
                lstcomments.setAdapter(adapter);

                if(CurLevel != role){
//                    btnNeedEnh.setVisibility(View.INVISIBLE);
//                    btnNoNeedEnh.setVisibility(View.INVISIBLE);
                    btnNeedEnh.setEnabled(false);
                    btnNoNeedEnh.setEnabled(false);
                }

                //IF NO NEED ENHANCEMENT REVERT TO MAIN SCREEN.
                //COMMENTED BY REQUEST
//                if(EHS_ == 2){
//                    finish();
//                }
            }
            prgdialog.dismiss();
        }
    }
}
