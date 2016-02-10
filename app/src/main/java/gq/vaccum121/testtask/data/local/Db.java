package gq.vaccum121.testtask.data.local;

import android.content.ContentValues;
import android.database.Cursor;

import gq.vaccum121.testtask.data.model.Place;
import gq.vaccum121.testtask.util.DateUtil;


public class Db {
    public Db() {
    }

    public abstract static class PlaceTable {
        public static final String TABLE_NAME = "places";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_LAST_VISITED = "lastVisited";


        public static final String CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_TEXT + " TEXT PRIMARY KEY," +
                        COLUMN_LATITUDE + " DOUBLE," +
                        COLUMN_LONGITUDE + " DOUBLE," +
                        COLUMN_LAST_VISITED + " DATE," +
                        COLUMN_IMAGE + " TEXT" +
                        " ); ";


        public static ContentValues toContentValues(Place place) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TEXT, place.text);
            values.put(COLUMN_LATITUDE, place.latitude);
            values.put(COLUMN_LONGITUDE, place.longitude);
            values.put(COLUMN_IMAGE, place.image);
            values.put(COLUMN_LAST_VISITED, String.valueOf(place.lastVisited));
            return values;
        }


        public static Place parseCursor(Cursor cursor) {
            Place place = new Place();
            place.text = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEXT));
            place.latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LATITUDE));
            place.longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_LONGITUDE));
            place.image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE));
            place.lastVisited = DateUtil.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_VISITED)));
            return place;
        }
    }
}
