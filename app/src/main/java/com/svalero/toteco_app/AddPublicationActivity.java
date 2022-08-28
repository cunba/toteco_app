package com.svalero.toteco_app;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.svalero.toteco_app.database.AppDatabase;
import com.svalero.toteco_app.domain.Product;
import com.svalero.toteco_app.domain.Publication;

import java.util.ArrayList;
import java.util.List;

public class AddPublicationActivity extends AppCompatActivity {

    public List<Product> products;
    private ArrayAdapter<Product> productsAdapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_publication);

        products = new ArrayList<>();
        ListView lvProducts = findViewById(R.id.add_publication_product_list);
        productsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products);
        lvProducts.setAdapter(productsAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //Delete unsaved products
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "toteco").allowMainThreadQueries().fallbackToDestructiveMigration().build();
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
        products.clear();
        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "toteco").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();
        products.addAll(db.productDao().findByPublicationId(1));
    }

    private void makeSummary() {
        double totalPrice = products.stream()
                .map(Product::getPrice)
                .mapToDouble(price -> price)
                .sum();

        TextView tvTotalPrice = findViewById(R.id.add_publication_total_price);
        tvTotalPrice.setText(getString(R.string.card_price, totalPrice));
    }

    @Override
    // Creates the action bar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    public void onPressAddProduct(View view) {
        Intent intent = new Intent(this, AddProductActivity.class);
        startActivity(intent);
    }
}