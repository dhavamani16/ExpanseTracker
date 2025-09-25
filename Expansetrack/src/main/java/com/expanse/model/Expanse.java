package com.expanse.model;

import java.time.LocalDateTime;

public class Expanse {
    private int eid;
    private int cid;
     private LocalDateTime date;
    private int amt;
    public Expanse(int eid, int cid, LocalDateTime date, int amt) {
        this.eid = eid;
        this.cid = cid;
        this.date = date;
        this.amt = amt;
    }
    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getAmt() {
        return amt;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }
}
