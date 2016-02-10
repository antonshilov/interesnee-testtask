package gq.vaccum121.testtask.util;

public class MapsUtil {
    public static String getMapUrl(Double lat, Double lon) {
        final String coordPair = lat + "," + lon;
        int dimen = 900;
        return "https://maps.googleapis.com/maps/api/staticmap?"
                + "&zoom=16"
                + "&size=" + dimen + "x" + dimen
                + "&maptype=roadmap&sensor=true"
                + "&center=" + coordPair
                + "&markers=color:red|" + coordPair;
    }
}
