package com.benedetto.lars.lab3;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    ArrayList<Manufacturer> manufacturers = new ArrayList<>();
    ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!parseFile("database.txt"))
            Toast.makeText(this, "Parsing database failed", Toast.LENGTH_SHORT).show();
        else {
            if(savedInstanceState!=null){
                ArrayList<Manufacturer> newManufacturers = new ArrayList<>();
                for(Manufacturer m : manufacturers){
                    newManufacturers.add(new Manufacturer(m.getManufacturer(), (ArrayList<String>)savedInstanceState.get(m.getManufacturer())));
                }
                manufacturers = newManufacturers;
            }
            listAdapter = new ListAdapter(this, manufacturers);
            ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
            expandableListView.setAdapter(listAdapter);
            final AppCompatActivity jankyHack = this;
            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    Manufacturer manufacturer = (Manufacturer) listAdapter.getGroup(groupPosition);
                    String s = "Manufacturer: " + manufacturer.getManufacturer() + ", Model: " + manufacturer.getModel(childPosition);
                    Toast.makeText(jankyHack, s, Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
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
            Toast.makeText(this, "Lab 3, Fall 2016, Lars B Benedetto", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean parseFile(String filename) {
        AssetManager assetManager = getResources().getAssets();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(assetManager.open(filename)));
            String line;
            while ((line = in.readLine()) != null) {
                String[] strings = line.split(",");
                Manufacturer manufacturer = new Manufacturer(strings[0]);
                for (int i = 1; i < strings.length; i++) {
                    manufacturer.addModel(strings[i]);
                }
                manufacturers.add(manufacturer);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle out){
        ArrayList<Manufacturer> mans = listAdapter.getManufacturers();
        for(Manufacturer m : mans){
            out.putStringArrayList(m.getManufacturer(), m.getModels());
        }
    }
}