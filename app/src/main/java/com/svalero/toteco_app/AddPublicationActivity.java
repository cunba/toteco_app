package com.svalero.toteco_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import com.squareup.picasso.Picasso;
import com.svalero.toteco_app.database.AppDatabase;
import com.svalero.toteco_app.domain.Establishment;
import com.svalero.toteco_app.domain.Product;
import com.svalero.toteco_app.domain.Publication;
import com.svalero.toteco_app.fragments.AddProductFragment;
import com.svalero.toteco_app.fragments.DeleteProductDialog;
import com.svalero.toteco_app.fragments.ModifyProductFragment;
import com.svalero.toteco_app.util.ImageAdapter;
import com.svalero.toteco_app.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class AddPublicationActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener,
        DeleteProductDialog.NoticeDialogListener, ModifyProductFragment.NoticeDialogListener, AddProductFragment.NoticeDialogListener {

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

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "toteco").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();

        products = new ArrayList<>();
        ListView lvProducts = findViewById(R.id.add_publication_product_list);
        productsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products);
        lvProducts.setAdapter(productsAdapter);
        lvProducts.setOnItemClickListener(this);
        lvProducts.setOnItemLongClickListener(this);
    }

    private void setEstablishment() {
        TextView tvEstablishmentName = findViewById(R.id.add_publication_establishment_name);
        TextView tvEstablishmentPunctuation = findViewById(R.id.add_publication_establishment_punctuation);

        try {
            establishment = db.establishmentDao().findById(1);
            if (establishment.getName().equals("")) {
                tvEstablishmentName.setText(R.string.add_publication_establishment_add);
                tvEstablishmentPunctuation.setText(R.string.add_publication_establishment_punctuation);
            } else {
                // We make sure that the establishment exists or not
                List<Establishment> exists = db.establishmentDao().findByNameExceptAux(establishment.getName());
                if (exists.size() != 0) {
                    exists.stream().forEach(e -> {
                        if (e.getLatitude() == establishment.getLatitude() && e.getLongitude() == establishment.getLongitude()) {
                            establishment.setId(e.getId());
                            establishment.setOpen(e.isOpen());
                        }
                    });
                }
                tvEstablishmentName.setText(establishment.getName());
                double price = Utils.roundNumber(establishment.getPunctuation());
                String sPrice = String.valueOf(price);
                tvEstablishmentPunctuation.setText(getString(R.string.add_publication_establishment_punctuation_print, sPrice));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //Delete unsaved products
        try {
            db.productDao().deleteByPublicationId(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Clean the aux establishment
        clearEstablishmentAux();

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
        totalPrice = Utils.roundNumber(totalPrice);

        if (products.size() != 0) {
            totalPunctuation = products.stream()
                    .map(Product::getPunctuation)
                    .mapToDouble(punctuation -> punctuation)
                    .sum() / products.size();
            totalPunctuation = Utils.roundNumber(totalPunctuation);
        }

        TextView tvTotalPrice = findViewById(R.id.add_publication_total_price);
        tvTotalPrice.setText(getString(R.string.add_publication_total_price, String.valueOf(totalPrice)));

        TextView tvTotalPunctuation = findViewById(R.id.add_publication_total_punctuation);
        tvTotalPunctuation.setText(getString(R.string.add_publication_total_punctuation, String.valueOf(totalPunctuation)));
    }

    public void onPressAddProduct(View view) {
        TextView tvError = findViewById(R.id.add_publication_error);
        tvError.setText("");
        DialogFragment newFragment = new AddProductFragment(db);
        newFragment.show(getSupportFragmentManager(), "add");
    }

    public void onPressAddEstablishment(View view) {
        TextView tvError = findViewById(R.id.add_publication_error);
        tvError.setText("");
        Intent intent = new Intent(this, AddEstablishmentActivity.class);
        startActivity(intent);
    }

    public void onPressAddImage(View view) {
        TextView tvError = findViewById(R.id.add_publication_error);
        tvError.setText("");
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_PICTURE_RESULT);
    }

    public void onPressSubmit(View view) {
        TextView tvEstablishmentName = findViewById(R.id.add_publication_establishment_name);
        TextView tvEstablishmentPunctuation = findViewById(R.id.add_publication_establishment_punctuation);
        TextView tvError = findViewById(R.id.add_publication_error);
        ImageView ivPublication = findViewById(R.id.add_publication_image);

        String establishmentName = tvEstablishmentName.toString();
        String establishmentPunctuationString = tvEstablishmentPunctuation.toString();

        if (establishmentName.equals("") || establishmentPunctuationString.equals("")) {
            tvError.setText(R.string.error_establishment_empty);
        } else if (products.size() == 0) {
            tvError.setText(R.string.error_products_empty);
        } else if (ivPublication.getDrawable() == null) {
            tvError.setText(R.string.error_empty_image);
        } else {
            tvError.setText("");
            byte[] publicationImage = ImageAdapter.fromImageViewToByteArray(ivPublication);
            double establishmentPunctuation = (establishment.getPunctuation() + totalPunctuation) / (1 + products.size());
            System.out.println(establishmentPunctuation);

            // if the establishment doesnt exists we create it
            if (establishment.getId() == 1) {
                Establishment newEstablishment = new Establishment(
                        establishment.getName(),
                        establishment.getLatitude(),
                        establishment.getLongitude(),
                        true,
                        (float) establishmentPunctuation
                );
                db.establishmentDao().insert(newEstablishment);
                establishment = db.establishmentDao().findLast();
                System.out.println(establishment.getPunctuation());
            } else {
                // Recalculate the establishment punctuation
                int establishmentPublications = db.establishmentDao().countPublicationsByEstablishmentId(establishment.getId()) + 1;
                Establishment e = db.establishmentDao().findById(establishment.getId());
                System.out.println(e.getPunctuation());
                System.out.println(establishmentPunctuation);
                float punctuation = (float) ((e.getPunctuation() + establishmentPunctuation) / establishmentPublications);
                establishment.setPunctuation(punctuation);
                db.establishmentDao().update(establishment);
            }

            Publication publication = new Publication(
                    (float) totalPrice,
                    (float) totalPunctuation,
                    1,
                    establishment.getId());

            publication.setImage(publicationImage);

            db.publicationDao().insert(publication);
            Publication addedPublication = db.publicationDao().findLast();

            products.stream().forEach(p -> {
                p.setPublicationId(addedPublication.getId());
                db.productDao().update(p);
            });

            Toast.makeText(this, R.string.publication_created, Toast.LENGTH_SHORT).show();

            tvEstablishmentName.setText(R.string.add_publication_establishment_add);
            tvEstablishmentPunctuation.setText(R.string.add_publication_establishment_punctuation);
            tvError.setText("");
            products.clear();

            // Clean the aux establishment
            clearEstablishmentAux();

            totalPrice = 0;
            totalPunctuation = 0;

            Intent intent = new Intent(this, PublicationsActivity.class);
            startActivity(intent);
        }
    }

    private void clearEstablishmentAux() {
        if (establishment != null) {
            establishment.setId(1);
            establishment.setName("");
            establishment.setPunctuation(0);
            establishment.setLatitude(0);
            establishment.setLongitude(0);
            db.establishmentDao().update(establishment);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Product product = products.get(position);
        DialogFragment newFragment = new ModifyProductFragment(db, product);
        newFragment.show(getSupportFragmentManager(), "modify");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        Product product = products.get(position);
        DialogFragment newFragment = new DeleteProductDialog(db, product);
        newFragment.show(getSupportFragmentManager(), "delete");
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        refreshList();
        makeSummary();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String error) {
        if (error.equals("")) {
            refreshList();
            makeSummary();
        } else {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }
    }
}