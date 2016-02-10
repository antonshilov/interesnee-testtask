package gq.vaccum121.testtask.ui.map;

import java.util.List;

import javax.inject.Inject;

import gq.vaccum121.testtask.data.DataManager;
import gq.vaccum121.testtask.data.model.Place;
import gq.vaccum121.testtask.ui.base.BasePresenter;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MapPresenter extends BasePresenter<MapMvpView> {
    private final DataManager mDataManager;
    public Subscription mSubscription;

    @Inject
    public MapPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void loadPlaces() {
        checkViewAttached();
        mSubscription = mDataManager.getPlaces()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Place>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("Places loading error!");
                        getMvpView().showLoadingError();
                    }

                    @Override
                    public void onNext(List<Place> places) {
                        getMvpView().showPlacesMarkers(places);
                    }
                });

    }
}
