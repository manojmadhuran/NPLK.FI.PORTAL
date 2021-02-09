package com.nipponit.fiportal;

public class requestModel {
    private String reqid;
    private String cuscode;
    private String cusname;
    private String crdlimit;
    private String status;
    private String Name;
    private int Role;
    private String datetime;
    private String type;
    private String action;

    public requestModel (String Reqid,String cuscode,String cusname, String crdlimit, String status,String Name_, int role,
                         String datetime,String type_, String action_){
        this.setReqid(Reqid);
        this.setCuscode(cuscode);
        this.setCusname(cusname);
        this.setCrdlimit(crdlimit);
        this.setStatus(status);
        this.setName(Name_);
        this.setRole(role);
        this.setDatetime(datetime);
        this.setType(type_);
        this.setAction(action_);
    }


    public String getCusname() {
        return cusname;
    }

    public void setCusname(String cusname) {
        this.cusname = cusname;
    }

    public String getCrdlimit() {
        return crdlimit;
    }

    public void setCrdlimit(String crdlimit) {
        this.crdlimit = crdlimit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCuscode() {
        return cuscode;
    }

    public void setCuscode(String cuscode) {
        this.cuscode = cuscode;
    }

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getRole() {
        return Role;
    }

    public void setRole(int role) {
        Role = role;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
