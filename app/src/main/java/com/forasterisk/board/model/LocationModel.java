package com.forasterisk.board.model;

/**
 * Created by yearnning on 15. 8. 4..
 */
public class LocationModel {

    /**
     *
     */
    private double latitude;
    private double longitude;
    private String name;

    /**
     * @return
     */
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
