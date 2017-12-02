package tn.abdessamed.yessine.tagthebus.stationList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tn.abdessamed.yessine.tagthebus.R;
import tn.abdessamed.yessine.tagthebus.entity.Station;
import tn.abdessamed.yessine.tagthebus.location.MapActivity;
import tn.abdessamed.yessine.tagthebus.util.ConnexionJson;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private String url = "http://barcelonaapi.marcpous.com/bus/nearstation/latlon/41.3985182/2.1917991/1.json";
    private Station station;
    private ArrayList<DataModel> dataModels;
    private ListView listView;
    private static CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = (ListView) findViewById(R.id.list);

        new GetContacts().execute();
        dataModels = new ArrayList<>();


        adapter = new CustomAdapter(dataModels, getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DataModel dataModel = dataModels.get(position);
                station = dataModel.getStation();


                Snackbar.make(view, "Station de " +station.getStreet_name() + "\n City : " + station.getCity(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();


            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent i = new Intent(MainActivity.this, MapActivity.class);
                startActivity(i);

                return (true);

        }
        return (super.onOptionsItemSelected(item));
    }


    private class GetContacts extends AsyncTask<Void, Void, Void> {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        JSONObject jsonObj;
        JSONArray stations;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //this method will be running on UI thread
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
                        dataModels.add(new DataModel(station));
                    }

                    adapter = new CustomAdapter(dataModels, MainActivity.this);


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
            listView.setAdapter(adapter);
        }


    }
}
