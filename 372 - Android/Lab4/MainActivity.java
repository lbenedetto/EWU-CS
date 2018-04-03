package com.benedetto.lars.lab4;

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
        final SevenSegmentView sevenSegmentViewTop = (SevenSegmentView) findViewById(R.id.sevenSegmentTop);
        final SevenSegmentView sevenSegmentViewOne = (SevenSegmentView) findViewById(R.id.sevenSegmentOne);
        final SevenSegmentView sevenSegmentViewTwo = (SevenSegmentView) findViewById(R.id.sevenSegmentTwo);
        final SevenSegmentView sevenSegmentViewThree = (SevenSegmentView) findViewById(R.id.sevenSegmentThree);
        sevenSegmentViewOne.setValue(1);
        sevenSegmentViewTwo.setValue(2);
        sevenSegmentViewThree.setValue(3);
        Button buttonIncrement = (Button) findViewById(R.id.buttonIncrement);
        buttonIncrement.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                sevenSegmentViewTop.incrementValue();
                sevenSegmentViewOne.incrementValue();
                sevenSegmentViewTwo.incrementValue();
                sevenSegmentViewThree.incrementValue();
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
            Toast.makeText(this, "Lab 4, Fall 2016, Lars B Benedetto", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}