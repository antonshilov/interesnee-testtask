package gq.vaccum121.testtask.injection.component;

import dagger.Component;
import gq.vaccum121.testtask.injection.PerActivity;
import gq.vaccum121.testtask.injection.module.ActivityModule;
import gq.vaccum121.testtask.ui.main.MainActivity;
import gq.vaccum121.testtask.ui.map.MapActivity;
import gq.vaccum121.testtask.ui.place_edit.PlaceEditActivity;
import gq.vaccum121.testtask.ui.places.PlacesFragment;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);

    void inject(PlacesFragment placesFragment);

    void inject(PlaceEditActivity placeEditActivity);

    void inject(MapActivity mapActivity);
}
