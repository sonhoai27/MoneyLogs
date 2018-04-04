package com.sonhoai.sonho.restful;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

public class ReportActivity extends AppCompatActivity {
    private String filter,sort;
    private ListView lvReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        init();
        Toast.makeText(getApplicationContext(), filter+"-"+sort,Toast.LENGTH_SHORT).show();
    }

    private void init(){
        //get intent data
        getIntentData();
        setTitle("Report Detail");
    }

    private void getIntentData(){
        Bundle bundle = getIntent().getExtras();
        filter = bundle.getString("FILTER");
        sort = bundle.getString("SORT");
    }
}
