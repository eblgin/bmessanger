package com.bilginsoft.user.bmesa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class index extends AppCompatActivity {
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (preferences.contains("IDe")) {
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
            Button btn = (Button) findViewById(R.id.btntel);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), Content.class);
                    startActivity(i);
                }
            });
            Button btnn = (Button)findViewById(R.id.btnlog);
            btnn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(),login.class);
                    startActivity(i);
                }
            });

        }else{
            Button btn = (Button) findViewById(R.id.btntel);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), Content.class);
                    startActivity(i);
                }
            });
            Button btnn = (Button)findViewById(R.id.btnlog);
            btnn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(),login.class);
                    startActivity(i);
                }
            });
        }
    }
}
