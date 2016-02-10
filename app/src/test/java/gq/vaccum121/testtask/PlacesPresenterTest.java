package gq.vaccum121.testtask;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import gq.vaccum121.testtask.data.DataManager;
import gq.vaccum121.testtask.data.model.Place;
import gq.vaccum121.testtask.ui.places.PlacesMvpView;
import gq.vaccum121.testtask.ui.places.PlacesPresenter;
import gq.vaccum121.testtask.util.MockPlaceFactory;
import gq.vaccum121.testtask.util.RxSchedulersOverrideRule;
import rx.Observable;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PlacesPresenterTest {
    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();
    @Mock
    PlacesMvpView mMockPlacesMvpView;
    @Mock
    DataManager mMockDataManager;
    private PlacesPresenter mPlacesPresenter;

    @Before
    public void setUp() {
        mPlacesPresenter = new PlacesPresenter(mMockDataManager);
        mPlacesPresenter.attachView(mMockPlacesMvpView);
    }

    @After
    public void end() {
        mPlacesPresenter.detachView();
    }

    @Test
    public void loadPlaces() {
        List<Place> places = MockPlaceFactory.newListOfPlaces(15);
        doReturn(Observable.just(places))
                .when(mMockDataManager)
                .getPlaces();
        mPlacesPresenter.loadPlaces();
        verify(mMockPlacesMvpView).showPlaces(places);
        verify(mMockPlacesMvpView, never()).showEmpty();
        verify(mMockPlacesMvpView, never()).showErrMsg();
    }

    @Test
    public void loadPlacesEmptyList() {
        doReturn(Observable.just(Collections.emptyList()))
                .when(mMockDataManager)
                .getPlaces();
        mPlacesPresenter.loadPlaces();
        verify(mMockPlacesMvpView).showEmpty();
        verify(mMockPlacesMvpView, never()).showPlaces(anyListOf(Place.class));
        verify(mMockPlacesMvpView, never()).showErrMsg();
    }

    @Test
    public void loadPlacesError() {
        doReturn(Observable.error(new RuntimeException()))
                .when(mMockDataManager)
                .getPlaces();
        mPlacesPresenter.loadPlaces();
        verify(mMockPlacesMvpView).showErrMsg();
        verify(mMockPlacesMvpView, never()).showEmpty();
        verify(mMockPlacesMvpView, never()).showPlaces(anyListOf(Place.class));
    }
}
