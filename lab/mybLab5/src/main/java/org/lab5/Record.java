package org.lab5;

import java.util.Date;

public class Record {
    private String ISBN;
    private Integer cid;
    private Date BorrowTime;
    private Date due;
    private Integer aid;
    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Date getBorrowTime() {
        return BorrowTime;
    }

    public void setBorrowTime(Date borrowTime) {
        BorrowTime = borrowTime;
    }

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }



}
