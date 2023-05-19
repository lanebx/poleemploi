package com.example.projetpoleemploi.ui.main.data.webservice.database;

import java.util.List;

public class JobResponse {
    public List<Resultat> resultats;
    public List<FiltresPossible> filtresPossibles;

    public static class Agence {
        public String courriel;
    }

    public static class Agregation {
        public String valeurPossible;
        public int nbResultats;
    }

    public static class Competence {
        public String code;
        public String libelle;
        public String exigence;
    }

    public static class Contact {
        public String nom;
        public String coordonnees1;
        public String coordonnees2;
        public String coordonnees3;
        public String courriel;
        public String telephone;
        public String urlPostulation;
    }

    public static class Entreprise {
        public String nom;
        public boolean entrepriseAdaptee;
        public String url;
        public String logo;
        public String description;
    }

    public static class FiltresPossible {
        public String filtre;
        public List<Agregation> agregation;
    }

    public static class Formation {
        public String codeFormation;
        public String domaineLibelle;
        public String niveauLibelle;
        public String exigence;
        public String commentaire;
    }

    public static class LieuTravail {
        public String libelle;
        public double latitude;
        public double longitude;
        public String codePostal;
        public String commune;
    }

    public static class OrigineOffre {
        public String origine;
        public String urlOrigine;
    }

    public static class Permis {
        public String libelle;
        public String exigence;
    }

    public static class QualitesProfessionnelles {
        public String libelle;
        public String description;
    }

    public static class Resultat {
        public String id;
        public String intitule;
        public String description;
        public String dateCreation;
        public String dateActualisation;
        public LieuTravail lieuTravail;
        public String romeCode;
        public String romeLibelle;
        public String appellationlibelle;
        public Entreprise entreprise;
        public String typeContrat;
        public String typeContratLibelle;
        public String natureContrat;
        public String experienceExige;
        public String experienceLibelle;
        public String experienceCommentaire;
        public List<Formation> formations;
        public List<Competence> competences;
        public Salaire salaire;
        public String dureeTravailLibelle;
        public String dureeTravailLibelleConverti;
        public boolean alternance;
        public Contact contact;
        public Agence agence;
        public int nombrePostes;
        public boolean accessibleTH;
        public String qualificationCode;
        public String qualificationLibelle;
        public String secteurActivite;
        public String secteurActiviteLibelle;
        public List<QualitesProfessionnelles> qualitesProfessionnelles;
        public OrigineOffre origineOffre;
        public String deplacementCode;
        public String deplacementLibelle;
        public boolean offresManqueCandidats;
        public String complementExercice;
        public List<Permis> permis;
    }

    public static class Salaire {
        public String commentaire;
        public String complement1;
        public String libelle;
        public String complement2;
    }
}