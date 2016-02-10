package gq.vaccum121.testtask.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import javax.inject.Inject;

import gq.vaccum121.testtask.BuildConfig;
import gq.vaccum121.testtask.TestTaskApplication;
import gq.vaccum121.testtask.data.DataManager;
import gq.vaccum121.testtask.data.model.Place;
import gq.vaccum121.testtask.util.AndroidComponentUtil;
import gq.vaccum121.testtask.util.NetworkUtil;
import rx.Observer;
import rx.Subscription;
import rx.schedulers.Schedulers;
import timber.log.Timber;


public class SyncService extends Service {
    @Inject
    DataManager mDataManager;
    private Subscription mPlaceSubscription;


    public static Intent getStartIntent(Context context) {
        return new Intent(context, SyncService.class);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        TestTaskApplication.get(this).getComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Timber.i("Start syncing...");


        if (!NetworkUtil.isNetworkConnected(this)) {
            Timber.i("Connection is not available");
            Toast.makeText(getApplicationContext(), "Connection is not available", Toast.LENGTH_LONG).show();
            AndroidComponentUtil.toggleComponent(this, SyncOnConnectionAvailable.class, true);
            stopSelf(startId);
            return START_NOT_STICKY;
        }

        if (mPlaceSubscription != null && !mPlaceSubscription.isUnsubscribed())
            mPlaceSubscription.unsubscribe();

        mPlaceSubscription = mDataManager.syncPlaces()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Place>() {
                    @Override
                    public void onCompleted() {
                        if (BuildConfig.DEBUG) Timber.i("Places Synced successfully");
                        stopSelf(startId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (BuildConfig.DEBUG) {
                            String errorMsg = String.format("Places Syncing error %s", e);
                            Timber.e(errorMsg);
                        }
                        stopSelf(startId);
                    }

                    @Override
                    public void onNext(Place place) {

                    }
                });

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (mPlaceSubscription != null) mPlaceSubscription.unsubscribe();
        super.onDestroy();
    }

    public static class SyncOnConnectionAvailable extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (NetworkUtil.isNetworkConnected(context)) {
                if (BuildConfig.DEBUG) {
                    Timber.i("Connection is now available, triggering sync...");
                }
                AndroidComponentUtil.toggleComponent(context, this.getClass(), false);
                context.startService(getStartIntent(context));
            }
        }
    }
}
