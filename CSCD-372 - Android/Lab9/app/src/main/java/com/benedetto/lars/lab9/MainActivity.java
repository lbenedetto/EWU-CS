package com.benedetto.lars.lab9;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.google.android.gms.maps.GoogleMap.*;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int markerNum;
    private ArrayList<MarkerOptions> markerOptions;
    private ArrayList<Marker> markers;
    Context THIS = this;
    private float zoom;
    private static final int PERMISSION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            markerNum = savedInstanceState.getInt("markerNum");
            markerOptions = (ArrayList<MarkerOptions>) savedInstanceState.getSerializable("markerOptions");
            zoom = savedInstanceState.getFloat("zoom");
        } else {
            zoom = 13f;
            markerNum = 1;
            markerOptions = new ArrayList<>();
        }
        markers = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button buttonType = (Button) findViewById(R.id.buttonChangeType);
        buttonType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = mMap.getMapType();
                switch (type) {
                    case MAP_TYPE_NORMAL:
                        mMap.setMapType(MAP_TYPE_SATELLITE);
                        break;
                    case MAP_TYPE_SATELLITE:
                        mMap.setMapType(MAP_TYPE_TERRAIN);
                        break;
                    case MAP_TYPE_TERRAIN:
                        mMap.setMapType(MAP_TYPE_NORMAL);
                        break;
                }
            }
        });
        Button buttonMark = (Button) findViewById(R.id.buttonMark);
        buttonMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Location location = getLocation();
                LatLng latLng = getLatLong(location);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Mark" + markerNum);
                markers.add(mMap.addMarker(markerOptions));
                MainActivity.this.markerOptions.add(markerOptions);
                markerNum++;
            }
        });
        buttonMark.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                try {
                    markers.get(markers.size() - 1).remove();
                    markers.remove(markers.size() - 1);
                    markerOptions.remove(markerOptions.size() - 1);
                    markerNum -= 1;
                }catch(Exception e){
                    //Ignore
                }
                return true;
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markerOptions or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        initMap();
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void initMap() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // we don’t yet have permission, so request it and return
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Location location = getLocation();
                if (location != null)
                    moveToLocation(location);
                else
                    Toast.makeText(THIS, "Could not get location", Toast.LENGTH_SHORT).show();
            }
        };
        Handler handler = new Handler();
        handler.postDelayed(runnable, 200);
        for (MarkerOptions markerOptions : this.markerOptions) {
            markers.add(mMap.addMarker(markerOptions));
        }
        LatLng latLng = getLatLong(getLocation());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.animateCamera(cameraUpdate);
    }

    private void moveToLocation(Location location) {
        LatLng latLng = getLatLong(location);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 13f);
        mMap.animateCamera(cameraUpdate);
    }

    private LatLng getLatLong(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        return new LatLng(lat, lng);
    }

    private Location getLocation() {
        // first try getting it from the map (if have one - this is now deprecated)
        Location location = mMap.getMyLocation();
        // if that fails, try using a FINE Criteria
        LocationManager locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (location == null) {
            String provider = getProvider(locMgr, Criteria.ACCURACY_FINE, GPS_PROVIDER);
            try {
                location = locMgr.getLastKnownLocation(provider);
            } catch (SecurityException e) {
                Log.e("SHOULDN'T HAPPEN", "Security Exception: " + e.getMessage());
            }
        }
        // if FINE failed, try COARSE
        if (location == null) {
            String provider = getProvider(locMgr, Criteria.ACCURACY_COARSE,
                    NETWORK_PROVIDER);
            try {
                location = locMgr.getLastKnownLocation(provider);
            } catch (SecurityException e) {
                Log.e("SHOULDN'T HAPPEN", "Security Exception: " + e.getMessage());
            }
        }
        return location;
    }

    private String getProvider(LocationManager locMgr, int accuracy, String defProvider) {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(accuracy);
        // get best provider regardless of whether it is enabled
        String providerName = locMgr.getBestProvider(criteria, false);
        if (providerName == null)
            providerName = defProvider;
        // if that provider is not enabled, prompt user to change settings
        if (!locMgr.isProviderEnabled(providerName)) {
            View parent = findViewById(R.id.rootLayout);
            Snackbar snack = Snackbar.make(parent,
                    "Location Provider Not Enabled: Goto Settings?", Snackbar.LENGTH_LONG);
            snack.setAction("Confirm", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            snack.show();
        }
        return providerName;
    }

    @Override
    public void onRequestPermissionsResult(int rqst, String perms[], int[] res) {
        if (rqst == PERMISSION_REQUEST) {
            // if the request is cancelled, the result arrays are empty.
            if (res.length > 0 && res[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted! We can now init the map
                initMap();
            } else {
                Toast.makeText(this, "This app is useless without loc permissions",
                        Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Lab 8, Fall 2016, Lars B Benedetto", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("markerNum", markerNum);
        outState.putSerializable("markerOptions", markerOptions);
        CameraPosition cameraPosition = mMap.getCameraPosition();
        outState.putFloat("zoom", cameraPosition.zoom);
    }
}
