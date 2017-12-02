package uploads;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import tn.abdessamed.yessine.tagthebus.publication.MainPubActivity;
import tn.abdessamed.yessine.tagthebus.sqlite.DatabaseHandler;
import tn.abdessamed.yessine.tagthebus.sqlite.PictureStation;
import tn.abdessamed.yessine.tagthebus.R;

public class UploadActivity extends Activity {
    // LogCat tag
    private static final String TAG = CaptureActivity.class.getSimpleName();

    private String filePath = null;
    private ImageView imgPreview;
    private VideoView vidPreview;
    private Button btnUpload;
    private EditText desc;
    private TextView Description;

    Context mContext;
    String url = "";
    Button buttoncrop;
    private Vibrator vib;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        vidPreview = (VideoView) findViewById(R.id.videoPreview);
        desc = (EditText) findViewById(R.id.desc);

        mContext = this.getApplicationContext();
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        Description = (TextView) findViewById(R.id.textView);

        // Receiving the data from previous activity
        Intent i = getIntent();
        id = Integer.parseInt(getIntent().getStringExtra("s"));
        filePath = i.getStringExtra("filePath");

        boolean isImage = i.getBooleanExtra("isImage", true);

        if (filePath != null) {
            // Displaying the image or video on the screen
            previewMedia(isImage);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }


        desc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (desc.getText().length() < 2) {
                    desc.setError("svp ajouter une description");
                    desc.setFocusable(true);
                }

            }
        });



        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                String dateAjout = (new SimpleDateFormat("yyyy/MM/dd   HH:mm").format(Calendar.getInstance().getTime()));

                DatabaseHandler db = new DatabaseHandler(mContext);
                PictureStation p = new PictureStation(id, filePath.toString(), desc.getText().toString(), dateAjout);
                db.addPicture(p);
                List<PictureStation> contacts = db.getAll();

                for (PictureStation cn : contacts) {

                }
                //	int  id = station.getId();
                Intent i = new Intent(UploadActivity.this, MainPubActivity.class);
                i.putExtra("id", "" + id);
                startActivity(i);


            }


        });





    }

    /**
     * Displaying captured image/video on the screen
     */
    private void previewMedia(boolean isImage) {
        // Checking whether captured media is image or video
        if (isImage) {
            imgPreview.setVisibility(View.VISIBLE);
            vidPreview.setVisibility(View.GONE);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

            imgPreview.setImageBitmap(bitmap);
        } else {
            imgPreview.setVisibility(View.GONE);
            vidPreview.setVisibility(View.VISIBLE);
            vidPreview.setVideoPath(filePath);
            // start playing
            vidPreview.start();
        }
    }


}