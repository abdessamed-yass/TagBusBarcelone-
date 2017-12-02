package tn.abdessamed.yessine.tagthebus.stationList;

import tn.abdessamed.yessine.tagthebus.entity.Station;

public class DataModel {
    private Station station;

    public  DataModel(Station station)
    {
        this.station=station;
    }
    public Station getStation() {
        return station;
    }


    public void setStation(Station station) {
        this.station = station;
    }
}
