package com.sonhoai.sonho.restful.Models;

/**
 * Created by sonho on 4/2/2018.
 */

public class MoneyLog {
    private int id;
    private String name;
    private float amount;
    private String note;
    private int type;
    private String date;

    public MoneyLog(int id, String name, float amount, String note, int type, String date) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.note = note;
        this.type = type;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
