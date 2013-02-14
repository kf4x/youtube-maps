package com.javier.maps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private Context context = this;
    private GoogleMap googlemap;
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initMap();

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = lm.getBestProvider(new Criteria(), true);
        
//        final Location lastKnownLoc = lm.getLastKnownLocation(provider);

        if (provider == null) {
            onProviderDisabled(provider);
        }
        
        ((Button) findViewById(R.id.clickBtn)).setOnClickListener(new View.OnClickListener() {
             
            public void onClick(View v) {
//                Double dLat = lastKnownLoc.getLatitude();
//                Double dLong = lastKnownLoc.getLongitude();                  
//                String srch = ((EditText)findViewById(R.id.etsearch)).getText().toString();
                if (googlemap.getMyLocation() != null) {
                    new SearchFoursquare().execute(
                        String.valueOf(googlemap.getMyLocation().getLatitude()),
                        String.valueOf(googlemap.getMyLocation().getLongitude()),
                        ((EditText)findViewById(R.id.etsearch)).getText().toString()                            
                            );
                }
                else{

                }
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

    protected void addToMap(ArrayList<HashMap<String, String>> venueMap) {
        googlemap.clear();

        LatLng pos = null;
            for (int i = 0; i < venueMap.size(); i++) {
                String lat = venueMap.get(i).get("lat");
                String lng = venueMap.get(i).get("lng");
                try {
                    pos = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                } catch (Exception e) {
                }
                googlemap.addMarker(new MarkerOptions()
                        .title(venueMap.get(i).get("name").toString())
                        .snippet(venueMap.get(i).get("here").toString())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .position(pos)
                        );
            }
    }

        
    
    private class SearchFoursquare extends AsyncTask<String, Void, Void>{
        private JSONObject json;
        private JSONObject response;
        private JSONArray venues;
        private ArrayList<HashMap<String, String>> venueMap = new ArrayList<HashMap<String, String>>();
        @Override
        protected Void doInBackground(String... params) {
            if (params[2].contains(" ")) {
                params[2] = params[2].replace(" ", "+");
            }
            json = new JSONParser().getJSONfromUrl("https://api.foursquare.com/v2/venues/search?ll="+ params[0] +","+ params[1] + "&query='"+params[2]+"'&oauth_token=QHWKC5NBUGVO2XSHGD5LVCKV5QXWK20GTAUSQHDG51PC32AA&v=20130212");
            try {
                response = json.getJSONObject("response");
                venues = response.getJSONArray("venues");
                venueMap.clear();
                for (int i = 0; i < venues.length(); i++) {
                    JSONObject v = venues.getJSONObject(i);

                    String name = v.getString("name");

                    JSONObject loc = v.getJSONObject("location");
                    String lat = loc.getString("lat");
                    String lng = loc.getString("lng");

                    JSONObject here = v.getJSONObject("hereNow");
                    String count = here.getString("count");

                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put("name", name);
                    map.put("lat", lat);
                    map.put("lng", lng);
                    map.put("here", count);

                    venueMap.add(map);

                }
            } catch (JSONException ex) {
                Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            addToMap(venueMap);
            super.onPostExecute(result);
        }

    }
}

//	37.7769904 -122.4169725 

