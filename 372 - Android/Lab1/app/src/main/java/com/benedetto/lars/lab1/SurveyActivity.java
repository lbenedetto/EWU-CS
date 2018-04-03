package com.benedetto.lars.lab1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SurveyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        String helloText = getString(R.string.hello) + " " + getIntent().getStringExtra("name");
        ((TextView) findViewById(R.id.textViewHello)).setText(helloText);
        final Button buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Submit button clicked");
                String input = ((EditText) findViewById(R.id.editTextAge)).getText().toString();
                if (input.equals("")) {
                    Toast.makeText(SurveyActivity.this, getResources().getString(R.string.must_enter_age), Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        int age = Integer.parseInt(input);
                        Intent intent = new Intent().putExtra("age", age);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    } catch (NumberFormatException e) {
                        Toast.makeText(SurveyActivity.this, getResources().getString(R.string.invalid_age), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
