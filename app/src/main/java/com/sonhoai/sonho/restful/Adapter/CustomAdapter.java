package com.sonhoai.sonho.restful.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sonhoai.sonho.restful.Models.MoneyLog;
import com.sonhoai.sonho.restful.Models.Week;
import com.sonhoai.sonho.restful.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonho on 4/2/2018.
 */

public class CustomAdapter extends BaseAdapter{
    private List<Week> weeks;
    private List<MoneyLog> moneyLogList;
    private Context context;

    public CustomAdapter(List<MoneyLog> moneyLogList, Context context) {
        this.moneyLogList = moneyLogList;
        this.context = context;
        init();
    }

    @Override
    public int getCount() {
        return moneyLogList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }


    private class ViewHolder{
        private TextView txtDate, txtMonthYear,txtName,txtNote, txtType, txtAmount;

        public ViewHolder(View view) {

        }
    }

    private void generateWeekList(){
        weeks = new ArrayList<>();
        for(int i = 0; i < 4;i++){
            weeks.add(new Week(
               i+1,
               "Tuần: "+i+1
            ));
        }
    }
    private void init(){
        generateWeekList();
    }
}
