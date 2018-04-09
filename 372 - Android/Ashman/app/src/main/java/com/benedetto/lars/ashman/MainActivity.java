package com.benedetto.lars.ashman;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
        mazeView = findViewById(R.id.mazeView);
        mazeView.registerCakeListener(this);
        ImageView dpadUp = findViewById(R.id.dpadUp);
        ImageView dpadDown = findViewById(R.id.dpadDown);
        ImageView dpadLeft = findViewById(R.id.dpadLeft);
        ImageView dpadRight = findViewById(R.id.dpadRight);
        dpadUp.setOnClickListener(v -> mazeView.setAshmanDirection(UP));
        dpadDown.setOnClickListener(v -> mazeView.setAshmanDirection(DOWN));
        dpadLeft.setOnClickListener(v -> mazeView.setAshmanDirection(LEFT));
        dpadRight.setOnClickListener(v -> mazeView.setAshmanDirection(RIGHT));
        TextView startGame = findViewById(R.id.textViewInstructions);
        startGame.setOnClickListener(v -> mazeView.pausePlay());
        startGame.setOnLongClickListener(v -> {
            mazeView.cheat();
            return true;
        });
        textViewCakes = findViewById(R.id.textViewCakesRemaining);
        textViewLevel = findViewById(R.id.textViewLevel);
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
