package com.svalero.toteco_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.svalero.toteco_app.database.AppDatabase;
import com.svalero.toteco_app.domain.Establishment;

import java.util.List;
import java.util.Objects;

public class LocationsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    private GoogleMap map;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "toteco").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.locations_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void loadEstablishments() {
        try {
            List<Establishment> establishments = db.establishmentDao().findAllExceptAux();
            establishments.stream().forEach(p -> {
                LatLng latLng = new LatLng(p.getLatitude(), p.getLongitude());
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .snippet(getString(R.string.add_publication_establishment_punctuation_print, String.valueOf(p.getPunctuation())))
                        .title(p.getName()));
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        googleMap.setOnMarkerClickListener(this);

        // give the permissions to access to users device
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        googleMap.setMyLocationEnabled(true);

        loadEstablishments();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}