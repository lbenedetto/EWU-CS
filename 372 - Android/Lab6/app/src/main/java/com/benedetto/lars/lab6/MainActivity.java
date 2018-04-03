package com.benedetto.lars.lab6;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MainFragment.callback {
    private static final String mTagMainFragment = "MAIN_FRAGMENT";
    private static final String mTagDetailFragment = "DETAIL_FRAGMENT";
    private static final String mTimestamp = "TIMESTAMP";
    private String lastTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!getResources().getBoolean(R.bool.dual_pane)) {
            FragmentManager manager = getSupportFragmentManager();
            if (manager.findFragmentById(R.id.portHolder) == null) {
                MainFragment mainFragment = MainFragment.newInstance();
                manager.beginTransaction()
                        .add(R.id.portHolder, mainFragment, mTagMainFragment)
                        .commit();
            }
            DetailFragment detailFragment = (DetailFragment) manager.findFragmentByTag(mTagDetailFragment);
            if (detailFragment != null && savedInstanceState != null) {
                detailFragment.updateTimeStamp(savedInstanceState.getString(mTimestamp));
            }
        } else {
            if (savedInstanceState != null) {
                DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentDetailHolder);
                detailFragment.updateTimeStamp(savedInstanceState.getString(mTimestamp));
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
            Toast.makeText(this, "Lab 6, Fall 2016, Lars B Benedetto", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String timestamp) {
        lastTimestamp = timestamp;
        DetailFragment detailFragment;
        if (getResources().getBoolean(R.bool.dual_pane)) {
            detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentDetailHolder);
            detailFragment.updateTimeStamp(timestamp);
        } else {
            detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentByTag(mTagDetailFragment);
            if (detailFragment == null) {
                detailFragment = DetailFragment.newInstance(timestamp);
                Log.e("onFragmentInteraction", "Created new detail fragment");
            } else {
                Log.e("onFragmentInteraction", "Found existing detail fragment");
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.portHolder, detailFragment, mTagDetailFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(mTimestamp, lastTimestamp);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        lastTimestamp = savedInstanceState.getString(mTimestamp);
    }
    @Override
    public void onBackPressed(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(getResources().getBoolean(R.bool.dual_pane) && fragmentManager.getBackStackEntryCount() > 0){
            fragmentManager.popBackStack();
        }
        super.onBackPressed();
    }
}
