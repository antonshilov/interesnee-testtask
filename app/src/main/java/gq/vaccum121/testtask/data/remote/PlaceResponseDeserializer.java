package gq.vaccum121.testtask.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import gq.vaccum121.testtask.data.model.Place;
import gq.vaccum121.testtask.util.DateUtil;

public class PlaceResponseDeserializer implements JsonDeserializer<List<Place>> {
    @Override
    public List<Place> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray places = json.getAsJsonObject().getAsJsonArray("places");
        Type listType = new TypeToken<List<Place>>() {
        }.getType();
        Gson gson = new GsonBuilder()
                .setDateFormat(DateUtil.DATE_FORMAT)
                .create();
        return gson.fromJson(places, listType);
    }
}
