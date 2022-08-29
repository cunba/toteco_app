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
import android.os.Parcelable;
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

public class AddPublicationActivity extends AppCompatActivity {

    public List<Product> products;
    private ArrayAdapter<Product> productsAdapter;
    private AppDatabase db;

    private Establishment establishment;
    double totalPrice = 0;
    double totalPunctuation = 0;

    private final int SELECT_PICTURE_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_publication);

        products = new ArrayList<>();
        ListView lvProducts = findViewById(R.id.add_publication_product_list);
        productsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products);
        lvProducts.setAdapter(productsAdapter);
    }

    private void setEstablishment() {
        TextView tvEstablishmentName = findViewById(R.id.add_publication_establishment_name);
        TextView tvEstablishmentPunctuation = findViewById(R.id.add_publication_establishment_punctuation);

        establishment = db.establishmentDao().findById(1);
        if (establishment.getName().equals("")) {
            tvEstablishmentName.setText(R.string.add_publication_establishment_add);
            tvEstablishmentPunctuation.setText(R.string.add_publication_establishment_punctuation);
        } else {
            tvEstablishmentName.setText(establishment.getName());
            double price = Math.round(establishment.getPunctuation() * 100) / 100;
            String sPrice = String.valueOf(price);
            tvEstablishmentPunctuation.setText(getString(R.string.add_publication_establishment_punctuation_print, sPrice));
        }

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

        setEstablishment();
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
        totalPrice = Math.round(totalPrice*100.0)/100.0;

        if (products.size() != 0) {
            totalPunctuation = products.stream()
                    .map(Product::getPunctuation)
                    .mapToDouble(punctuation -> punctuation)
                    .sum() / products.size();
            totalPunctuation = Math.round(totalPunctuation*100.0)/100.0;
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

    public void onPressAddEstablishment(View view) {
        Intent intent = new Intent(this, AddEstablishmentActivity.class);
//        intent.putExtra("establishment", (Parcelable) establishment);
        startActivity(intent);
    }

    public void onPressAddImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_PICTURE_RESULT);
    }

    public void onPressSubmit(View view) {
        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "toteco").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();

        TextView tvEstablishmentName = findViewById(R.id.add_publication_establishment_name);
        TextView tvEstablishmentPunctuation = findViewById(R.id.add_publication_establishment_punctuation);
        TextView tvError = findViewById(R.id.add_publication_error);
        ImageView productImageView = findViewById(R.id.add_publication_image);

        String establishmentName = tvEstablishmentName.toString();
        String establishmentPunctuationString = tvEstablishmentPunctuation.toString();

        if (establishmentName.equals("") || establishmentPunctuationString.equals("")) {
            tvError.setText(R.string.error_establishment_empty);
        } else if (products.size() == 0) {
            tvError.setText(R.string.error_products_empty);
        } else {
            tvError.setText("");
            float establishmentPunctuation = Float.parseFloat(establishmentPunctuationString);
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

            publication.setImage(publicationImage);

            db.publicationDao().insert(publication);
            Publication addedPublication = db.publicationDao().findLast();

            products.stream().forEach(p -> {
                p.setPublicationId(addedPublication.getId());
                db.productDao().update(p);
                System.out.println(p.getName());
            });

            Toast.makeText(this, R.string.publication_created, Toast.LENGTH_SHORT).show();

            tvEstablishmentName.setText("");
            tvEstablishmentPunctuation.setText("");
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
        if ((requestCode == SELECT_PICTURE_RESULT) && (resultCode == RESULT_OK)
                && (data != null)) {
            Picasso.get().load(data.getData()).noPlaceholder().centerCrop().fit()
                    .into((ImageView) findViewById(R.id.add_publication_image));

        }
    }
}