package tn.abdessamed.yessine.tagthebus.publication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import tn.abdessamed.yessine.tagthebus.R;
import tn.abdessamed.yessine.tagthebus.stationList.MainActivity;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PictureZoom extends AppCompatActivity {

    private ImageView imageView;
    private String titre;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoom_activity);
        Intent i = getIntent();
        titre = i.getStringExtra("titre");
        url = "" + i.getStringExtra("url");
        imageView = (ImageView) findViewById(R.id.imageView);
        Glide.with(this.getApplicationContext()).load("" + url)
                .placeholder(R.drawable.image_set_erreur)
                .error(R.drawable.image_set_erreur)
                .into(imageView);
        PhotoViewAttacher photoView = new PhotoViewAttacher(imageView);
        photoView.update();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Uri bmpUri = Uri.fromFile(new File(url));
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                startActivity(Intent.createChooser(shareIntent, "Share Image"));
                break;
            case R.id.back:

                Intent i = new Intent(PictureZoom.this, MainActivity.class);
                startActivity(i);
        }
        return (super.onOptionsItemSelected(item));
    }
}
