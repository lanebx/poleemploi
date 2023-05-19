package com.example.projetpoleemploi.ui.main.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.projetpoleemploi.ui.main.data.webservice.database.JobResponse;

import java.io.Serializable;

@Entity(tableName = "job_table")
public class JobEntity implements Serializable {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public String id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "location")
    public String location;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "typeContrat")
    public String typeContrat;

    public JobEntity(@NonNull String id, String title, String description, String location, String image, String typeContrat) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.image = image;
        this.typeContrat = typeContrat;
    }

    // JobResponse.Resultat в объект JobEntity
    public static JobEntity fromResultat(JobResponse.Resultat resultat) {
        String location = String.format("%s, %s", resultat.lieuTravail.libelle, resultat.lieuTravail.codePostal);
        return new JobEntity(resultat.id, resultat.intitule, resultat.description, location, resultat.entreprise.logo, resultat.typeContrat);
    }

    public static JobEntity[] fav = {
            new JobEntity("001", "TEST_titl", "TEST_dis", "Avignon", null, "1")
    };
}
