package com.bilginsoft.user.bmesa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> userdizi = new ArrayList<String>();
    ArrayList<String> IDdizi = new ArrayList<String>();
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    int x;

    ArrayList<String> arrayList = new ArrayList<>();
    private ListView listView;
    private void listView_ItemClick(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinearLayout lay = (LinearLayout)view;
                TextView txtisim = (TextView)lay.findViewById(R.id.ctxtv);
                editor.putString("userID",txtisim.getText().toString());
                editor.commit();
                Toast.makeText(getApplicationContext(),txtisim.getText().toString(),Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this,mesajl.class);
                startActivity(i);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"onlongItem",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        Toast.makeText(getApplicationContext(),preferences.getString("IDe",""),Toast.LENGTH_SHORT).show();
        new NetManager().execute("https://bmessanger.com/denemejson.php");

        Toast.makeText(getApplicationContext(),userdizi.toString(),Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.cikit){
            editor.remove("IDe");
            editor.commit();
            startActivity(new Intent(MainActivity.this,index.class));
        }
        return true;
    }

    public class NetManager extends AsyncTask<String,Void,String> {
        private JSONArray array;
        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setDoOutput(true);
                BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String text = "";
                String line;
                while ((line = rd.readLine())!=null){
                    text+=line;
                }
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
            try {
                array = new JSONArray(s);

                for (int i = 0; i<array.length();i++){
                    JSONObject object = array.getJSONObject(i);
                    userdizi.add(object.getString("username"));
                    IDdizi.add(object.getString("ID"));
                    arrayList.add(object.getString("Resim"));
                }
                /*Toast.makeText(getApplicationContext(), userdizi.toString(),Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), IDdizi.toString(),Toast.LENGTH_SHORT).show();*/
                x = 0;
                CustomAdapter adapter = new CustomAdapter();
                listView = (ListView)findViewById(R.id.lstView);
                listView.setAdapter(adapter);
                listView_ItemClick();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return userdizi.size();
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
            convertView = getLayoutInflater().inflate(R.layout.customlayout,null);
            TextView textView = (TextView)convertView.findViewById(R.id.ctxtv);
            if(preferences.getString("IDe","").equals(userdizi.get(position))){

            }else {

                textView.setText(userdizi.get(position));
                if (arrayList.get(position).equals("")) {
                    ImageView ımageView = (ImageView) convertView.findViewById(R.id.imgv);

                    ımageView.setImageResource(R.drawable.res);
                } else {
                    ImageView img = (ImageView) convertView.findViewById(R.id.imgv);
                    byte[] decodedString = Base64.decode(arrayList.get(position), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    img.setImageBitmap(decodedByte);
                }

            }
            return convertView;
        }
    }
}
