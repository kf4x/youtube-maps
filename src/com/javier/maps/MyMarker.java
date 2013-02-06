/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javier.maps;

import com.google.android.gms.maps.model.LatLng;

/**
 *
 * @author javierAle
 */
public class MyMarker {
    private long id;
    private String title;
    private String snippet;
    private String position;
    private LatLng poLatLng;

    public MyMarker() {
    }

    public MyMarker(long id, String title, String snippet, LatLng poLatLng) {
        this.id = id;
        this.title = title;
        this.snippet = snippet;
        this.poLatLng = poLatLng;
        this.position = String.valueOf(poLatLng.latitude) + " " + String.valueOf(poLatLng.longitude);

    }
    public MyMarker(String title, String snippet, LatLng poLatLng){
        this.title = title;
        this.snippet = snippet;
        this.poLatLng = poLatLng;
        this.position = String.valueOf(poLatLng.latitude) + " " + String.valueOf(poLatLng.longitude);

    }

    
    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the snippet
     */
    public String getSnippet() {
        return snippet;
    }

    /**
     * @param snippet the snippet to set
     */
    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    /**
     * @return the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(LatLng poLatLng) {
        this.poLatLng = poLatLng;
        this.position = String.valueOf(poLatLng.latitude) + " " + String.valueOf(poLatLng.longitude);
    }
    
    /**
     * @param position needs to be Latitude</space/>Longitude
     */
    public void setPosition(String position) {
        this.position = position;
        String[] pos = position.split(" ");
        this.poLatLng = new LatLng(Double.valueOf(pos[0]), Double.valueOf(pos[1]));
    }
    
    public LatLng getLatLng (){
        return poLatLng;
    }
}
