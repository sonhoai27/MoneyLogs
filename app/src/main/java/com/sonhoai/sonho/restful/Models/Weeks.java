package com.sonhoai.sonho.restful.Models;

import java.util.List;

/**
 * Created by sonho on 4/4/2018.
 */

public class Weeks {
    private List<Week> weekList;
    private List<MoneyLog> moneyLogList;

    public Weeks(List<Week> weekList, List<MoneyLog> moneyLogList) {
        this.weekList = weekList;
        this.moneyLogList = moneyLogList;
    }

    public List<Week> getWeekList() {
        return weekList;
    }

    public void setWeekList(List<Week> weekList) {
        this.weekList = weekList;
    }

    public List<MoneyLog> getMoneyLogList() {
        return moneyLogList;
    }

    public void setMoneyLogList(List<MoneyLog> moneyLogList) {
        this.moneyLogList = moneyLogList;
    }
}
