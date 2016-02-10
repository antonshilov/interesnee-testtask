package gq.vaccum121.testtask.ui.place_edit;

import javax.inject.Inject;

import gq.vaccum121.testtask.data.DataManager;
import gq.vaccum121.testtask.data.model.Place;
import gq.vaccum121.testtask.ui.base.BasePresenter;
import gq.vaccum121.testtask.util.MapsUtil;

public class PlaceEditPresenter extends BasePresenter<PlaceEditMvpView> {
    private DataManager mDataManager;

    @Inject
    public PlaceEditPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    public void savePlace(Place place) {
        if (verifyPlace(place)) {
            mDataManager.addPlace(place).subscribe();
            getMvpView().finishActivity();
        }
    }

    private boolean verifyPlace(Place place) {
        boolean result = true;
        if (place.text.isEmpty()) {
            getMvpView().showPlaceTextError();
            result = false;
        }
        if (place.lastVisited == null) {
            getMvpView().showPlaceDateError();
            result = false;
        }
        if (place.latitude == null || place.longitude == null) {
            getMvpView().showPlaceLocationError();
            result = false;
        }
        if (place.image == null && result == true) {
            place.image = MapsUtil.getMapUrl(place.latitude, place.longitude);
        }
        return result;
    }
}
