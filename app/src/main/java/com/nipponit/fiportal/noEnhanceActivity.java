package com.nipponit.fiportal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nipponit.fiportal.db.dbconnection;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class noEnhanceActivity extends AppCompatActivity {

    TextView txtcusname;
    EditText txtcrdlimit,txtcomment;
    Button btnrequest,btnimg1,btnimg2,btnimg3;
    ImageView img1,img2,img3;

    public static final int RequestPermissionCode = 1;

    private static String ROLE = "Role", NAME = "Name", name = "", REQUEST_ID = "ReqID",CUSTOMER_NAME = "cusName",ReqID = "",cusName="";
    private static int role = 0;

    byte[] byteArray;
    String encodedImage;
    String encodedImage1;
    String encodedImage2;
    String encodedImage3;


    private static final int REQUEST_IMAGE_CAPTURE_1 = 1;
    private static final int REQUEST_IMAGE_CAPTURE_2 = 2;
    private static final int REQUEST_IMAGE_CAPTURE_3 = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_enhance);

        ReqID = getIntent().getStringExtra(REQUEST_ID);
        cusName = getIntent().getStringExtra(CUSTOMER_NAME);
        name = getIntent().getStringExtra(NAME);
        role = getIntent().getIntExtra(ROLE,0);

        txtcusname = (TextView) findViewById(R.id.txtcusname);
        txtcomment = (EditText) findViewById(R.id.txtreq_comment);
        btnrequest =(Button)findViewById(R.id.btnRequest);

        btnimg1 = (Button) findViewById(R.id.btn_img1);
        btnimg2 = (Button) findViewById(R.id.btn_img2);
        btnimg3 = (Button) findViewById(R.id.btn_img3);

        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);

        EnableRuntimePermission();

        btnimg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_1);
                }
            }
        });



        btnimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_2);
                }
            }
        });

        btnimg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_3);
                }
            }
        });

        txtcusname.setText(cusName);

        btnrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = txtcomment.getText().toString();

                if(!comment.equals("")) {
                    new SendRequest().execute(comment);
                }else{
                    Toast.makeText(noEnhanceActivity.this, "All fields mandatory", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private String encodeImg(int img){
        try{
            if(img == 1) {
                Bitmap image = ((BitmapDrawable) img1.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                byteArray = byteArrayOutputStream.toByteArray();
            }else if(img == 2){
                Bitmap image = ((BitmapDrawable) img2.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                byteArray = byteArrayOutputStream.toByteArray();
            }else if(img == 3){
                Bitmap image = ((BitmapDrawable) img3.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                byteArray = byteArrayOutputStream.toByteArray();
            }
            return encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }catch (Exception ex){
            return "";
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE_1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img1.setImageBitmap(imageBitmap);
            encodedImage1 = encodeImg(1);
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE_2 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img2.setImageBitmap(imageBitmap);
            encodedImage2 = encodeImg(2);
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE_3 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img3.setImageBitmap(imageBitmap);
            encodedImage3 = encodeImg(3);
        }
    }

    private void EnableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(noEnhanceActivity.this,
                Manifest.permission.CAMERA)) {
            Toast.makeText(noEnhanceActivity.this,"CAMERA permission allows us to Access CAMERA app",     Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(noEnhanceActivity.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] result) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (result.length > 0 && result[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(noEnhanceActivity.this, "Permission Granted.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(noEnhanceActivity.this, "Permission Canceled.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    class SendRequest extends AsyncTask<String,String,String> {
        ProgressDialog prgdialog;
        String result = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgdialog= new ProgressDialog(noEnhanceActivity.this);
            prgdialog.setMessage("Request processing,Please wait..!");
            prgdialog.setCancelable(false);
            prgdialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String comment = strings[0];

            dbconnection dbconnection = new dbconnection();
            result = dbconnection.RequestingData(1,"0",ReqID,"1","0",comment,role,name,2,
                    encodedImage1,encodedImage2,encodedImage3);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
            prgdialog.dismiss();
            finish();
        }
    }
}
