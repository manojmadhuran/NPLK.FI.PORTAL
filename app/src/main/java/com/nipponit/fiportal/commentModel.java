package com.nipponit.fiportal;

public class commentModel  {
    private String reqid;
    private String username;
    private String crdlimit;
    private String comment;
    private String reason;
    private int curLevel;
    private String datetime;
    private String LEVEL;
    private String ENS;
    private String Attachment;



    public commentModel(String reqID,String username,String crdlimit,String comment,String reason,int curlevel, String datetime, String level,
                        String ens, String attachment){
        this.setReqid(reqID);
        this.setUsername(username);
        this.setCrdlimit(crdlimit);
        this.setComment(comment);
        this.setReason(reason);
        this.setCurLevel(curlevel);
        this.setDatetime(datetime);
        this.setLEVEL(level);
        this.setENS(ens);
        this.setAttachment(attachment);
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCrdlimit() {
        return crdlimit;
    }

    public void setCrdlimit(String crdlimit) {
        this.crdlimit = crdlimit;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    public int getCurLevel() {
        return curLevel;
    }

    public void setCurLevel(int curLevel) {
        this.curLevel = curLevel;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getLEVEL() {
        return LEVEL;
    }

    public void setLEVEL(String LEVEL) {
        this.LEVEL = LEVEL;
    }

    public String getENS() {
        return ENS;
    }

    public void setENS(String ENS) {
        this.ENS = ENS;
    }

    public String getAttachment() {
        return Attachment;
    }

    public void setAttachment(String attachment) {
        Attachment = attachment;
    }
}
