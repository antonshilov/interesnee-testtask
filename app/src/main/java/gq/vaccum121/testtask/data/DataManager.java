package gq.vaccum121.testtask.data;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import gq.vaccum121.testtask.data.local.DatabaseHelper;
import gq.vaccum121.testtask.data.model.Place;
import gq.vaccum121.testtask.data.remote.PlaceService;
import rx.Observable;
import rx.functions.Func1;

@Singleton
public class DataManager {
    private final PlaceService mPlaceService;
    private final DatabaseHelper mDataBaseHelper;

    @Inject
    public DataManager(PlaceService mPlaceService, DatabaseHelper mDataBaseHelper) {
        this.mPlaceService = mPlaceService;
        this.mDataBaseHelper = mDataBaseHelper;
    }

    public Observable<List<Place>> getPlaces() {
        return mDataBaseHelper.getPlaces();
    }

    public Observable<Place> syncPlaces() {
        return mPlaceService.getPlaces()
                .concatMap(new Func1<List<Place>, Observable<Place>>() {
                    @Override
                    public Observable<Place> call(List<Place> places) {
                        return mDataBaseHelper.setPlaces(places);
                    }
                });
    }


    public Observable<Place> addPlace(Place place) {
        return mDataBaseHelper.addPlace(place);
    }
}
