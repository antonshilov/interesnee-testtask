package gq.vaccum121.testtask.injection.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import gq.vaccum121.testtask.data.remote.PlaceService;
import gq.vaccum121.testtask.injection.ApplicationContext;

@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    PlaceService providePlaceService() {
        return PlaceService.Factory.makePlaceService(mApplication);
    }
}
