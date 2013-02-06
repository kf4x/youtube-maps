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

    public MyMarker() {
    }

    public MyMarker(long id, String title, String snippet, String position) {
        this.id = id;
        this.title = title;
        this.snippet = snippet;
        this.position = position;
    }
    public MyMarker(String title, String snippet, String position){
        this.title = title;
        this.snippet = snippet;
        this.position = position;        
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
    public void setPosition(String position) {
        this.position = position;
    }
    public LatLng getLatLng (){
        String[] pos = position.split(" ");
        return new LatLng(Double.valueOf(pos[0]), Double.valueOf(pos[1]));
    }
}
