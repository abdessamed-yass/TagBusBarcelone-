package tn.abdessamed.yessine.tagthebus.publication;

import tn.abdessamed.yessine.tagthebus.sqlite.PictureStation;


public class DataPubModel {
    private PictureStation station;

    public DataPubModel(PictureStation station) {
        this.station = station;
    }

    public PictureStation getStation() {
        return station;
    }


    public void setStation(PictureStation station) {
        this.station = station;
    }
}
