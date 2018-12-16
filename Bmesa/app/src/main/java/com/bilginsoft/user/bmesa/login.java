package com.bilginsoft.user.bmesa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
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

public class login extends AppCompatActivity {
    EditText edtpass,edtuser;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtuser = (EditText)findViewById(R.id.edtusername);
        edtpass = (EditText)findViewById(R.id.edtpassword);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        Button btn = (Button)findViewById(R.id.btnuser);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetManagerr nr = new NetManagerr();
                nr.execute("https://bmessanger.com/sorgula.php");
            }
        });

    }
    public class NetManagerr extends AsyncTask<String,Void,String> {
        private JSONArray array;
        private String kulad,pass;
        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                String data = URLEncoder.encode("kullanad","UTF-8")+"="+URLEncoder.encode(kulad,"UTF-8");
                data+="&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8");
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
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            kulad = edtuser.getText().toString();
            pass = edtpass.getText().toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
                if(s.equals("1")){
                    Toast.makeText(getApplicationContext(),"Giriş yaıldı",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(login.this,MainActivity.class));
                    editor.putString("IDe",kulad);
                    editor.commit();

                }else{
                    Toast.makeText(getApplicationContext(),"Kullanıcı adı veya şifre yanlış",Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(),mesaj.toString(),Toast.LENGTH_SHORT).show();
                /*Toast.makeText(getApplicationContext(), userdizi.toString(),Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), IDdizi.toString(),Toast.LENGTH_SHORT).show();*/

        }
    }
}
