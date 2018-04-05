package com.sonhoai.sonho.restful.Models;

import java.util.List;

/**
 * Created by sonho on 4/4/2018.
 */

public class Weeks {
    private Obj obj;
    private List<MoneyLog> moneyLogList;

    public Weeks(Obj obj, List<MoneyLog> moneyLogList) {
        this.obj = obj;
        this.moneyLogList = moneyLogList;
    }

    public Obj getObj() {
        return obj;
    }

    public void setObj(Obj obj) {
        this.obj = obj;
    }

    public List<MoneyLog> getMoneyLogList() {
        return moneyLogList;
    }

    public void setMoneyLogList(List<MoneyLog> moneyLogList) {
        this.moneyLogList = moneyLogList;
    }

    @Override
    public String toString() {
        return "Weeks{" +
                "obj=" + obj +
                ", moneyLogList=" + moneyLogList +
                '}';
    }
}
