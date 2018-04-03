package com.benedetto.lars.ashman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.benedetto.lars.ashman.enums.Direction.DOWN;
import static com.benedetto.lars.ashman.enums.Direction.LEFT;
import static com.benedetto.lars.ashman.enums.Direction.RIGHT;
import static com.benedetto.lars.ashman.enums.Direction.UP;

public class MainActivity extends AppCompatActivity implements CakeListener {
    private TextView textViewCakes;
    private TextView textViewLevel;
    private MazeView mazeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mazeView = (MazeView) findViewById(R.id.mazeView);
        mazeView.registerCakeListener(this);
        ImageView dpadUp = (ImageView) findViewById(R.id.dpadUp);
        ImageView dpadDown = (ImageView) findViewById(R.id.dpadDown);
        ImageView dpadLeft = (ImageView) findViewById(R.id.dpadLeft);
        ImageView dpadRight = (ImageView) findViewById(R.id.dpadRight);
        dpadUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mazeView.setAshmanDirection(UP);
            }
        });
        dpadDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mazeView.setAshmanDirection(DOWN);
            }
        });
        dpadLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mazeView.setAshmanDirection(LEFT);
            }
        });
        dpadRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mazeView.setAshmanDirection(RIGHT);
            }
        });
        TextView startGame = (TextView) findViewById(R.id.textViewInstructions);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mazeView.pausePlay();
            }
        });
        startGame.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mazeView.cheat();
                return true;
            }
        });
        textViewCakes = (TextView) findViewById(R.id.textViewCakesRemaining);
        textViewLevel = (TextView) findViewById(R.id.textViewLevel);
        String s = getString(R.string.level) + " " + 1;
        textViewLevel.setText(s);
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
            Toast.makeText(this, "Ashman, Fall 2016, Lars B Benedetto", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateCakes(int cakes) {
        String s = getString(R.string.cakes_left) + cakes;
        textViewCakes.setText(s);
        updateLevel();
    }

    private void updateLevel() {
        String s = getString(R.string.level) + " " + mazeView.level;
        textViewLevel.setText(s);
    }
}
