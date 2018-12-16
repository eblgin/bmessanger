package com.bilginsoft.user.bmesa;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class mesajl extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ArrayList<String> mesaj,gonderId,date;
    EditText editText;
    String mesajs;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesajl);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        editText = (EditText)findViewById(R.id.edtTe);
        listView = (ListView)findViewById(R.id.lstv);


        final NetManagerr net = new NetManagerr();
        net.execute("Your Url");
        Button b = (Button)findViewById(R.id.bnsen);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetManager manager = new NetManager();
                mesajs = editText.getText().toString();
                manager.execute("Your Url");
                NetManagerr netManagerr = new NetManagerr();
                netManagerr.execute("Your Url");
            }
        });
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){

                    try {
                        NetManagerr nett = new NetManagerr();
                        nett.execute("Your Url");
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }



    public class NetManager extends AsyncTask<String,Void,String> {
        private JSONArray array;
        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                String data = URLEncoder.encode("gondId","UTF-8")+"="+URLEncoder.encode(preferences.getString("IDe",""),"UTF-8");
                data += "&"+URLEncoder.encode("kisiId","UTF-8")+"="+URLEncoder.encode(preferences.getString("userID",""),"UTF-8");
                data += "&"+URLEncoder.encode("mesaj","UTF-8")+"="+URLEncoder.encode(mesajs,"UTF-8");
                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(data);
                osw.flush();
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String text = "";
                String line;
                while ((line = rd.readLine())!=null){
                    text+=line;
                }
                rd.close();
                osw.close();
                return text;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                /*Toast.makeText(getApplicationContext(), userdizi.toString(),Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), IDdizi.toString(),Toast.LENGTH_SHORT).show();*/




        }
    }
    public class NetManagerr extends AsyncTask<String,Void,String> {
        private JSONArray array;
        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                String data = URLEncoder.encode("gondId","UTF-8")+"="+URLEncoder.encode(preferences.getString("IDe",""),"UTF-8");
                data+="&"+URLEncoder.encode("kisiId","UTF-8")+"="+URLEncoder.encode(preferences.getString("userID",""),"UTF-8");
                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(data);
                osw.flush();
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String text = "";
                String line;
                while ((line = rd.readLine())!=null){
                    text+=line;
                }
                rd.close();
                osw.close();
                return text;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            mesaj = new ArrayList<String>();
            gonderId = new ArrayList<String>();
            date = new ArrayList<String>();
            try {
                array = new JSONArray(s);

                for (int i = 0; i<array.length();i++){
                    JSONObject object = array.getJSONObject(i);
                    mesaj.add(object.getString("mesaj"));
                    gonderId.add(object.getString("gonderenId"));
                    date.add(object.getString("tarih"));

                }
                CusAdapt adapt = new CusAdapt();
                adapt.notifyDataSetChanged();
                listView.setAdapter(adapt);
                //Toast.makeText(getApplicationContext(),mesaj.toString(),Toast.LENGTH_SHORT).show();
                /*Toast.makeText(getApplicationContext(), userdizi.toString(),Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), IDdizi.toString(),Toast.LENGTH_SHORT).show();*/


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public class CusAdapt extends BaseAdapter{

        @Override
        public int getCount() {
            return mesaj.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(gonderId.get(position).equals(preferences.getString("IDe",""))){
                convertView = getLayoutInflater().inflate(R.layout.oursendbox,null);
                TextView txtv = (TextView)convertView.findViewById(R.id.message_body);
                txtv.setText(mesaj.get(position));
            }else{
                convertView = getLayoutInflater().inflate(R.layout.yoursendbox,null);
                TextView txt = (TextView)convertView.findViewById(R.id.message_bodyy);
                txt.setText(mesaj.get(position));
            }
            return convertView;
        }
    }
}
