package tn.abdessamed.yessine.tagthebus.sqlite;


public class PictureStation {
    private int id;
    private int idStation;
    private String nomStation;
    private String dateCreation;
    private String titre;

    public PictureStation() {

    }

    public PictureStation(int idStation, String nomStation, String titre, String dateCreation) {

        this.idStation = idStation;
        this.nomStation = nomStation;
        this.titre = titre;
        this.dateCreation = dateCreation;
    }

    public PictureStation(int id, String nomStation, String titre, int idStation, String dateCreation) {
        this.id = id;
        this.idStation = idStation;
        this.nomStation = nomStation;
        this.titre = titre;
        this.dateCreation = dateCreation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdStation() {
        return idStation;
    }

    public void setIdStation(int idStation) {
        this.idStation = idStation;
    }

    public String getNomStation() {
        return nomStation;
    }

    public void setNomStation(String nomStation) {
        this.nomStation = nomStation;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
}