package com.sonhoai.sonho.restful.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sonhoai.sonho.restful.Models.MoneyLog;
import com.sonhoai.sonho.restful.R;

import java.util.List;

/**
 * Created by sonho on 4/2/2018.
 */

public class Adapter extends BaseAdapter{
    private Context mContext;
    private List<MoneyLog> moneyLogList;

    public Adapter(Context mContext, List<MoneyLog> moneyLogList) {
        this.mContext = mContext;
        this.moneyLogList = moneyLogList;
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
        ViewHolder holder;

        if(view == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.custom_normal_item_log, viewGroup,false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.txtDate.setText(moneyLogList.get(i).getDate().split("-")[2]);
        holder.txtMonthYear.setText(moneyLogList.get(i).getDate().substring(0, moneyLogList.get(i).getDate().length() - 3));
        holder.txtName.setText(moneyLogList.get(i).getName());
        holder.txtNote.setText(moneyLogList.get(i).getNote());
        holder.txtType.setText((moneyLogList.get(i).getType() == 0) ? "Thu" : "Chi");
        holder.txtAmount.setText(moneyLogList.get(i).getAmount()+"");
        return view;
    }

    private class ViewHolder{
        private TextView txtDate, txtMonthYear,txtName,txtNote, txtType, txtAmount;

        public ViewHolder(View view) {
            txtDate = view.findViewById(R.id.txtDate);
            txtMonthYear = view.findViewById(R.id.txtMonthYear);
            txtName = view.findViewById(R.id.txtName);
            txtNote = view.findViewById(R.id.txtNote);
            txtType = view.findViewById(R.id.txtType);
            txtAmount = view.findViewById(R.id.txtAmount);
        }
    }
}
