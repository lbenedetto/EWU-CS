package com.benedetto.lars.lab5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BouncyMassView bouncyMassView = (BouncyMassView) findViewById(R.id.viewBouncyMass);
        Button button = (Button) findViewById(R.id.buttonPausePlay);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bouncyMassView.pausePlay();
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
            Toast.makeText(this, "Lab 5, Fall 2016, Lars B Benedetto", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}