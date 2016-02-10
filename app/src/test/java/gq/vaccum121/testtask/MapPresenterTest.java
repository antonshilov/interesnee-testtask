package gq.vaccum121.testtask;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import gq.vaccum121.testtask.data.DataManager;
import gq.vaccum121.testtask.data.model.Place;
import gq.vaccum121.testtask.ui.map.MapMvpView;
import gq.vaccum121.testtask.ui.map.MapPresenter;
import gq.vaccum121.testtask.util.MockPlaceFactory;
import gq.vaccum121.testtask.util.RxSchedulersOverrideRule;
import rx.Observable;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MapPresenterTest {
    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();
    @Mock
    MapMvpView mMockMapMvpView;
    @Mock
    DataManager mMockDataManager;
    private MapPresenter mMapPresenter;

    @Before
    public void setUp() {
        mMapPresenter = new MapPresenter(mMockDataManager);
        mMapPresenter.attachView(mMockMapMvpView);
    }

    @After
    public void end() {
        mMapPresenter.detachView();
    }

    @Test
    public void loadPlaces() {
        List<Place> places = MockPlaceFactory.newListOfPlaces(15);
        doReturn(Observable.just(places))
                .when(mMockDataManager)
                .getPlaces();
        mMapPresenter.loadPlaces();
        verify(mMockMapMvpView).showPlacesMarkers(places);
        verify(mMockMapMvpView, never()).showLoadingError();
    }


    @Test
    public void loadPlacesError() {
        doReturn(Observable.error(new RuntimeException()))
                .when(mMockDataManager)
                .getPlaces();
        mMapPresenter.loadPlaces();
        verify(mMockMapMvpView).showLoadingError();
        verify(mMockMapMvpView, never()).showPlacesMarkers(anyListOf(Place.class));
    }
}
