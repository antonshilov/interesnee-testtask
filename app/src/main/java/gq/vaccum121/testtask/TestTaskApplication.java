package gq.vaccum121.testtask;

import android.app.Application;
import android.content.Context;

import gq.vaccum121.testtask.injection.component.ApplicationComponent;
import gq.vaccum121.testtask.injection.component.DaggerApplicationComponent;
import gq.vaccum121.testtask.injection.module.ApplicationModule;
import timber.log.Timber;

public class TestTaskApplication extends Application {
    ApplicationComponent mApplicationComponent;

    public static TestTaskApplication get(Context context) {
        return (TestTaskApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        mApplicationComponent.inject(this);

    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    public void setComponent(ApplicationComponent component) {
        this.mApplicationComponent = component;
    }
}
