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

public class Content extends AppCompatActivity {
    EditText name,surname,username,password1,password2,email;
    Button save;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String names,surnames,usernames,password1s,password2s,emails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        //if (preferences.contains("Ide")) {

            name = (EditText) findViewById(R.id.name);
            surname = (EditText) findViewById(R.id.surname);
            username = (EditText) findViewById(R.id.username);
            password1 = (EditText) findViewById(R.id.password1);
            password2 = (EditText) findViewById(R.id.password2);
            email = (EditText) findViewById(R.id.emal);
            save = (Button) findViewById(R.id.btnsave);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    names = name.getText().toString();
                    surnames = username.getText().toString();
                    usernames = username.getText().toString();
                    password1s = password1.getText().toString();
                    password2s = password2.getText().toString();
                    emails = email.getText().toString();
                    if (names.equals("")) {
                        name.setError("Bu alan boş bırakılamaz.");
                    }
                    if (surnames.equals("")) {
                        surname.setError("Bu alan boş bıakılamaz.");
                    }
                    if (usernames.equals("")) {
                        username.setError("Bu alan boş bıakılamaz.");
                    }
                    if (password1s.equals("")) {
                        password1.setError("Bu alan boş bıakılamaz.");
                    }
                    if (password2s.equals("")) {
                        password2.setError("Bu alan boş bıakılamaz.");
                    }
                    if (email.equals("")) {
                        email.setError("Bu alan boş bıakılamaz.");
                    }
                    if (!names.equals("") && !surnames.equals("") && !usernames.equals("") && !password1s.equals("") && !password2s.equals("")) {
                        if (password2s.equals(password1s)) {
                            NetManager manager = new NetManager();
                            manager.execute("You Url");
                        } else {
                            password1.setError("Bu alanlar eşit olmalıdır");
                            password2.setError("Bu alanlar eşit olmalıdır");
                        }
                    }
                }
            });
        /*}else{
            Intent i = new Intent(Content.this,MainActivity.class);
            startActivity(i);
        }*/
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
                String data = URLEncoder.encode("telno","UTF-8")+"="+URLEncoder.encode(emails,"UTF-8");
                data+="&"+URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(names,"UTF-8");
                data+="&"+URLEncoder.encode("surname","UTF-8")+"="+URLEncoder.encode(surnames,"UTF-8");
                data+="&"+URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(usernames,"UTF-8");
                data+="&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password1s,"UTF-8");
                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(data);
                osw.flush();
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String text = "";
                String line;
                while ((line = rd.readLine())!=null){
                    text+=line;
                }
                osw.close();
                rd.close();
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
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                editor.putString("IDe",usernames);
                editor.commit();
                Toast.makeText(getApplicationContext(),"Başarıyla giriş yapıldı",Toast.LENGTH_SHORT).show();

            Intent ii = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(ii);
        }
    }
}
