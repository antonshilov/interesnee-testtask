package gq.vaccum121.testtask;

import android.database.Cursor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import gq.vaccum121.testtask.data.local.DatabaseHelper;
import gq.vaccum121.testtask.data.local.Db;
import gq.vaccum121.testtask.data.local.DbOpenHelper;
import gq.vaccum121.testtask.data.model.Place;
import gq.vaccum121.testtask.util.DefaultConfig;
import gq.vaccum121.testtask.util.MockPlaceFactory;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = DefaultConfig.EMULATE_SDK)
public class DatabaseHelperTest {
    final DatabaseHelper mDatabaseHelper = new DatabaseHelper(new DbOpenHelper(RuntimeEnvironment.application));

    @Before
    public void setUp() {
        mDatabaseHelper.clearTables().subscribe();
    }

    @Test
    public void setPlaces() {
        List<Place> places = MockPlaceFactory.newListOfPlaces(5);
        TestSubscriber<Place> result = new TestSubscriber<>();
        mDatabaseHelper.setPlaces(places).subscribe(result);
        result.assertNoErrors();
        result.assertReceivedOnNext(places);

        Cursor cursor = mDatabaseHelper.getBriteDb().query("SELECT * FROM " + Db.PlaceTable.TABLE_NAME);
        assertEquals(places.size(), cursor.getCount());
        for (Place place : places) {
            cursor.moveToNext();
            assertEquals(place, Db.PlaceTable.parseCursor(cursor));
        }
    }

    @Test
    public void addPlace() {
        Place place = MockPlaceFactory.newPlace();
        TestSubscriber<Place> result = new TestSubscriber<>();
        mDatabaseHelper.addPlace(place).subscribe(result);
        result.assertNoErrors();
        result.assertCompleted();

        Cursor cursor = mDatabaseHelper.getBriteDb().query("SELECT * FROM " + Db.PlaceTable.TABLE_NAME);
        assertEquals(1, cursor.getCount());
        cursor.moveToNext();
        assertEquals(place, Db.PlaceTable.parseCursor(cursor));
    }
}
