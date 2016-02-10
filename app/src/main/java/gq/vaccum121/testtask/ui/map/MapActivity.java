package gq.vaccum121.testtask.ui.map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import gq.vaccum121.testtask.R;
import gq.vaccum121.testtask.data.model.Place;
import gq.vaccum121.testtask.ui.base.BaseActivity;

public class MapActivity extends BaseActivity implements MapMvpView, OnMapReadyCallback {
    public static final String EXTRA_FOCUS_PLACE = "gq.vaccum121.testtask.ui.map.MapActivity.EXTRA_FOCUS_PLACE";
    @Inject
    MapPresenter mMapPresenter;
    private GoogleMap mMap;
    private List<Marker> mMarkers = new ArrayList<>();

    public static Intent getStartIntent(Context context, Place place) {
        Intent intent = new Intent(context, MapActivity.class);
        if (place == null) {
            intent.putExtra(EXTRA_FOCUS_PLACE, (Place) null);
        } else {
            intent.putExtra(EXTRA_FOCUS_PLACE, place);
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mMapPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapPresenter.detachView();
    }

    @Nullable
    private LatLng getExtraPlaceLatLng() {
        Place place = getIntent().getParcelableExtra(EXTRA_FOCUS_PLACE);
        if (place == null) {
            return null;
        } else {
            return new LatLng(place.latitude, place.longitude);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMapPresenter.loadPlaces();
        LatLng focusLatLng = getExtraPlaceLatLng();
        if (focusLatLng != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(focusLatLng)
                    .zoom(15)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void showPlacesMarkers(List<Place> places) {
        for (Place place : places) {
            mMarkers.add(mMap.addMarker(new MarkerOptions().
                    position(new LatLng(place.latitude, place.longitude))
                    .title(place.text)));
        }
    }

    @Override
    public void showLoadingError() {
        Toast.makeText(this, "Places loading error!", Toast.LENGTH_SHORT).show();
    }
}
