package com.benedetto.lars.lab1;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends TracerActivity {
    private static int intentCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        if(intent != null){
            String actionString = intent.getAction();
            String typeString = intent.getType();
            if(actionString.equals(Intent.ACTION_SEND) && typeString.equals("text/plain")) {
                ((TextView) findViewById(R.id.textViewResults)).setText(intent.getStringExtra(Intent.EXTRA_TEXT));
            }

        }
        final Button buttonSurvey = (Button) findViewById(R.id.buttonTakeSurvey);
        buttonSurvey.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = ((EditText) findViewById(R.id.editTextFirstName)).getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.enter_name), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, SurveyActivity.class);
                    intent.putExtra("name", name);
                    intentCode++;
                    startActivityForResult(intent, intentCode);
                }
            }
        });
        final Button buttonWebpage = (Button) findViewById(R.id.buttonOpenWebsite);
        buttonWebpage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://sites.google.com/site/pschimpf99/")));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == intentCode) {
            if (resultCode == RESULT_OK) {
                int age = data.getIntExtra("age", -1);
                TextView result = (TextView) findViewById(R.id.textViewResults);
                if (age < 40) {
                    result.setText(getString(R.string.under_40));
                } else {
                    result.setText(getString(R.string.over_40));
                }
            }
        }
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
            Toast.makeText(this, getString(R.string.about_text), Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
