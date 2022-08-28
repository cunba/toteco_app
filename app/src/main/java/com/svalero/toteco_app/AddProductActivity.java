package com.svalero.toteco_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.svalero.toteco_app.database.AppDatabase;
import com.svalero.toteco_app.domain.Product;
import com.svalero.toteco_app.domain.Publication;

import java.util.List;

public class AddProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
    }

    public void addProduct(View view) {
        EditText etName = findViewById(R.id.add_product_name);
        EditText etPrice = findViewById(R.id.add_product_price);
        EditText etPunctuation = findViewById(R.id.add_product_punctuation);
        TextView tvError = findViewById(R.id.add_product_error);

        etName.clearFocus();
        etPrice.clearFocus();
        etPunctuation.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etPunctuation.getWindowToken(), 0);

        String name = etName.getText().toString();
        String priceString = etPrice.getText().toString();
        String punctuationString = etPunctuation.getText().toString();

        if (name.equals("") || priceString.equals("") || punctuationString.equals("")) {
            tvError.setText(R.string.error_field_empty);
        } else {
            float price = Float.parseFloat(priceString);
            float punctuation = Float.parseFloat(punctuationString);

            if (price < 0.0 || punctuation < 0.0) {
                tvError.setText(R.string.add_product_error_price_punctuation);
            } else if (punctuation > 5.0) {
                tvError.setText(R.string.add_product_error_punctuation);
            } else {
                tvError.setText("");

                Product newProduct = new Product(name, price, punctuation, 1);

                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "toteco").allowMainThreadQueries().fallbackToDestructiveMigration().build();
                List<Publication> publications = db.publicationDao().findAll();
                if (publications.size() == 0) {
                    Publication publication = new Publication(0,0,1,1);
                    db.publicationDao().insert(publication);
                }

                db.productDao().insert(newProduct);

                etName.setText("");
                etPrice.setText("");
                etPunctuation.setText("");

                // Move to add publication
                Intent intent = new Intent(this, AddPublicationActivity.class);
                startActivity(intent);
            }
        }
    }
}