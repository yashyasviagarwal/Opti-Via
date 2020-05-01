package com.boss.routedirectionsapp;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import androidx.fragment.app.FragmentActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    protected GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(helper.waypoints.get(0)).title("Starting Position"));
        mMap.addMarker(new MarkerOptions().position(helper.waypoints.get(helper.waypoints.size()-1)).title("Destination"));
        PolylineOptions polylineOptions = new PolylineOptions();
        for (int i = 0; i < helper.waypoints.size(); i++)
        {
            polylineOptions.add(helper.waypoints.get(i));
        }
        polylineOptions.width(8f);
        polylineOptions.color(Color.BLUE);
        mMap.addPolyline(polylineOptions);
        CircleOptions circleOptions1 = new CircleOptions();
        circleOptions1.center(helper.waypoints.get(0));
        circleOptions1.radius(250.0);
        circleOptions1.strokeColor(Color.BLUE);
        circleOptions1.strokeWidth(3f);
        circleOptions1.fillColor(Color.argb(70,50,150,50));
        CircleOptions circleOptions2 = new CircleOptions();
        circleOptions2.center(helper.waypoints.get(helper.waypoints.size()-1));
        circleOptions2.radius(250.0);
        circleOptions2.strokeColor(Color.GREEN);
        circleOptions2.strokeWidth(3f);
        circleOptions2.fillColor(Color.argb(70,50,150,50));
        mMap.addCircle(circleOptions1);
        mMap.addCircle(circleOptions2);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(helper.waypoints.get(0),16f));
        mMap.setTrafficEnabled(true);
        helper.waypoints.clear();
    }
}
