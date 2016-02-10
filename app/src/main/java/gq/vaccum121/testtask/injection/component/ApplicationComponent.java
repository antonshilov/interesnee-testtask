package gq.vaccum121.testtask.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import gq.vaccum121.testtask.TestTaskApplication;
import gq.vaccum121.testtask.data.DataManager;
import gq.vaccum121.testtask.data.local.DatabaseHelper;
import gq.vaccum121.testtask.data.remote.PlaceService;
import gq.vaccum121.testtask.injection.ApplicationContext;
import gq.vaccum121.testtask.injection.module.ApplicationModule;
import gq.vaccum121.testtask.service.SyncService;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(SyncService syncService);


    @ApplicationContext
    Context context();

    Application application();

    PlaceService placeService();

    DatabaseHelper databaseHelper();

    DataManager dataManager();


    void inject(TestTaskApplication testTaskApplication);
}
