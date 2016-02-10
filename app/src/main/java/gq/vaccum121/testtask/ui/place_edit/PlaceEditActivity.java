package gq.vaccum121.testtask.ui.place_edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gq.vaccum121.testtask.BuildConfig;
import gq.vaccum121.testtask.R;
import gq.vaccum121.testtask.ui.base.BaseActivity;
import gq.vaccum121.testtask.ui.map.MapActivity;
import timber.log.Timber;


public class PlaceEditActivity extends BaseActivity implements PlaceEditMvpView, DatePickerDialog.OnDateSetListener {
    private static final String EXTRA_EDIT_PLACE = "gq.vaccum121.testtask.ui.place_edit.PlaceDetailActivity.EXTRA_EDIT_PLACE";
    private static final int PLACE_PICKER_REQUEST = 19681;
    @Inject
    protected PlaceEditPresenter mPlaceEditPresenter;
    DateFormat mDateFormat;
    @Bind(R.id.edittext_text_edit_place)
    EditText mText;
    @Bind(R.id.image_edit_place)
    ImageView mImage;
    @Bind(R.id.edittext_latitude_edit_place)
    EditText mLatitude;
    @Bind(R.id.edittext_longitude_edit_place)
    EditText mLongitude;
    @Bind(R.id.edittext_last_visited_edit_place)
    EditText mDate;
    @Bind(R.id.fab_edit_place)
    FloatingActionButton mFab;
    @Bind(R.id.btn_last_visited_edit_place)
    ImageButton mDateButton;

    private gq.vaccum121.testtask.data.model.Place mPlace;

    public static Intent getStartIntent(Context context, gq.vaccum121.testtask.data.model.Place place) {
        Intent intent = new Intent(context, PlaceEditActivity.class);
        if (place == null) {
            intent.putExtra(EXTRA_EDIT_PLACE, (gq.vaccum121.testtask.data.model.Place) null);
        } else {
            intent.putExtra(EXTRA_EDIT_PLACE, place);
        }
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_edit);
        activityComponent().inject(this);
        ButterKnife.bind(this);
        mPlaceEditPresenter.attachView(this);
        mDateFormat = android.text.format.DateFormat.getDateFormat(this);
        setExtraPlace();
    }

    private void setExtraPlace() {
        mPlace = getIntent().getParcelableExtra(EXTRA_EDIT_PLACE);
        if (mPlace == null) {
            mPlace = new gq.vaccum121.testtask.data.model.Place();
            showCurrentDate();
        } else {
            showPlaceText(mPlace.text);
            showPlaceImage(mPlace.image);
            showPlaceDate(mPlace.lastVisited);
            showPlaceLocation(mPlace.latitude, mPlace.longitude);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlaceEditPresenter.detachView();
        mPlace = null;

    }

    @Override
    public void displayPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            if (BuildConfig.DEBUG) Timber.e(e.getMessage());
        } catch (GooglePlayServicesNotAvailableException e) {
            if (BuildConfig.DEBUG) Timber.e(e.getMessage());
            Toast.makeText(this, "Google Play Services is not available", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void displayDatePicker() {
        DatePickerDialog dpd;
        if (mPlace.lastVisited == null) {
            Calendar now = Calendar.getInstance();
            dpd = DatePickerDialog.newInstance(
                    PlaceEditActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
        } else {

            /**
             * Workaround with java.Util.Date deprecated implementation.
             * Probably need to migrate to JodaTime.
             */
            int year = mPlace.lastVisited.getYear() + 1900;
            dpd = DatePickerDialog.newInstance(
                    PlaceEditActivity.this,
                    year,
                    mPlace.lastVisited.getMonth(),
                    mPlace.lastVisited.getDay()
            );
        }

        dpd.show(getFragmentManager(), "DatePicker dialog");
    }

    @Override
    public void showPlaceText(String text) {
        mText.setText(text);
    }

    @Override
    public void showPlaceDate(Date date) {
        mDate.setText(mDateFormat.format(date));
    }

    @Override
    public void showPlaceLocation(double latitude, double longitude) {
        mLatitude.setText(Double.toString(latitude));
        mLongitude.setText(Double.toString(longitude));
    }


    @Override
    public void showPlaceImage(String path) {
        Glide.with(this).load(path).fitCenter().into(mImage);
    }

    @Override
    public void showPlaceTextError() {
        mText.setError(getString(R.string.error_empty_text));
    }

    @Override
    public void showPlaceDateError() {
        mDate.setError(getString(R.string.error_empty_date));
    }

    @Override
    public void showPlaceLocationError() {
        mLatitude.setError(getString(R.string.error_empty_location));
        mLongitude.setError(getString(R.string.error_empty_location));
    }

    @Override
    public void showCurrentDate() {
        Calendar now = Calendar.getInstance();
        showPlaceDate(now.getTime());
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @OnClick(R.id.image_edit_place)
    public void onImageClick() {
        FishBun.with(this)
                .setCamera(true)
                .setPickerCount(1)
                .startAlbum();
    }

    @OnClick(R.id.btn_last_visited_edit_place)
    public void onDateClick() {
        displayDatePicker();
    }

    @OnClick(R.id.fab_edit_place)
    public void onFabClick() {
        mPlace.text = mText.getText().toString();
        try {
            mPlace.lastVisited = mDateFormat.parse(mDate.getText().toString());
        } catch (ParseException e) {
            Timber.e(e.getMessage());
            showPlaceDateError();
        }
        try {
            mPlace.latitude = Double.parseDouble(mLatitude.getText().toString());
            mPlace.longitude = Double.parseDouble(mLongitude.getText().toString());
        } catch (NumberFormatException e) {
            if (BuildConfig.DEBUG) Timber.e(e.getMessage());
            showPlaceLocationError();
        }
        mPlaceEditPresenter.savePlace(mPlace);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        showPlaceDate(new Date(year, monthOfYear, dayOfMonth));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PLACE_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(this, data);
                    LatLng latLng = place.getLatLng();
                    showPlaceLocation(latLng.latitude, latLng.longitude);
                    break;
                }
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    String path = data.getStringArrayListExtra(Define.INTENT_PATH).get(0);
                    showPlaceImage(path);
                    mPlace.image = path;
                    break;
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_action_map:
                Intent intent = MapActivity.getStartIntent(this, mPlace);
                this.startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
