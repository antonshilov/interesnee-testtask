package gq.vaccum121.testtask;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import gq.vaccum121.testtask.data.DataManager;
import gq.vaccum121.testtask.data.model.Place;
import gq.vaccum121.testtask.ui.place_edit.PlaceEditMvpView;
import gq.vaccum121.testtask.ui.place_edit.PlaceEditPresenter;
import gq.vaccum121.testtask.util.MockPlaceFactory;
import gq.vaccum121.testtask.util.RxSchedulersOverrideRule;
import rx.Observable;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PlaceEditPresenterTest {
    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();
    @Mock
    PlaceEditMvpView mMockPlaceEditMvpView;
    @Mock
    DataManager mMockDataManager;

    private PlaceEditPresenter mPlacesEditPresenter;

    @Before
    public void setUp() {
        mPlacesEditPresenter = new PlaceEditPresenter(mMockDataManager);
        mPlacesEditPresenter.attachView(mMockPlaceEditMvpView);
    }

    @After
    public void end() {
        mPlacesEditPresenter.detachView();
    }

    @Test
    public void savePlace() {
        Place place = MockPlaceFactory.newPlace();
        doReturn(Observable.just(place))
                .when(mMockDataManager)
                .addPlace(place);
        mPlacesEditPresenter.savePlace(place);
        verify(mMockDataManager).addPlace(place);
        verify(mMockPlaceEditMvpView).finishActivity();
    }

    @Test
    public void savePlaceDoesNotSaveInvalidTextPlace() {
        Place place = MockPlaceFactory.newPlace();
        place.text = "";

        mPlacesEditPresenter.savePlace(place);
        verify(mMockDataManager, never()).addPlace(any(Place.class));
        verify(mMockPlaceEditMvpView, never()).finishActivity();
        verify(mMockPlaceEditMvpView).showPlaceTextError();
    }

    @Test
    public void savePlaceDoesNotSaveInvalidDatePlace() {
        Place place = MockPlaceFactory.newPlace();
        place.lastVisited = null;

        mPlacesEditPresenter.savePlace(place);
        verify(mMockDataManager, never()).addPlace(any(Place.class));
        verify(mMockPlaceEditMvpView, never()).finishActivity();
        verify(mMockPlaceEditMvpView).showPlaceDateError();
    }

    @Test
    public void savePlaceDoesNotSaveInvalidLatitudePlace() {
        Place place = MockPlaceFactory.newPlace();
        place.latitude = null;

        mPlacesEditPresenter.savePlace(place);
        verify(mMockDataManager, never()).addPlace(any(Place.class));
        verify(mMockPlaceEditMvpView, never()).finishActivity();
        verify(mMockPlaceEditMvpView).showPlaceLocationError();
    }

    @Test
    public void savePlaceDoesNotSaveInvalidLongitudePlace() {
        Place place = MockPlaceFactory.newPlace();
        place.longitude = null;

        mPlacesEditPresenter.savePlace(place);
        verify(mMockDataManager, never()).addPlace(any(Place.class));
        verify(mMockPlaceEditMvpView, never()).finishActivity();
        verify(mMockPlaceEditMvpView).showPlaceLocationError();
    }

    @Test
    public void savePlaceDoesNotSaveInvalidImagePlace() {
        Place place = MockPlaceFactory.newPlace();
        place.image = null;
        doReturn(Observable.just(place))
                .when(mMockDataManager)
                .addPlace(place);
        mPlacesEditPresenter.savePlace(place);
        assertNotNull(place.image);
        verify(mMockDataManager).addPlace(any(Place.class));
        verify(mMockPlaceEditMvpView).finishActivity();
    }
}
