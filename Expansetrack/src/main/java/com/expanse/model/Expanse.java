package com.expanse.model;

import java.time.LocalDateTime;

public class Expanse {
    private int eid;
    private int cid;
     private LocalDateTime date;
    private int amt;
    public Expanse(int eid, int cid, LocalDateTime date, double amount) {
        this.eid = eid;
        this.cid = cid;
        this.date = date;
        this.amt = amt;
    }
    public Expanse(int categoryId, String date2, double amount) {
        
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

    public void setAmt(double amount) {
        this.amt = amt;
    }
    public void setCategoryId(int categoryId) {
  
        throw new UnsupportedOperationException("Unimplemented method 'setCategoryId'");
    }
    public void setDate(String date2) {
       
        throw new UnsupportedOperationException("Unimplemented method 'setDate'");
    }
    public Object getCategoryId() {
       
        throw new UnsupportedOperationException("Unimplemented method 'getCategoryId'");
    }
}
