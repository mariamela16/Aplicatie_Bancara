package com.example.frag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap gmap;
    FrameLayout map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        map=findViewById((R.id.map));
        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gmap=googleMap;
        LatLng mapRomania=new LatLng(47.643053, 26.237697);
        this.gmap.addMarker(new MarkerOptions().position(mapRomania).title("Ines Bank"));
        this.gmap.moveCamera(CameraUpdateFactory.newLatLng(mapRomania));
    }

    @Override
    public void onBackPressed() {
        // Setează rezultatul pentru activitatea anterioară
        Intent intent = new Intent();
        setResult(MainActivity.RESULT_OK, intent);
        super.onBackPressed();
    }
}