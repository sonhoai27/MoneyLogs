package com.sonhoai.sonho.restful;

import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sonhoai.sonho.restful.Adapter.Adapter;
import com.sonhoai.sonho.restful.Models.MoneyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lvMoneyLogs;
    private List<MoneyLog> moneyLogList;
    private Adapter adapter;
    private FloatingActionButton fbAddMoneyLog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        //registerForContextMenu(lvMoneyLogs);
        new DoGets().execute("http://192.168.1.129:9000/api/MoneyLogs");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemWeek: return  true;
            case R.id.itemQuarter: return  true;
            case R.id.itemMonth: return  true;
            case R.id.itemYear: return  true;
            default: return false;
        }
    }

    private void init(){
        fbAddMoneyLog = findViewById(R.id.fbAddMoneyLog);
        lvMoneyLogs = findViewById(R.id.lvMoneyLogs);
        moneyLogList = new ArrayList<>();
        adapter = new Adapter(
                getApplicationContext(),
                moneyLogList
        );
        lvMoneyLogs.setAdapter(adapter);
        //init
        addMoneyLog();
        setTitle("Money Logs");
    }

    private void addMoneyLog(){

        fbAddMoneyLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View view1 = inflater.inflate(R.layout.dialog_add, null);
                builder.setView(view1);
                builder.setCancelable(false);

                final EditText edtContent, edtAmount,edtNote;
                RadioGroup group;
                final RadioButton radioType;
                Button btnSave,btnClose;

                btnSave = view1.findViewById(R.id.btnSave);
                btnClose = view1.findViewById(R.id.btnClose);
                edtContent = view1.findViewById(R.id.edtContent);
                edtAmount = view1.findViewById(R.id.edtAmount);
                edtNote = view1.findViewById(R.id.edtNote);
                group = view1.findViewById(R.id.radioType);
                final AlertDialog dialog = builder.show();

                int checked = group.getCheckedRadioButtonId();
                radioType = view1.findViewById(checked);
                Log.i("CHECK", radioType.getText().toString());
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new DoPost().execute(
                                "http://192.168.1.129:9000/api/MoneyLogs",
                                edtContent.getText().toString(),
                                edtAmount.getText().toString(),
                                edtNote.getText().toString(),
                                radioType.getTag().toString()
                        );
                    }
                });
                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
            }
        });
    }

    private class DoGets extends AsyncTask<String, Void, Integer>{
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
                adapter.notifyDataSetChanged();
            }else if(integer == 400){
                Toast.makeText(getApplicationContext(), "That bai", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void JsonToArray(String jsonArray) throws JSONException {
        Log.i("result",jsonArray);
        JSONArray array = new JSONArray(jsonArray);
        JSONObject object;
        for(int i = 0; i < array.length(); i++){
            object = array.getJSONObject(i);
            moneyLogList.add(new MoneyLog(
               object.getInt("Id"),
               object.getString("Name"),
               object.getInt("Amount"),
               object.getString("Note"),
               object.getInt("Type"),
               object.getString("Date")
            ));
        }
    }
    private void JsonToObject(String result) throws JSONException {
        Log.i("result",result);
        JSONObject object = new JSONObject(result);
        moneyLogList.add(new MoneyLog(
                object.getInt("Id"),
                object.getString("Name"),
                object.getInt("Amount"),
                object.getString("Note"),
                object.getInt("Type"),
                object.getString("Date")
        ));
    }

    private class DoPost extends AsyncTask<String, Void, Integer>{
        @Override
        protected Integer doInBackground(String... strings) {
            String urlString = strings[0];
            URL url = null;
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            OutputStream outStream;
            int c;
            String result = "";
            try {
                url = new URL(urlString);
                httpURLConnection = (HttpURLConnection) url.openConnection();//truyen vao method
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                httpURLConnection.setRequestProperty("Content-Type","application/json");
                httpURLConnection.setRequestProperty("Accept","application/json");


                JSONObject object = new JSONObject();
                object.put("Name", strings[1]);
                object.put("Amount", strings[2]);
                object.put("Note", strings[3]);
                object.put("Type", strings[4]);

                outStream = new BufferedOutputStream(httpURLConnection.getOutputStream());
                outStream.write(object.toString().getBytes(Charset.forName("UTF-8")));
                outStream.flush();
                outStream.close();

                inputStream = httpURLConnection.getInputStream();

                //khác -1 là vẫn còn
                while ((c=inputStream.read()) != -1){
                    result+=(char)c;
                }
                Log.i("ThanhCong", result);
                JsonToObject(result);
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
            Log.i("CODE", String.valueOf(integer));
            if(integer == 400){
                Toast.makeText(getApplicationContext(), "That bai", Toast.LENGTH_SHORT).show();
            }else if(integer == 200){
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Thanh cong",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater  inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }
}
