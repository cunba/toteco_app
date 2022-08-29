package com.svalero.toteco_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

public class AddEstablishmentActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap map;
    private AppDatabase db;
    private Establishment establishment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_establishment);

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "toteco").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.add_establishment_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .snippet("new")
                .title("new establishment"));
        addEstablishment(latLng);
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
        googleMap.setOnMapClickListener(this);
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
        EditText etEstablishmentName = findViewById(R.id.add_establishment_name);

        if (!Objects.equals(marker.getSnippet(), "new")) {
            // If the establishment does exists we print the name in the editor
            etEstablishmentName.setText(marker.getTitle());
            etEstablishmentName.setEnabled(false);
            List<Establishment> establishment1 = db.establishmentDao().findByName(marker.getTitle());
            establishment = establishment1.get(0);
        } else {
            etEstablishmentName.setText("");
            etEstablishmentName.setEnabled(true);
        }

        return false;
    }

    private void addEstablishment(LatLng latLng) {
        EditText etEstablishmentName = findViewById(R.id.add_establishment_name);
        etEstablishmentName.setEnabled(true);
        String establishmentName = etEstablishmentName.getText().toString();
        establishment = new Establishment(
                establishmentName,
                latLng.latitude,
                latLng.longitude,
                true,
                0);
    }

    public void onPressSubmit(View view) {
        EditText etEstablishmentName = findViewById(R.id.add_establishment_name);
        EditText etEstablishmentPunctuation = findViewById(R.id.add_establishment_punctuation);
        TextView tvError = findViewById(R.id.add_establishment_error);

        String establishmentName = etEstablishmentName.getText().toString();
        String sEstablishmentPunctuation = etEstablishmentPunctuation.getText().toString();

        if (establishmentName.equals("") || sEstablishmentPunctuation.equals("")) {
            tvError.setText(R.string.error_field_empty);
        } else {
            float establishmentPunctuation = Float.parseFloat(sEstablishmentPunctuation);

            if (establishmentPunctuation > 5) {
                tvError.setText(R.string.add_product_error_punctuation);
            } else {
                if (establishment.getId() == 0) {
                    establishment.setName(establishmentName);
                    establishment.setPunctuation(establishmentPunctuation);
                } else {
                    int establishmentPublications = db.establishmentDao().countPublicationsByEstablishmentId(establishment.getId());
                    float punctuation = (establishment.getPunctuation() + establishmentPunctuation) / establishmentPublications;
                    establishment.setPunctuation(punctuation);
                }

                establishment.setId(1);
                db.establishmentDao().update(establishment);

                tvError.setText("");
                etEstablishmentName.setText("");
                etEstablishmentName.setEnabled(true);
                etEstablishmentPunctuation.setText("");

                Intent intent = new Intent(this, AddPublicationActivity.class);
                startActivity(intent);
            }
        }
    }
}