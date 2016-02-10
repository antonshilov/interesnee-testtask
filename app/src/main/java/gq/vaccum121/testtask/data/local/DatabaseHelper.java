package gq.vaccum121.testtask.data.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import gq.vaccum121.testtask.data.model.Place;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * The  DatabaseHelper handles inserting, updating and retrieving data from a local SQLite database.
 */
@Singleton
public class DatabaseHelper {
    private final BriteDatabase mDb;

    @Inject
    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        mDb = SqlBrite.create().wrapDatabaseHelper(dbOpenHelper);
    }

    public BriteDatabase getBriteDb() {
        return mDb;
    }

    public Observable<Void> clearTables() {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    Cursor cursor = mDb.query("SELECT name FROM sqlite_master WHERE type='table'");
                    while (cursor.moveToNext()) {
                        mDb.delete(cursor.getString(cursor.getColumnIndex("name")), null);
                    }
                    cursor.close();
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<Place> setPlaces(final List<Place> newPlaces) {
        return Observable.create(new Observable.OnSubscribe<Place>() {
            @Override
            public void call(Subscriber<? super Place> subscriber) {
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    for (Place place : newPlaces) {
                        long result = mDb.insert(Db.PlaceTable.TABLE_NAME,
                                Db.PlaceTable.toContentValues(place),
                                SQLiteDatabase.CONFLICT_REPLACE);
                        if (result >= 0) subscriber.onNext(place);
                    }
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }

    public Observable<List<Place>> getPlaces() {
        return mDb.createQuery(Db.PlaceTable.TABLE_NAME,
                "SELECT * FROM " + Db.PlaceTable.TABLE_NAME)
                .mapToList(new Func1<Cursor, Place>() {
                    @Override
                    public Place call(Cursor cursor) {
                        return Db.PlaceTable.parseCursor(cursor);
                    }
                });
    }

    public Observable<Place> addPlace(final Place place) {
        return Observable.create(new Observable.OnSubscribe<Place>() {
            @Override
            public void call(Subscriber<? super Place> subscriber) {
                BriteDatabase.Transaction transaction = mDb.newTransaction();
                try {
                    mDb.insert(Db.PlaceTable.TABLE_NAME,
                            Db.PlaceTable.toContentValues(place),
                            SQLiteDatabase.CONFLICT_REPLACE);
                    transaction.markSuccessful();
                    subscriber.onCompleted();
                } finally {
                    transaction.end();
                }
            }
        });
    }
}
