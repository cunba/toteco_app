package com.svalero.toteco_app.domain.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.svalero.toteco_app.domain.Establishment;
import com.svalero.toteco_app.domain.Publication;

import java.util.List;

public class EstablishmentWithPublication {
    @Embedded
    Establishment establishment;

    @Relation(parentColumn = "id", entityColumn = "establishment_id")
    List<Publication> publications;

    public EstablishmentWithPublication(Establishment establishment, List<Publication> publications) {
        this.establishment = establishment;
        this.publications = publications;
    }

    public Establishment getEstablishment() {
        return establishment;
    }

    public void setEstablishment(Establishment establishment) {
        this.establishment = establishment;
    }

    public List<Publication> getPublications() {
        return publications;
    }

    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }
}
