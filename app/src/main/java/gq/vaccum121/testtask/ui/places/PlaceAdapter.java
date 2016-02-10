package gq.vaccum121.testtask.ui.places;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import gq.vaccum121.testtask.R;
import gq.vaccum121.testtask.data.model.Place;
import gq.vaccum121.testtask.ui.place_edit.PlaceEditActivity;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceHolder> {
    private List<Place> mPlaces;
    private DateFormat mDateFomat;
    private Context mContext;

    @Inject
    public PlaceAdapter() {
        this.mPlaces = new ArrayList<>();
    }

    @Override
    public PlaceHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_place, parent, false);
        mDateFomat = android.text.format.DateFormat.getDateFormat(parent.getContext());
        return new PlaceHolder(view);
    }

    @Override
    public void onBindViewHolder(final PlaceHolder holder, final int position) {
        Place place = mPlaces.get(position);
        holder.text.setText(place.text);
        String date = mDateFomat.format(place.lastVisited);
        holder.date.setText(date);
        Glide.with(holder.cardView.getContext())
                .load(place.image)
                .crossFade()
                .into(holder.circleImage);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = PlaceEditActivity.getStartIntent(mContext, mPlaces.get(holder.getAdapterPosition()));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }

    public void setPlaces(List<Place> list) {
        mPlaces = list;
    }

    class PlaceHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.card_view)
        CardView cardView;

        @Bind(R.id.circle_image_place)
        CircleImageView circleImage;

        @Bind(R.id.text_place_text)
        TextView text;

        @Bind(R.id.text_place_date)
        TextView date;

        public PlaceHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
