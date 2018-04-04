package com.sonhoai.sonho.restful;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
    private TextView txtNothings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        registerForContextMenu(lvMoneyLogs);
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
            case R.id.itemWeek: inCome(); return  true;
            case R.id.itemQuarter: outCome(); return  true;
            default: return false;
        }
    }

    private void init(){
        txtNothings = findViewById(R.id.txtNothings);
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
                final View view1 = inflater.inflate(R.layout.dialog_add, null);
                builder.setView(view1);
                builder.setCancelable(false);

                final EditText edtContent, edtAmount,edtNote;
                final RadioGroup group;
                final RadioButton[] radioType = new RadioButton[1];
                Button btnSave,btnClose;

                btnSave = view1.findViewById(R.id.btnSave);
                btnClose = view1.findViewById(R.id.btnClose);
                edtContent = view1.findViewById(R.id.edtContent);
                edtAmount = view1.findViewById(R.id.edtAmount);
                edtNote = view1.findViewById(R.id.edtNote);
                group = view1.findViewById(R.id.radioType);
                final AlertDialog dialog = builder.show();
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int checked = group.getCheckedRadioButtonId();
                        radioType[0] = view1.findViewById(checked);
                        new DoPost().execute(
                                "http://192.168.1.129:9000/api/MoneyLogs",
                                edtContent.getText().toString(),
                                edtAmount.getText().toString(),
                                edtNote.getText().toString(),
                                radioType[0].getTag().toString()
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
                if(moneyLogList.size() != 0){
                    txtNothings.setVisibility(View.GONE);
                }else {
                    txtNothings.setVisibility(View.VISIBLE);
                }
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
                if(moneyLogList.size() != 0){
                    txtNothings.setVisibility(View.GONE);
                }else {
                    txtNothings.setVisibility(View.VISIBLE);
                }
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
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        if(item.getItemId() == R.id.cmDelete){
            delete(moneyLogList.get(position).getId());
            return true;
        }else if(item.getItemId() == R.id.ctEdit){
           edit(moneyLogList.get(position).getId(), position);
           return true;
        }
        return false;
    }

    private void delete(final int currentId){
        new doDelete().execute("http://192.168.1.129:9000/api/MoneyLogs/"+currentId);
    }
    private void edit(final int currentId,final int idRow){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View view1 = inflater.inflate(R.layout.dialog_add, null);
        builder.setView(view1);
        builder.setCancelable(false);

        final EditText edtContent, edtAmount,edtNote;
        final RadioGroup group;
        final RadioButton[] radioType = new RadioButton[1];
        final RadioButton radioB1;
        final RadioButton radioB2;
        Button btnSave,btnClose;

        radioB1 = view1.findViewById(R.id.radioThu);
        radioB2 = view1.findViewById(R.id.radioChi);

        btnSave = view1.findViewById(R.id.btnSave);
        btnClose = view1.findViewById(R.id.btnClose);

        edtContent = view1.findViewById(R.id.edtContent);
        edtContent.setText(moneyLogList.get(idRow).getName()+"");

        edtAmount = view1.findViewById(R.id.edtAmount);
        edtAmount.setText(moneyLogList.get(idRow).getAmount()+"");
        edtNote = view1.findViewById(R.id.edtNote);
        edtNote.setText(moneyLogList.get(idRow).getNote());

        group = view1.findViewById(R.id.radioType);
        final AlertDialog dialog = builder.show();

        if(moneyLogList.get(idRow).getType() == 0){
            radioB1.setChecked(true);
            radioB2.setChecked(false);
        }else if(moneyLogList.get(idRow).getType() == 1){
            radioB2.setChecked(true);
            radioB1.setChecked(false);
        }
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonObject = new JSONObject();
                try {
                    int checked = group.getCheckedRadioButtonId();
                    radioType[0] = view1.findViewById(checked);
                    jsonObject.put("Id", moneyLogList.get(idRow).getId()+"");
                    jsonObject.put("Name", edtContent.getText().toString());
                    jsonObject.put("Amount", edtAmount.getText().toString());
                    jsonObject.put("Note", edtNote.getText().toString());
                    jsonObject.put("Type", radioType[0].getTag().toString());
                    jsonObject.put("Date", moneyLogList.get(idRow).getDate().toString());
                    String obj = jsonObject.toString();
                    new doUpdate().execute("http://192.168.1.129:9000/api/MoneyLogs/"+currentId, obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }

    private class doDelete extends AsyncTask<String,Void,Integer>{
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
                httpURLConnection.setRequestMethod("DELETE");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                inputStream = httpURLConnection.getInputStream();

                //khác -1 là vẫn còn
                while ((c=inputStream.read()) != -1){
                    result+=(char)c;
                }
            } catch (Exception e) {
                //that bai
                e.printStackTrace();
                return 400;
            }
            String kq = result.substring(1,4).toString();
            if(!kq.equals("200")){
                return 400;
            }else {
                return 200;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            Log.i("CODES", String.valueOf(integer));
            if(integer == 200){
                dialog.setTitle("Thành công!");
                dialog.setMessage("Bạn đã xóa thành công");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        moneyLogList.clear();
                        new DoGets().execute("http://192.168.1.129:9000/api/MoneyLogs");
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
            }else if(integer ==400){
                dialog.setTitle("Thất bại!");
                dialog.setMessage("Xóa thất bại, vui lòng xem lại.");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
            }
        }
    }

    private class doUpdate extends AsyncTask<String,Void,Integer>{
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

                httpURLConnection.setRequestMethod("PUT");

                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                httpURLConnection.setRequestProperty("Content-Type","application/json");
                httpURLConnection.setRequestProperty("Accept","application/json");

                outStream = new BufferedOutputStream(httpURLConnection.getOutputStream());
                Log.i("OBJECT", strings[1]);
                outStream.write(strings[1].getBytes(Charset.forName("UTF-8")));
                outStream.flush();
                outStream.close();

                inputStream = httpURLConnection.getInputStream();

                //khác -1 là vẫn còn
                while ((c=inputStream.read()) != -1){
                    result+=(char)c;
                }
            } catch (Exception e) {
                //that bai
                e.printStackTrace();
                return 400;
            }
            String kq = result.substring(1,4).toString();
            if(!kq.equals("200")){
                return 400;
            }else {
                return 200;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            Log.i("CODES", String.valueOf(integer));
            if(integer == 200){
                dialog.setTitle("Thành công!");
                dialog.setMessage("Bạn đã sửa thành công");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        moneyLogList.clear();
                        new DoGets().execute("http://192.168.1.129:9000/api/MoneyLogs");
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
            }else if(integer ==400){
                dialog.setTitle("Thất bại!");
                dialog.setMessage("Xóa thất bại, vui lòng xem lại.");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();
            }
        }
    }

    private void inCome(){
        showReportDialog(1);
    }
    private void outCome(){
        //method,
        showReportDialog(2);
    }

    private  void showReportDialog(final int filter){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final LayoutInflater inflater = getLayoutInflater();
        final View view1 = inflater.inflate(R.layout.dialog_reports, null);
        builder.setView(view1);
        builder.setCancelable(false);

        final RadioGroup group;
        final RadioButton[] radioType = new RadioButton[1];
        Button btnSave,btnClose;

        btnSave = view1.findViewById(R.id.btnSave);
        btnClose = view1.findViewById(R.id.btnClose);
        group = view1.findViewById(R.id.radioFilter);

        final AlertDialog dialog = builder.show();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checked = group.getCheckedRadioButtonId();
                radioType[0] = view1.findViewById(checked);
                Intent showReport = new Intent(MainActivity.this, ReportActivity.class);
                showReport.putExtra("FILTER", String.valueOf(filter));
                showReport.putExtra("SORT", radioType[0].getTag().toString());
                startActivity(showReport);
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }
}
