package com.nipponit.fiportal.db;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.util.Log;

import com.nipponit.fiportal.CustomerModel;
import com.nipponit.fiportal.commentModel;
import com.nipponit.fiportal.reasonModel;
import com.nipponit.fiportal.requestModel;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class dbconnection {
    // connect with sql server

    private static String Uname="nippolac";
    private static String Password="nplk#456";
    private static String driver="net.sourceforge.jtds.jdbc.Driver";
    private static String connString_SAP="jdbc:jtds:sqlserver://192.168.101.145; DatabaseName=FINANCE_MGR_SYS";

    private Connection conn=null;

    public ArrayList<requestModel> DownloadRequest(String SIMserial, String YEAR, String MONTH, String STATUS){
        try {

            Class.forName(driver);
            conn = DriverManager.getConnection(connString_SAP,Uname,Password);
            Log.w("connection","open");


            String query =
                    "EXEC \n" +
                    "FINANCE_MGR_SYS.dbo.DownloadRequest '"+SIMserial+"','"+YEAR+"','"+MONTH+"','"+STATUS+"'";

            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rsRequest = stmt.executeQuery();
            ArrayList<requestModel> DataList = new ArrayList<>();

            if(rsRequest != null) {
                while (rsRequest.next()){
                    String reqid = rsRequest.getString("ReqID");
                    String cuscode = rsRequest.getString("CusCode");
                    String cusname = rsRequest.getString("CusName");

                    String crdlimit = "";
                    String AMT = rsRequest.getString("CreditLimit");
                    if(AMT != null || !AMT.equals("")) {
                        double amount = Double.parseDouble(AMT);
                        DecimalFormat formatter = new DecimalFormat("#,###,###,###.00");
                        crdlimit = formatter.format(amount);
                    }

                    String status = rsRequest.getString("StatusText");
                    String salesName = rsRequest.getString("Name");
                    int role = Integer.parseInt(rsRequest.getString("Role"));
                    String datetime = rsRequest.getString("CreatedDate");
                    String Rtype = rsRequest.getString("RequestType");

                    String action = rsRequest.getString("RecentAction");

                    DataList.add(new requestModel(reqid, cuscode, cusname, crdlimit, status,salesName, role,datetime,Rtype,
                            action));
                }
                conn.close();
            }
            return DataList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<commentModel> DownloadCommentData(String reqID){
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(connString_SAP, Uname, Password);
            Log.w("connection", "open");


            String query = "EXEC FINANCE_MGR_SYS.dbo.DownloadRequestDeatail '"+reqID+"'";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rsComment = stmt.executeQuery();
            ArrayList<commentModel> DataList = new ArrayList<>();

            while (rsComment.next()){
                String reqId = rsComment.getString("ReqID");
                String createdByName = rsComment.getString("CreatedByName");

                String crdlimit = "";
                String AMT = rsComment.getString("CreditLimit");
                if(AMT == null || AMT.equals("")){

                }else{
                    double amount = Double.parseDouble(AMT);
                    DecimalFormat formatter = new DecimalFormat("#,###,###,###.00");
                    crdlimit = formatter.format(amount);
                }

                String comment = rsComment.getString("comment");
                String reason = rsComment.getString("reason");
                int curLevel  = Integer.parseInt(rsComment.getString("CUR_STATUS"));
                String datetime = rsComment.getString("CreatedDate");
                String LEVEL = rsComment.getString("LEVEL");
                String ENS_ = rsComment.getString("ENS_");
                String attachment = rsComment.getString("attachment");

                DataList.add(new commentModel(reqId,createdByName,crdlimit,comment,reason,curLevel,datetime,LEVEL,ENS_,attachment));
            }
            return DataList;
        }catch (Exception ex){
            return null;
        }
    }

    // Download Customer Data
    public ArrayList<CustomerModel> DownloadCustomers(String SIMserial){
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(connString_SAP, Uname, Password);
            Log.w("connection", "open");


            String query = "EXEC FINANCE_MGR_SYS.dbo.DownloadCustomers '"+SIMserial+"'";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rsCustomer = stmt.executeQuery();
            ArrayList<CustomerModel> DataList = new ArrayList<>();

            while (rsCustomer.next()){
                String CusCode = rsCustomer.getString("CusCode");
                String CusName = rsCustomer.getString("CusName");

                String crdlimit = "";
                double AMT = Double.parseDouble(rsCustomer.getString("CREDITLIMIT"));
                if(AMT == 0.00){
                    crdlimit = "0.00";
                }else{
                    double amount = (AMT);
                    DecimalFormat formatter = new DecimalFormat("#,###,###,###.00");
                    crdlimit = formatter.format(amount);
                }

                DataList.add(new CustomerModel(CusCode,CusName,crdlimit));
            }
            return DataList;
        }catch (Exception ex){
            return null;
        }
    }


    public ArrayList<String> DownloadReasonData(){
        try{
            Class.forName(driver);
            conn = DriverManager.getConnection(connString_SAP,Uname,Password);
            Log.w("connection","open");

            String query =
                    "SELECT ReasonID, Reason FROM FINANCE_MGR_SYS.dbo.ReasonMaster";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rsReason = stmt.executeQuery();
            ArrayList<String> DataList = new ArrayList<>();

            while (rsReason.next()){
                String id = rsReason.getString("ReasonID");
                String text = rsReason.getString("Reason");
                DataList.add(id+" - "+text);
            }
            return DataList;
        }catch (Exception ex){
            return null;
        }
    }



    public String RequestingData(int reqtype, String cusCode,String reqid,String reasonID, String crdlimit, String comment, int createdby, String createdbyName, int entype,
                                 String encImg1, String encImg2, String encImg3){
        try{
            Class.forName(driver);
            conn = DriverManager.getConnection(connString_SAP,Uname,Password);
            Log.w("connection","open");

            String cID="";

            String query = "exec \n" +
                    "[FINANCE_MGR_SYS].[dbo].[SendRequest]\n" +
                    ""+reqtype+","+cusCode+","+
                    ""+reqid+","+reasonID+","+crdlimit+",'"+comment+"','"+createdby+"','"+createdbyName+"', "+entype+"," +
                    "'"+encImg1+"','"+encImg2+"','"+encImg3+"'";

            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rsRequest = stmt.executeQuery();

            while(rsRequest.next()){
                cID = rsRequest.getString(1);
            }
            return cID;
        }catch (Exception ex){
            return null;
        }
    }


}

