package gq.vaccum121.testtask;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import gq.vaccum121.testtask.data.DataManager;
import gq.vaccum121.testtask.data.local.DatabaseHelper;
import gq.vaccum121.testtask.data.model.Place;
import gq.vaccum121.testtask.data.remote.PlaceService;
import gq.vaccum121.testtask.util.MockPlaceFactory;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {
    @Mock
    PlaceService mMockPlaceService;
    @Mock
    DatabaseHelper mMockDataBaseHelper;
    DataManager mDataManager;

    @Before
    public void setUp() {
        mDataManager = new DataManager(mMockPlaceService, mMockDataBaseHelper);
    }

    @Test
    public void syncPlacesEmitsValues() {
        List<Place> places = MockPlaceFactory.newListOfPlaces(5);
        stubSyncPlacesCalls(places);

        TestSubscriber<Place> result = new TestSubscriber<>();
        mDataManager.syncPlaces().subscribe(result);
        result.assertNoErrors();
        result.assertReceivedOnNext(places);

    }

    @Test
    public void syncPlacesCallsDatabaseAndApi() {
        List<Place> places = MockPlaceFactory.newListOfPlaces(5);
        stubSyncPlacesCalls(places);

        mDataManager.syncPlaces().subscribe();
        verify(mMockPlaceService).getPlaces();
        verify(mMockDataBaseHelper).setPlaces(places);

    }

    @Test
    public void syncPlacesDoesNotCallDatabaseIfApiFails() {
        when(mMockPlaceService.getPlaces()).
                thenReturn(Observable.<List<Place>>error(new RuntimeException()));

        mDataManager.syncPlaces().subscribe(new TestSubscriber<Place>());
        verify(mMockPlaceService).getPlaces();
        verify(mMockDataBaseHelper, never()).setPlaces(anyListOf(Place.class));

    }

    @Test
    public void addPlace() {
        Place place = MockPlaceFactory.newPlace();
        TestSubscriber<Place> result = new TestSubscriber<>();

        when(mMockDataBaseHelper.addPlace(place)).thenReturn(Observable.just(place));

        mDataManager.addPlace(place).subscribe(result);
        result.assertNoErrors();
        result.assertCompleted();
    }


    private void stubSyncPlacesCalls(List<Place> places) {
        when(mMockPlaceService.getPlaces()).thenReturn(Observable.just(places));
        when(mMockDataBaseHelper.setPlaces(places)).thenReturn(Observable.from(places));
    }
}
