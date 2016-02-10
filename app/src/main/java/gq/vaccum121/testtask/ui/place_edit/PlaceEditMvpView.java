package gq.vaccum121.testtask.ui.place_edit;

import java.util.Date;

import gq.vaccum121.testtask.ui.base.MvpView;

public interface PlaceEditMvpView extends MvpView {
    void displayPlacePicker();

    void displayDatePicker();

    void showPlaceText(String text);

    void showPlaceDate(Date date);

    void showPlaceLocation(double latitude, double longitude);

    void showPlaceImage(String path);

    void showPlaceTextError();

    void showPlaceDateError();

    void showPlaceLocationError();

    void showCurrentDate();

    void finishActivity();
}
