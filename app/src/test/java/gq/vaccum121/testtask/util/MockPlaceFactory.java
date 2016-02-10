package gq.vaccum121.testtask.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import gq.vaccum121.testtask.data.model.Place;

public class MockPlaceFactory {
    public static Place newPlace() {
        Place place = new Place();
        place.text = randomString();
        Random random = new Random();
        place.longitude = random.nextDouble();
        place.latitude = random.nextDouble();
        place.lastVisited = new Date();
        place.image = randomString();
        return place;
    }

    public static List<Place> newListOfPlaces(int size) {
        List<Place> places = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            places.add(newPlace());
        }
        return places;
    }

    public static String randomString() {
        return UUID.randomUUID().toString();
    }
}
