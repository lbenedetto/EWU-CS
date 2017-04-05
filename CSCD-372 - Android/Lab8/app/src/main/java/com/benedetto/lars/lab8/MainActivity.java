package com.benedetto.lars.lab8;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnDragListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView X = (ImageView) findViewById(R.id.imageViewX);
        ImageView O = (ImageView) findViewById(R.id.imageViewO);
        X.setOnTouchListener(this);
        O.setOnTouchListener(this);
        X.setTag(R.drawable.x);
        O.setTag(R.drawable.o);
        ImageView temp;
        boolean restore = savedInstanceState != null;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String id = "imageView" + i + "" + j;
                temp = (ImageView) findViewById(getResources().getIdentifier(id, "id", getPackageName()));
                temp.setOnDragListener(this);
                if (restore) {
                    int drawableID = savedInstanceState.getInt(i + "" + j);
                    temp.setTag(drawableID);
                    temp.setImageResource(drawableID);
                } else {
                    temp.setTag(R.drawable.blank);
                }
            }
        }
        Button b = (Button) findViewById(R.id.buttonReset);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        String id = "imageView" + i + "" + j;
                        ImageView temp = (ImageView) findViewById(getResources().getIdentifier(id, "id", getPackageName()));
                        temp.setTag(R.drawable.blank);
                        temp.setImageResource(R.drawable.blank);
                    }
                }
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
            Toast.makeText(this, "Lab 8, Fall 2016, Lars B Benedetto", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(null, dragShadowBuilder, v, 0);
            return true;
        }
        return false;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        if (event.getAction() == DragEvent.ACTION_DROP) {
            ImageView src = (ImageView) event.getLocalState();
            ImageView dest = (ImageView) v;
            int destTag = (int) dest.getTag();
            int srcTag = (int) src.getTag();
            if (destTag == R.drawable.blank) {
                dest.setImageResource((int) src.getTag());
                dest.setTag(srcTag);
            } else {
                Toast.makeText(this, "Cannot play cell that has already been played", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String id = "imageView" + i + "" + j;
                ImageView temp = (ImageView) findViewById(getResources().getIdentifier(id, "id", getPackageName()));
                outState.putInt(i + "" + j, (int) temp.getTag());
            }
        }
    }
}
