package tn.abdessamed.yessine.tagthebus.entity;

import java.io.Serializable;


public class Station implements Serializable {

    private int id;
    private String street_name;
    private String city;
    private double latitude;
    private double longitude;


    public Station(int id, String street_name, String city, double latitude, double longitude) {
        this.id = id;
        this.street_name = street_name;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet_name() {
        return street_name;
    }

    public void setStreet_name(String street_name) {
        this.street_name = street_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


}
