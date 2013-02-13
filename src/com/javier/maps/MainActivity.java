package com.javier.maps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends FragmentActivity implements LocationListener
{
    Context context = this;
    GoogleMap googlemap;
    JSONParser jParser = new JSONParser();
    JSONObject json;
    JSONObject response;
    JSONArray venues;
    ArrayList<HashMap<String, String>> venueMap = new ArrayList<HashMap<String, String>>();

    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initMap();

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = lm.getBestProvider(new Criteria(), true);
        
        Location lastKnownLoc = lm.getLastKnownLocation(provider);
        final Double dLat = lastKnownLoc.getLatitude();
        final Double dLong = lastKnownLoc.getLongitude(); //"+ dLat +","+ dLong + "       
        Toast.makeText(context,"youre at " + dLat + " " + dLong , Toast.LENGTH_LONG);     
        if (provider == null) {
            onProviderDisabled(provider);
        }
        
        
        new Thread(new Runnable() {
            public void run() {
                json = jParser.getJSONfromUrl("https://api.foursquare.com/v2/venues/search?ll="+ dLat +","+ dLong + "&query=club&oauth_token=QHWKC5NBUGVO2XSHGD5LVCKV5QXWK20GTAUSQHDG51PC32AA&v=20130212");
                try {
                    response = json.getJSONObject("response");
                    venues = response.getJSONArray("venues");
                    for (int i = 0; i < venues.length(); i++) {
                        JSONObject v = venues.getJSONObject(i);
                        
                        String name = v.getString("name");
                        
                        JSONObject loc = v.getJSONObject("location");
                        String lat = loc.getString("lat");
                        String lng = loc.getString("lng");
                        HashMap<String, String> map = new HashMap<String, String>();
                        
                        map.put("name", name);
                        map.put("lat", lat);
                        map.put("lng", lng);
                        
                        venueMap.add(map);
                                                
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
                }
                addTwittertoMap();
            }
        }).start();
        
        

        
        googlemap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            public void onMapLongClick(final LatLng latlng) {
                LayoutInflater li = LayoutInflater.from(context);
                final View v = li.inflate(R.layout.alertlayout, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(v);
                builder.setCancelable(false);
                
                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        EditText title = (EditText) v.findViewById(R.id.ettitle);
                        EditText snippet = (EditText) v.findViewById(R.id.etsnippet);
                        googlemap.addMarker(new MarkerOptions()
                                .title(title.getText().toString())
                                .snippet(snippet.getText().toString())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                .position(latlng)
                                );                    
                    }
                });
                
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                
                AlertDialog alert = builder.create();
                alert.show();
                
            }
        });
    }

    public void onLocationChanged(Location location) {
//        Toast.makeText(context,"youre at " + location.getLatitude() + " " + location.getLongitude() , Toast.LENGTH_LONG);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onProviderDisabled(String provider) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Phone is in airplane mode");
        builder.setCancelable(false);
        builder.setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent startGps = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(startGps);
            }
        });
        builder.setNegativeButton("Leave GPS off", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void initMap(){
        SupportMapFragment mf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        googlemap = mf.getMap();
        
        googlemap.setMyLocationEnabled(true);
        googlemap.setMapType(GoogleMap.MAP_TYPE_NORMAL); 
    }

    protected void addTwittertoMap() {
//        LatLng pos = new LatLng(37.7769904, -122.4169725);
//        MarkerOptions mm = new MarkerOptions();
//        googlemap.addMarker(new MarkerOptions()
//                .title("Twitter")
//                .snippet("Twitter HQ")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
//                .position(pos)
//                );
//      t
        try {
            for (int i = 0; i < 10; i++) {
                String lat = venueMap.get(i).get("lat");
                String lng = venueMap.get(i).get("lng");

                googlemap.addMarker(new MarkerOptions()
                        .title(venueMap.get(i).get("title"))
        //                .snippet("Twitter HQ")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .position(new LatLng(Double.valueOf(lat), Double.valueOf(lng)))
                        );
            }            
        } catch (Exception e) {
        }

    
    }
}

//	37.7769904 -122.4169725 

