package gq.vaccum121.testtask.ui.map;

import java.util.List;

import gq.vaccum121.testtask.data.model.Place;
import gq.vaccum121.testtask.ui.base.MvpView;

public interface MapMvpView extends MvpView {
    void showPlacesMarkers(List<Place> places);

    void showLoadingError();

}
