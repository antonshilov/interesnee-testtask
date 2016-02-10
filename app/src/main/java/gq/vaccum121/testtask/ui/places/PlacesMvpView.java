package gq.vaccum121.testtask.ui.places;

import java.util.List;

import gq.vaccum121.testtask.data.model.Place;
import gq.vaccum121.testtask.ui.base.MvpView;

public interface PlacesMvpView extends MvpView {
    void showPlaces(List<Place> places);

    void showEmpty();

    void showErrMsg();

    void showProgress();
}
