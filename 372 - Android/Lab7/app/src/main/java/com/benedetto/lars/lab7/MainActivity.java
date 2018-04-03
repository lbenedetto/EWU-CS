package com.benedetto.lars.lab7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static BouncyMassView mBouncyMassView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBouncyMassView = (BouncyMassView) findViewById(R.id.viewBouncyMass);
        Button button = (Button) findViewById(R.id.buttonPausePlay);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBouncyMassView.pausePlay();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Toast.makeText(this, "Lab 7, Fall 2016, Lars B Benedetto", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mBouncyMassView.setStiffness(Double.parseDouble(sp.getString("stiffness", "1.5")));
        mBouncyMassView.setCoils(sp.getInt("coils", 11));
        mBouncyMassView.setDisplacement(sp.getInt("displacement", 0));
        mBouncyMassView.setShape(sp.getString("shape", "Rectangle"));
    }
}