package tn.abdessamed.yessine.tagthebus.entity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tn.abdessamed.yessine.tagthebus.R;
import tn.abdessamed.yessine.tagthebus.util.ConnexionJson;

import static android.content.ContentValues.TAG;

public class StationPicture extends Activity implements OnMapReadyCallback {
    private Station station;
    private String url = "http://barcelonaapi.marcpous.com/bus/nearstation/latlon/41.3985182/2.1917991/1.json";

    private ArrayList<Station> lstations = new ArrayList<>();
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (googleServicesAvailable()) {
            Toast.makeText(this, "Perfect!!!", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_station);
            initMap();
        } else {
        }
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    public boolean googleServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int isAvailable = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (googleApiAvailability.isUserResolvableError(isAvailable)) {
            Dialog dialog = googleApiAvailability.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cant connect to play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        goToLocation(station.getLatitude(), station.getLongitude(), 15);


    }

    private void goToLocation(double latitude, double longitude, float zoom) {
        LatLng latLng = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        googleMap.moveCamera(cameraUpdate);
        MarkerOptions markerOptions = new MarkerOptions()
                .title(station.getStreet_name())
                .position(latLng)
                .snippet("City : " + station.getCity())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.stationbus));

        googleMap.addMarker(markerOptions);
        new GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        ProgressDialog pdLoading = new ProgressDialog(StationPicture.this);
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
                        lstations.add(station);
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
            pdLoading.dismiss();
            initMap();

            for (int i = 0; i < 2; i++) {
                LatLng latLng = new LatLng(lstations.get(i).getLatitude(), lstations.get(i).getLongitude());

                MarkerOptions markerOptions = new MarkerOptions()
                        .title(lstations.get(i).getStreet_name())
                        .position(latLng)
                        .snippet("City : " + station.getCity())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.stationbus));

                googleMap.addMarker(markerOptions);
            }
        }


    }
}
