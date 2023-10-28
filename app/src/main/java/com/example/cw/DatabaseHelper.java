package com.example.cw;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cw.db";
    private static final int DATABASE_VERSION = 1;

    // Define the table Hiking
    private static final String TABLE_NAME_Hiking = "hiking";
    private static final String COLUMN_ID_Hiking = "id";
    private static final String COLUMN_NAME_Hiking = "name";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_PARKING_AVAILABLE = "parkingAvailable";
    private static final String COLUMN_LENGTH_OF_HIKE = "lengthOfHike";
    private static final String COLUMN_DIFFICULT_LEVEL = "difficultLevel";
    private static final String COLUMN_DESCRIPTION = "description";

    // Define the table Observation
    private static final String TABLE_NAME_Observation = "observation";
    private static final String COLUMN_ID_Observation = "id";
    private static final String COLUMN_NAME_Observation = "name";
    private static final String COLUMN_Time = "time";
    private static final String COLUMN_Comment = "comment";
    private static final String COLUMN_HikingId = "hikingId";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        // Create the table with the specified columns
        String createTableHikingSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_Hiking + " (" +
                COLUMN_ID_Hiking + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME_Hiking + " TEXT, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_PARKING_AVAILABLE + " TEXT, " +
                COLUMN_LENGTH_OF_HIKE + " TEXT, " +
                COLUMN_DIFFICULT_LEVEL + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT );";

        String createTableObservationSQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_Observation + " (" +
                COLUMN_ID_Observation + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME_Observation + " TEXT, " +
                COLUMN_Time + " TEXT, " +
                COLUMN_Comment + " TEXT, " +
                COLUMN_HikingId + " INTEGER);";

        db.execSQL(createTableHikingSQL);
        db.execSQL(createTableObservationSQL);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertHiking(String name, String location,
                             String date, String parkingAvailable,
                             String lengthOfHike, String difficultLevel, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_Hiking, name);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_PARKING_AVAILABLE, parkingAvailable);
        values.put(COLUMN_LENGTH_OF_HIKE, lengthOfHike);
        values.put(COLUMN_DIFFICULT_LEVEL, difficultLevel);
        values.put(COLUMN_DESCRIPTION, description);
        long newRowId = db.insert(TABLE_NAME_Hiking, null, values);
        db.close();
        return newRowId;
    }

    public int updateHiking(long id,
                            String name, String location, String date,
                            String parkingAvailable, String lengthOfHike, String difficultLevel, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_Hiking, name);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_PARKING_AVAILABLE, parkingAvailable);
        values.put(COLUMN_LENGTH_OF_HIKE, lengthOfHike);
        values.put(COLUMN_DIFFICULT_LEVEL, difficultLevel);
        values.put(COLUMN_DESCRIPTION, description);
        int rowsUpdated = db.update(TABLE_NAME_Hiking, values, COLUMN_ID_Hiking + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsUpdated;
    }

    @SuppressLint("Range")
    public Hiking getHikingRecordById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_ID_Hiking + "=?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(TABLE_NAME_Hiking, null, selection, selectionArgs, null, null, null);
        Hiking hikingData = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int Id = cursor.getColumnIndex("id");
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String parkingAvailable = cursor.getString(cursor.getColumnIndex("parkingAvailable"));
                String lengthOfHike = cursor.getString(cursor.getColumnIndex("lengthOfHike"));
                String difficultLevel = cursor.getString(cursor.getColumnIndex("difficultLevel"));
                String description = cursor.getString(cursor.getColumnIndex("description"));

                hikingData = new Hiking(
                        cursor.getInt(Id),
                        name,
                        location,
                        date,
                        parkingAvailable,
                        lengthOfHike,
                        difficultLevel,
                        description
                );
            }
            cursor.close();
        }
        db.close();
        return hikingData;
    }

    public List<Hiking> getAllHikings() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_Hiking, null, null, null, null, null, null);

        List<Hiking> dataList = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int Id = cursor.getColumnIndex("id");
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex("location"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
                @SuppressLint("Range") String parkingAvailable = cursor.getString(cursor.getColumnIndex("parkingAvailable"));
                @SuppressLint("Range") String lengthOfHike = cursor.getString(cursor.getColumnIndex("lengthOfHike"));
                @SuppressLint("Range") String difficultLevel = cursor.getString(cursor.getColumnIndex("difficultLevel"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                Hiking hiking = new Hiking(cursor.getInt(Id),
                        name, location, date, parkingAvailable, lengthOfHike, difficultLevel, description);
                dataList.add(hiking);
            }
            cursor.close();
        }
        db.close();
        return dataList;
    }

    public int deleteHiking(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME_Hiking, COLUMN_ID_Hiking + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted;
    }

    public long insertObservation(String name, String time, String comment, int hikingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_Observation, name);
        values.put(COLUMN_Time, time);
        values.put(COLUMN_Comment, comment);
        values.put(COLUMN_HikingId, hikingId);
        long newRowId = db.insert(TABLE_NAME_Observation, null, values);
        db.close();
        return newRowId;
    }

    public int updateObservation(int id, String name, String time, String comment, int hikingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_Observation, name);
        values.put(COLUMN_Time, time);
        values.put(COLUMN_Comment, comment);
        values.put(COLUMN_HikingId, hikingId);
        int rowsUpdated = db.update(TABLE_NAME_Observation, values, COLUMN_ID_Observation + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsUpdated;
    }

    public int deleteObservation(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted =
                db.delete(TABLE_NAME_Observation, COLUMN_ID_Observation + "=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted;
    }

    @SuppressLint("Range")
    public Observation getObservationById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                COLUMN_ID_Observation,
                COLUMN_NAME_Observation,
                COLUMN_Time,
                COLUMN_Comment,
                COLUMN_HikingId
        };
        String selection = COLUMN_ID_Observation + "=?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = db.query(TABLE_NAME_Observation, projection, selection, selectionArgs, null, null, null);
        Observation observationData = null;
        if (cursor != null && cursor.moveToFirst()) {
            int obsId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_Observation));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_Observation));
            String time = cursor.getString(cursor.getColumnIndex(COLUMN_Time));
            String comment = cursor.getString(cursor.getColumnIndex(COLUMN_Comment));
            int hikingId = cursor.getInt(cursor.getColumnIndex(COLUMN_HikingId));
            observationData = new Observation(obsId, name, time, comment, hikingId);
            cursor.close();
        }
        db.close();
        return observationData;
    }

    public List<Observation> getAllObservations(int hikingId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_HikingId + "=?";
        String[] selectionArgs = {String.valueOf(hikingId)};
        Cursor cursor = db.query(TABLE_NAME_Observation, null, selection, selectionArgs, null, null, null);

        List<Observation> dataList = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int obsId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_Observation));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_Observation));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(COLUMN_Time));
                @SuppressLint("Range")String comment = cursor.getString(cursor.getColumnIndex(COLUMN_Comment));
                @SuppressLint("Range") int hikeId = cursor.getInt(cursor.getColumnIndex(COLUMN_HikingId));
                Observation observation = new Observation(obsId, name, time, comment, hikeId);
                dataList.add(observation);
            }
            cursor.close();
        }
        db.close();
        return dataList;
    }
}
