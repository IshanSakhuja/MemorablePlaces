package com.ishan.memorableplaces;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnKeyListener {
    Intent intent;
    private GoogleMap mMap;
    double latitude;
    double longtitude;
    String title;
    int index;
    EditText textView;
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
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f));
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    mMap.addMarker(new MarkerOptions().position(latLng).title(getGeoCode(latLng)));
                    updateArrayList(title, latLng);
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
                textView.setOnKeyListener(this);
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
                    textView.setOnKeyListener(this);
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
        Toast.makeText(MapsActivity.this, "Location Added", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(i == KeyEvent.KEYCODE_ENTER)
        {
            EditText editText = (EditText)view;
            title = editText.getText().toString();
        }
        return false;
    }
}