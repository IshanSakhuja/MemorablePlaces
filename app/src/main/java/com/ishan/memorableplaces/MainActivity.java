package com.ishan.memorableplaces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static ListView listView;
    static ArrayAdapter arrayAdapter;
    Intent intent;
    static ArrayList<String> locationsName;
    static ArrayList<LatLng> locationsCoordinates;
    static LocationManager locationManager;
    LocationListener locationListener;
    Location myLocation;
    int locationFetched = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationsCoordinates = new ArrayList<>();
        locationsCoordinates.add(new LatLng(0.0,0.0));
        requestPermission();
        listView = findViewById(R.id.listView);
        locationsName = new ArrayList<>();
        locationsName.add("Add new Place");
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,locationsName);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0)
                {
                    intent = new Intent(MainActivity.this,MapsActivity.class);
                    intent.putExtra("lat",myLocation.getLatitude());
                    intent.putExtra("lng",myLocation.getLongitude());
                    intent.putExtra("Index",i);
                    startActivity(intent);

                }else
                {

                }
            }
        });
    }

    public void requestPermission() {
        Log.i("Start2","Starting of Application");
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
            }else
            {
                getLocation();
            }
        } else {
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("Start3","Starting of Application");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
            getLocation();
        } else {
            Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
        }
    }
        public void getLocation() {
            Log.i("Start4","Starting of Application");
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    myLocation = location;
                    Log.i("Location Fetched","Location");
                }
            };
            if (Build.VERSION.SDK_INT >= 23) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener);
                }
            }
        }
}