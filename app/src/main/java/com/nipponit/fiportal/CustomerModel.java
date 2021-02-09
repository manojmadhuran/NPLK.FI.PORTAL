package com.nipponit.fiportal;


public class CustomerModel {
    private String Ccode;
    private String Cname;
    private String Climit;

    public CustomerModel(String ccode, String cname, String climit){
        super();
        this.setCcode(ccode);
        this.setCname(cname);
        this.setClimit(climit);
    }


    public String getCcode() {
        return Ccode;
    }

    public void setCcode(String ccode) {
        Ccode = ccode;
    }

    public String getCname() {
        return Cname;
    }

    public void setCname(String cname) {
        Cname = cname;
    }

    public String getClimit() {
        return Climit;
    }

    public void setClimit(String climit) {
        Climit = climit;
    }
}
