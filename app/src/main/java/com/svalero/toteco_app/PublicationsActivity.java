package com.svalero.toteco_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;

import com.svalero.toteco_app.database.AppDatabase;
import com.svalero.toteco_app.domain.Establishment;
import com.svalero.toteco_app.domain.Product;
import com.svalero.toteco_app.domain.Publication;
import com.svalero.toteco_app.domain.relation.EstablishmentWithPublication;
import com.svalero.toteco_app.domain.util.PublicationToRecyclerView;
import com.svalero.toteco_app.util.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class PublicationsActivity extends AppCompatActivity {

    private List<Publication> publications;
    private List<PublicationToRecyclerView> publicationsToRecyclerView;
    private RecyclerView.Adapter adapter;
    private AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "toteco").allowMainThreadQueries().fallbackToDestructiveMigration().build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publications);

        publications = new ArrayList<>();
        loadPublications();
        publicationsToRecyclerView = new ArrayList<>();
        createRecyclerView();
    }

    private void loadPublications() {
        publications.clear();
        publications.addAll(db.publicationDao().findAll());
    }

    @Override
    // Creates the action bar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    private void createRecyclerView() {
        // Get the recycler view
        RecyclerView rv = findViewById(R.id.publication_recycler_view);
        rv.setHasFixedSize(true);

        // Use the linear layout
        RecyclerView.LayoutManager lManager = new LinearLayoutManager(this);
        rv.setLayoutManager(lManager);

        // Create the adapter
        adapter = new RecyclerViewAdapter(publicationsToRecyclerView);
        rv.setAdapter(adapter);
    }

    private void convertPublications() {
        publications.stream().forEach((p) -> {
            Establishment establishment = db.publicationDao().findEstablishmentById(p.getEstablishmentId()).getEstablishment();
            List<Product> products = db.productDao().findByPublicationId(p.getId());
            PublicationToRecyclerView publicationToRecyclerView = new PublicationToRecyclerView(
                    p.getId(),
                    establishment.getName(),
                    1,
                    products,
                    String.valueOf(p.getTotalPrice()),
                    String.valueOf(p.getTotalPunctuation())
            );
            publicationsToRecyclerView.add(publicationToRecyclerView);
        });
    }

}