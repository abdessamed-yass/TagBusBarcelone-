package tn.abdessamed.yessine.tagthebus.publication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import tn.abdessamed.yessine.tagthebus.R;
import tn.abdessamed.yessine.tagthebus.location.MapActivity;
import tn.abdessamed.yessine.tagthebus.sqlite.DatabaseHandler;
import tn.abdessamed.yessine.tagthebus.sqlite.PictureStation;
import tn.abdessamed.yessine.tagthebus.stationList.MainActivity;
import uploads.CaptureActivity;

public class MainPubActivity extends AppCompatActivity {

    private PictureStation station;
    private ArrayList<DataPubModel> dataModels;
    private ListView listView;
    private static CustomPubAdapter adapter;
    private int id = 1;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);


        listView = (ListView) findViewById(R.id.list);
        mContext = this.getApplicationContext();
        dataModels = new ArrayList<>();

        Intent i = getIntent();
        id = Integer.parseInt(i.getStringExtra("id"));
        remplissage();


        adapter = new CustomPubAdapter(dataModels, getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DataPubModel dataModel = dataModels.get(position);
                station = dataModel.getStation();


                Snackbar.make(view, "Image  prise en : "+station.getDateCreation() + "\n Titre : " + station.getTitre(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();


            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:

                Intent intent = new Intent(mContext, CaptureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("s", "" + id);
                mContext.startActivity(intent);
                return (true);

            case R.id.map:
                intent = new Intent(mContext, MapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("s", "" + id);
                mContext.startActivity(intent);
                break;
            case R.id.back:

                Intent i = new Intent(MainPubActivity.this, MainActivity.class);
                mContext.startActivity(i);
        }

        return (super.onOptionsItemSelected(item));
    }

    @Override
    public void onBackPressed() {
    }

    public void remplissage() {

        DatabaseHandler db = new DatabaseHandler(mContext);

        List<PictureStation> contacts = db.getAll();

        for (PictureStation cn : contacts) {
            if (cn.getIdStation() == id) {
                PictureStation ps = new PictureStation();


                ps.setId(cn.getId());

                ps.setTitre(cn.getTitre());
                ps.setNomStation("" + cn.getNomStation());
                ps.setDateCreation("" + cn.getDateCreation());
                ps.setIdStation(cn.getIdStation());


                DataPubModel d = new DataPubModel(ps);
                dataModels.add(d);


            }
            adapter = new CustomPubAdapter(dataModels, MainPubActivity.this);

        }
    }
}
