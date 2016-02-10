package gq.vaccum121.testtask.ui.places;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gq.vaccum121.testtask.R;
import gq.vaccum121.testtask.data.model.Place;
import gq.vaccum121.testtask.ui.main.MainActivity;
import gq.vaccum121.testtask.ui.place_edit.PlaceEditActivity;

public class PlacesFragment extends Fragment implements PlacesMvpView {
    @Inject
    PlacesPresenter mPlacesPresenter;
    @Inject
    PlaceAdapter mPlaceAdapter;

    @Bind(R.id.recycler_view_places)
    RecyclerView mPlacesRecycler;

    @Bind(R.id.text_no_places)
    TextView mNoPlacesText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_places, container, false);
        ButterKnife.bind(this, fragmentView);
        mPlacesPresenter.attachView(this);
        mPlacesRecycler.setHasFixedSize(true);
        mPlacesRecycler.setAdapter(mPlaceAdapter);
        mPlacesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mPlacesPresenter.loadPlaces();
        return fragmentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((MainActivity) getActivity()).activityComponent().inject(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void showPlaces(List<Place> places) {
        mPlacesRecycler.setVisibility(View.VISIBLE);
        mPlaceAdapter.setPlaces(places);
        mPlaceAdapter.notifyDataSetChanged();
        mNoPlacesText.setVisibility(View.GONE);
    }

    @Override
    public void showEmpty() {
        mPlacesRecycler.setVisibility(View.GONE);
        mNoPlacesText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrMsg() {

    }

    @Override
    public void showProgress() {

    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        Intent intent = PlaceEditActivity.getStartIntent(getContext(), null);
        this.startActivity(intent);
    }
}
