package com.ishan.memorableplaces;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

import static com.ishan.memorableplaces.MainActivity.arrayAdapter;
import static com.ishan.memorableplaces.MainActivity.listView;
import static com.ishan.memorableplaces.MainActivity.locationsCoordinates;
import static com.ishan.memorableplaces.MainActivity.locationsName;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{
    Intent intent;
    private GoogleMap mMap;
    double latitude;
    double longtitude;
    String title;
    int index;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        textView = findViewById(R.id.editTextTextPersonName2);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        intent = getIntent();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        latitude = intent.getDoubleExtra("lat",0.0);
        longtitude = intent.getDoubleExtra("lng",0.0);
        index = intent.getIntExtra("Index",0);
        if(index == 0) {
            LatLng myLocation = new LatLng(latitude, longtitude);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13f));
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    Log.i("Map Long Press", "True");
                    mMap.addMarker(new MarkerOptions().position(latLng).title(getGeoCode(latLng)));
                    updateArrayList(title, latLng);
                    Toast.makeText(MapsActivity.this, "Location Added", Toast.LENGTH_LONG).show();
                }
            });
        }else
        {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationsCoordinates.get(index), 13f));
            mMap.addMarker(new MarkerOptions().position(locationsCoordinates.get(index)).title(locationsName.get(index)));
        }
    }
    public String getGeoCode(LatLng latLng)
    {
        Geocoder geocoder = new Geocoder(this,Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if(addresses == null)
            {
                textView.setVisibility(View.VISIBLE);
            }else
            {
                title += addresses.get(0).getSubThoroughfare();
                if(title == null)
                {
                    title += addresses.get(0).getThoroughfare();
                }
                if(title == null)
                {
                    textView.setVisibility(View.VISIBLE);
                }
            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return title;
    }
    public void updateArrayList(String title,LatLng latLng)
    {
        locationsName.add(title);
        locationsCoordinates.add(latLng);
        arrayAdapter.notifyDataSetChanged();
    }
}