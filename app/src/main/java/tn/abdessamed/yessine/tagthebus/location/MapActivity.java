package tn.abdessamed.yessine.tagthebus.location;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tn.abdessamed.yessine.tagthebus.R;
import tn.abdessamed.yessine.tagthebus.entity.Station;
import tn.abdessamed.yessine.tagthebus.stationList.MainActivity;
import tn.abdessamed.yessine.tagthebus.util.ConnexionJson;

import static android.content.ContentValues.TAG;

public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener {

    private GoogleMap map;


    private String url = "http://barcelonaapi.marcpous.com/bus/nearstation/latlon/41.3985182/2.1917991/1.json";

    private GPSTracker gps;
    private int id = -1;
    private double myLatitude = 0;
    private double MyLongitude = 0;
    private Marker Mr;
    private Context mContext;
    private Boolean focus = false;
    private ArrayList<Station> Tab = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new GetContacts().execute();
        mContext = this;
        locateMe();
        Intent i = getIntent();
        if (i.getStringExtra("s") != null)
            id = Integer.parseInt(i.getStringExtra("s"));
        if (id != -1) {
            focus = true;
        }


        super.onCreate(savedInstanceState);

        setContentView(R.layout.map);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                LatLng CurrentPlace = new LatLng(myLatitude, MyLongitude);
                Mr = map.addMarker(new MarkerOptions().position(CurrentPlace).title("Current Place").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(CurrentPlace, 19));

                return (true);

            case R.id.back:
                focus = false;
                Intent i = new Intent(MapActivity.this, MainActivity.class);
                startActivity(i);

        }
        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Toast.makeText(getApplicationContext(), "helo", Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        ProgressDialog pdLoading = new ProgressDialog(MapActivity.this);
        JSONObject jsonObj;
        JSONArray stations;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();


        }

        @Override
        protected Void doInBackground(Void... arg0) {
            ConnexionJson connexionJson = new ConnexionJson();
            String jsonStr = connexionJson.makeServiceCall(url);
            if (jsonStr != null) {
                try {
                    jsonObj = new JSONObject(jsonStr);
                    JSONObject res = jsonObj.getJSONObject("data");
                    stations = res.getJSONArray("nearstations");
                    for (int i = 0; i < stations.length(); i++) {

                        int id = Integer.parseInt(stations.getJSONObject(i).get("id").toString());
                        String street = stations.getJSONObject(i).get("street_name").toString();
                        String city = stations.getJSONObject(i).get("city").toString();
                        double latitude = Double.parseDouble(stations.getJSONObject(i).get("lat").toString());
                        double longitude = Double.parseDouble(stations.getJSONObject(i).get("lon").toString());
                        Station station = new Station(id, street, city, latitude, longitude);
                        Tab.add(station);
                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());

                }
            }


            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            pdLoading.dismiss();

            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();


            int i = 0;
            for (i = 0; i < Tab.size(); i++) {


                if (focus == true) {
                    if (Tab.get(i).getId() == id) {
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Tab.get(i).getLatitude(), Tab.get(i).getLongitude()), 19));
                    }
                    Mr = map.addMarker(new MarkerOptions().position(new LatLng(Tab.get(i).getLatitude(), Tab.get(i).getLongitude())).title(Tab.get(i).getStreet_name() + " City =" + Tab.get(i).getCity()));


                } else {
                    Mr = map.addMarker(new MarkerOptions().position(new LatLng(Tab.get(i).getLatitude(), Tab.get(i).getLongitude())).title(Tab.get(i).getStreet_name() + " City =" + Tab.get(i).getCity()));

                }


            }
            if (focus == false) {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Tab.get(i - 1).getLatitude(), Tab.get(i - 1).getLongitude()), 17));
            }


        }


    }

    public void locateMe() {
        final int REQUEST_LOCATION = 1;


        LocationManager locationManager;
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                myLatitude = location.getLatitude();
                MyLongitude = location.getLongitude();


            } else {

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Please Turn ON your GPS Connection")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

}


