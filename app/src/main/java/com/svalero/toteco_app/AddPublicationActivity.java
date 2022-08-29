package com.svalero.toteco_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.svalero.toteco_app.database.AppDatabase;
import com.svalero.toteco_app.domain.Establishment;
import com.svalero.toteco_app.domain.Product;
import com.svalero.toteco_app.domain.Publication;
import com.svalero.toteco_app.util.ImageAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddPublicationActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    public List<Product> products;
    private ArrayAdapter<Product> productsAdapter;
    private AppDatabase db;
    private GoogleMap map;

    private Establishment establishment;
    double totalPrice = 0;
    double totalPunctuation = 0;

    private final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_publication);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.add_publication_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        products = new ArrayList<>();
        ListView lvProducts = findViewById(R.id.add_publication_product_list);
        productsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products);
        lvProducts.setAdapter(productsAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

         db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "toteco").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();
        //Delete unsaved products
        try {
            db.productDao().deleteByPublicationId(1);
        } catch (Exception e) {
            System.out.println("no hay publicacion auxiliar");
        }

        Intent intent = new Intent(this, PublicationsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshList();
        makeSummary();
    }

    public void refreshList() {
        loadProducts();
        productsAdapter.notifyDataSetChanged();
    }

    private void loadProducts() {
        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "toteco").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();

        products.clear();

        try {
            products.addAll(db.productDao().findByPublicationId(1));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void makeSummary() {
        totalPrice = products.stream()
                .map(Product::getPrice)
                .mapToDouble(price -> price)
                .sum();

        if (products.size() != 0) {
            totalPunctuation = products.stream()
                    .map(Product::getPunctuation)
                    .mapToDouble(punctuation -> punctuation)
                    .sum() / products.size();
        }

        TextView tvTotalPrice = findViewById(R.id.add_publication_total_price);
        tvTotalPrice.setText(getString(R.string.add_publication_total_price, String.valueOf(totalPrice)));

        TextView tvTotalPunctuation = findViewById(R.id.add_publication_total_punctuation);
        tvTotalPunctuation.setText(getString(R.string.add_publication_total_punctuation, String.valueOf(totalPunctuation)));
    }

    public void onPressAddProduct(View view) {
        Intent intent = new Intent(this, AddProductActivity.class);
        startActivity(intent);
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
        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "toteco").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();

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
        EditText etEstablishmentName = findViewById(R.id.add_publication_establishment_name);
        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "toteco").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();

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
        EditText etEstablishment = findViewById(R.id.add_publication_establishment_name);
        etEstablishment.setEnabled(true);
        String establishmentName = etEstablishment.getText().toString();
        establishment = new Establishment(
                establishmentName,
                latLng.latitude,
                latLng.longitude,
                true,
                0);
    }

    public void onPressAddImage(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void onPressSubmit(View view) {
        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "toteco").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();

        EditText etEstablishmentName = findViewById(R.id.add_publication_establishment_name);
        EditText etEstablishmentPunctuation = findViewById(R.id.add_publication_establishment_punctuation);
        TextView tvError = findViewById(R.id.add_publication_error);
        ImageView productImageView = findViewById(R.id.add_publication_image);

        String sEstablishmentPunctuation = etEstablishmentPunctuation.getText().toString();
        String establishmentName = etEstablishmentName.getText().toString();

        if (sEstablishmentPunctuation.equals("") || establishmentName.equals("")) {
            tvError.setText(R.string.error_field_empty);
        } else if (products.size() == 0) {
            tvError.setText(R.string.error_products_empty);
        } else {
            tvError.setText("");
            float establishmentPunctuation = Float.parseFloat(sEstablishmentPunctuation);
            byte[] publicationImage = ImageAdapter.fromImageViewToByteArray(productImageView);

            // if the establishment doesnt exists we create it
            if (establishment.getId() == 0) {
                establishment.setName(establishmentName);
                establishment.setPunctuation(establishmentPunctuation);
                db.establishmentDao().insert(establishment);
                establishment = db.establishmentDao().findLast();
                System.out.println(establishment.getName());
            }

            Publication publication = new Publication(
                    Float.parseFloat(String.valueOf(totalPrice)),
                    Float.parseFloat(String.valueOf(totalPunctuation)),
                    1,
                    establishment.getId());

            db.publicationDao().insert(publication);
            Publication addedPublication = db.publicationDao().findLast();

            products.stream().forEach(p -> {
                p.setPublicationId(addedPublication.getId());
                db.productDao().update(p);
                System.out.println(p.getName());
            });

            Toast.makeText(this, R.string.publication_created, Toast.LENGTH_SHORT).show();

            etEstablishmentName.setText("");
            etEstablishmentPunctuation.setText("");
            tvError.setText("");
            products.clear();
            establishment = null;
            totalPrice = 0;
            totalPunctuation = 0;

            Intent intent = new Intent(this, PublicationsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ((ImageView) findViewById(R.id.add_publication_image)).setImageBitmap(imageBitmap);
        }
    }
}