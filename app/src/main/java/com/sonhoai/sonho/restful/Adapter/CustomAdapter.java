package com.sonhoai.sonho.restful.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sonhoai.sonho.restful.ExpandableHeightListView;
import com.sonhoai.sonho.restful.Models.Weeks;
import com.sonhoai.sonho.restful.R;

import java.util.List;

/**
 * Created by sonho on 4/2/2018.
 */

public class CustomAdapter extends BaseAdapter{
    private List<Weeks> weeks;
    private Context context;

    public CustomAdapter(List<Weeks> weeks, Context context) {
        this.weeks = weeks;
        this.context = context;
    }

    @Override
    public int getCount() {
        return weeks.size();
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
        ViewHolder  holder;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.custom_item_report, viewGroup,false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        Adapter adapter = new Adapter(
                view.getContext(),
                weeks.get(i).getMoneyLogList()
        );
        Log.i("DATAS", String.valueOf(weeks.get(i).getMoneyLogList().size()));
        holder.txtTuan.setText(weeks.get(i).getObj().getName());
        if(weeks.get(i).getObj().getDate().length() > 0){
            holder.txtThang.setText(weeks.get(i).getObj().getDate().substring(0,weeks.get(i).getObj().getDate().length()-3));
        }else {
            holder.txtThang.setText("");
        }
        holder.listView.setExpanded(true);
        holder.listView.setAdapter(adapter);
        return view;
    }


    private class ViewHolder{
        private ExpandableHeightListView listView;
        private TextView txtTuan,txtThang;

        public ViewHolder(View view) {
            listView = view.findViewById(R.id.lvItemsReport);
            txtTuan = view.findViewById(R.id.txtTitleReportTuan);
            txtThang = view.findViewById(R.id.txtTitleReportMonth);
        }
    }
}
