package com.svalero.toteco_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.svalero.toteco_app.database.AppDatabase;
import com.svalero.toteco_app.domain.Establishment;
import com.svalero.toteco_app.domain.Product;
import com.svalero.toteco_app.domain.Publication;
import com.svalero.toteco_app.domain.util.PublicationToRecyclerView;
import com.svalero.toteco_app.fragments.DeletePublicationDialog;
import com.svalero.toteco_app.util.RecyclerViewAdapter;
import com.svalero.toteco_app.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class PublicationsActivity extends AppCompatActivity
        implements DeletePublicationDialog.NoticeDialogListener, AdapterView.OnItemLongClickListener {

    private List<Publication> publications;
    private List<PublicationToRecyclerView> publicationsToRecyclerView;
    private RecyclerView.Adapter adapter;
    private AppDatabase db;
    private RecyclerView rv;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publications);

//        // drawer layout instance to toggle the menu icon to open
//        // drawer and back button to close drawer
//        drawerLayout = findViewById(R.id.drawer_layout);
//        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
//
//        // pass the Open and Close toggle for the drawer layout listener
//        // to toggle the button
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//        actionBarDrawerToggle.syncState();
//
//        // to make the Navigation drawer icon always appear on the action bar
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "toteco")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();

        publications = new ArrayList<>();
        loadPublications();
        publicationsToRecyclerView = new ArrayList<>();
        convertPublications();
        createRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        deleteUnsavedProducts();
        refreshPublications();
    }

    private void deleteUnsavedProducts() {
        try {
            db.productDao().deleteByPublicationId(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void loadPublications() {
        publications.clear();
        publications.addAll(db.publicationDao().findAllExceptAux());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshPublications() {
        loadPublications();
        convertPublications();
        adapter.notifyDataSetChanged();
    }

    @Override
    // Creates the action bar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        switch (id) {
            case R.id.add_publication_button:
                intent = new Intent(this, AddPublicationActivity.class);
                startActivity(intent);
                return true;
            case R.id.locations_button:
                intent = new Intent(this, LocationsActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout_button:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
        }

        return false;
    }

    private void createRecyclerView() {
        // Get the recycler view
        rv = findViewById(R.id.publication_recycler_view);
        rv.setHasFixedSize(true);

        // Use the linear layout
        RecyclerView.LayoutManager lManager = new LinearLayoutManager(this);
        rv.setLayoutManager(lManager);

        // Create the adapter
        adapter = new RecyclerViewAdapter(getSupportFragmentManager(), publicationsToRecyclerView);
        rv.setAdapter(adapter);
    }

    private void convertPublications() {
        publicationsToRecyclerView.clear();

        publications.stream().forEach((p) -> {
            Establishment establishment = db.establishmentDao().findById(p.getEstablishmentId());
            List<Product> products = db.productDao().findByPublicationId(p.getId());
            double totalPrice = Utils.roundNumber(p.getTotalPrice());
            double totalPunctuation = Utils.roundNumber(p.getTotalPunctuation());
            PublicationToRecyclerView publicationToRecyclerView = new PublicationToRecyclerView(
                    p.getId(),
                    establishment.getName(),
                    String.valueOf(establishment.getPunctuation()),
                    p.getImage(),
                    products,
                    getString(R.string.card_price, String.valueOf(totalPrice)),
                    getString(R.string.card_punctuation, String.valueOf(totalPunctuation))
            );
            publicationsToRecyclerView.add(publicationToRecyclerView);
        });
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        refreshPublications();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        System.out.println("entra");
        AppDatabase db = Room.databaseBuilder(view.getContext(),
                        AppDatabase.class, "toteco").allowMainThreadQueries()
                .fallbackToDestructiveMigration().build();
        Publication publication = publications.get(position);
        DialogFragment newFragment = new DeletePublicationDialog(db, publication);
        newFragment.show(getSupportFragmentManager(), "delete");
        return true;
    }

    @Override
    public void onBackPressed() {

    }
}