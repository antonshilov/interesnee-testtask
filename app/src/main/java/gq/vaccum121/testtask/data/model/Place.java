package gq.vaccum121.testtask.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import gq.vaccum121.testtask.util.DateUtil;

/**
 * The POJO of Place(location with photo).
 */
public class Place implements Parcelable {

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };
    public Double latitude;
    /**
     * Workaround for typo on the server side.
     */
    @SerializedName("longtitude")
    public Double longitude;
    public String text;
    public String image;
    public Date lastVisited;

    public Place() {
    }

    protected Place(Parcel in) {
        this.text = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.lastVisited = DateUtil.parse(in.readString());
        this.image = in.readString();
    }

    public Place(Double latitude, Double longitude, String text, String image, Date lastVisited) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.text = text;
        this.image = image;
        this.lastVisited = lastVisited;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Place)) return false;

        Place place = (Place) o;

        if (latitude != null ? !latitude.equals(place.latitude) : place.latitude != null)
            return false;
        if (longitude != null ? !longitude.equals(place.longitude) : place.longitude != null)
            return false;
        if (text != null ? !text.equals(place.text) : place.text != null) return false;
        if (image != null ? !image.equals(place.image) : place.image != null) return false;
        return !(lastVisited != null ? lastVisited.equals(place.lastVisited) : place.lastVisited == null);

    }

    @Override
    public int hashCode() {
        int result = latitude != null ? latitude.hashCode() : 0;
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (lastVisited != null ? lastVisited.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.text);
        parcel.writeDouble(this.latitude);
        parcel.writeDouble(this.longitude);
        parcel.writeString(DateUtil.toString(this.lastVisited));
        parcel.writeString(this.image);
    }
}