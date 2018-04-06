package com.sonhoai.sonho.restful;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.sonhoai.sonho.restful.Adapter.CustomAdapter;
import com.sonhoai.sonho.restful.Models.MoneyLog;
import com.sonhoai.sonho.restful.Models.Obj;
import com.sonhoai.sonho.restful.Models.Weeks;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    private String filter,sort;
    private ListView lvReport;
    private List<Weeks> weeks = new ArrayList<>();
    private CustomAdapter customAdapter;
    private String BASEURL = "http://192.168.43.127:9000/api/MoneyLogs/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        init();
    }

    private void init(){
        lvReport = findViewById(R.id.lvReport);
        //get intent data
        getIntentData();
        setTitle("Report Detail");
        customAdapter = new CustomAdapter(
                weeks,
                getApplicationContext()
        );
        lvReport.setAdapter(customAdapter);
    }

    private void getIntentData(){
        Bundle bundle = getIntent().getExtras();
        filter = bundle.getString("FILTER");
        sort = bundle.getString("SORT");
        Log.i("SORT", sort);
        if(sort.equals("0")){
            new doGets().execute(BASEURL+"Week?type="+filter);
        }else if(sort.equals("1")){
            new doGets().execute(BASEURL+"Quart?type="+filter);
        }else if(sort.equals("2")){
            new doGets().execute(BASEURL+"Month?type="+filter);
        }else if(sort.equals("3")){
            new doGets().execute(BASEURL+"Year?type="+filter);
        }
    }

    private class doGets extends AsyncTask<String, Void,Integer>{

        @Override
        protected Integer doInBackground(String... strings) {
            String urlString = strings[0];
            URL url = null;
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            int c;
            String result = "";
            try {
                url = new URL(urlString);
                httpURLConnection = (HttpURLConnection) url.openConnection();//truyen vao method
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                inputStream = httpURLConnection.getInputStream();

                //khác -1 là vẫn còn
                while ((c=inputStream.read()) != -1){
                    result+=(char)c;
                }
                JsonToArray(result);
            } catch (Exception e) {
                //that bai
                e.printStackTrace();
                return 400;
            }
            //thanh cong
            return 200;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.i("CODEGET", String.valueOf(integer));
            if(integer == 200){
                customAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Thanh công", Toast.LENGTH_SHORT).show();
            }else if(integer == 400){
                Toast.makeText(getApplicationContext(), "That bai", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void JsonToArray(String obj) throws JSONException {
        JSONArray array = new JSONArray(obj);
        JSONObject object;

        for (int i = 0; i < array.length();i++){
            List<MoneyLog> moneyLogList = new ArrayList<>();
            Obj myObj;

            object = array.getJSONObject(i);//obj lớn

            JSONArray arrMoenyLogs = new JSONArray(object.getString("MoneyLogList"));//obj nhỏ 2
            JSONObject objDay =  new JSONObject(object.getString("WeekObj"));

            JSONObject objectML;
            myObj = new Obj(
                   objDay.getInt("Id"),
                   objDay.getString("Name"),
                   objDay.getString("Date")
            );
            for (int y = 0; y < arrMoenyLogs.length();y++){
                objectML = arrMoenyLogs.getJSONObject(y);
                moneyLogList.add(new MoneyLog(
                   objectML.getInt("id"),
                   objectML.getString("name"),
                   objectML.getInt("amount"),
                   objectML.getString("note"),
                   objectML.getInt("type"),
                   objectML.getString("date")
                ));
            }

            weeks.add(new Weeks(
                    myObj,
               moneyLogList
            ));
        }

        System.out.println(weeks.get(0).getObj().getDate());
    }
}
