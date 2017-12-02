package tn.abdessamed.yessine.tagthebus.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "pictureManagerDB";

    private static final String TABLE_STATION = "picturesTable";

    private static final String KEY_ID = "id";
    private static final String KEY_ID_STATION = "idStation";
    private static final String KEY_NAME = "nomStation";
    private static final String KEY_DATE = "dateCreation";
    private static final String KEY_TITRE = "titre";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_STATION + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_TITRE + " TEXT," + KEY_ID_STATION + " INTEGER,"
                + KEY_DATE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATION);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new photo station
    public void addPicture(PictureStation picture) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, picture.getNomStation());
        values.put(KEY_ID_STATION, picture.getIdStation());
        values.put(KEY_TITRE, picture.getTitre());

        values.put(KEY_DATE, picture.getDateCreation()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_STATION, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact


    // Getting All stations
    public List<PictureStation> getAll() {
        List<PictureStation> contactList = new ArrayList<PictureStation>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_STATION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PictureStation contact = new PictureStation();
                contact.setId(Integer.parseInt(cursor.getString(0))); //id

                contact.setNomStation(cursor.getString(1));//name
                contact.setTitre(cursor.getString(2));//titre
                contact.setIdStation(Integer.parseInt(cursor.getString(3)));//id sattion
                contact.setDateCreation(cursor.getString(4));//date
                // Adding station to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        return contactList;
    }

    // Updating single contact
    public int updateStation(PictureStation picture) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, picture.getNomStation());
        values.put(KEY_DATE, picture.getDateCreation());

        // updating row
        return db.update(TABLE_STATION, values, KEY_ID + " = ?",
                new String[]{String.valueOf(picture.getId())});
    }

    // Deleting single station
    public void deleteStation(PictureStation picture) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STATION, KEY_ID + " = ?",
                new String[]{String.valueOf(picture.getId())});
        db.close();
    }


    // Getting stations Count
    public int getSationCount() {
        String countQuery = "SELECT  * FROM " + TABLE_STATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
