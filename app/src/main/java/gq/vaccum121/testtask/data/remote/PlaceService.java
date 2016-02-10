package gq.vaccum121.testtask.data.remote;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import gq.vaccum121.testtask.BuildConfig;
import gq.vaccum121.testtask.data.model.Place;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import retrofit2.http.GET;
import rx.Observable;

public interface PlaceService {
    String ENDPOINT = "http://interesnee.ru/files/";

    @GET("android-middle-level-data.json")
    Observable<List<Place>> getPlaces();


    /**
     * Factory class that creates a new place service.
     */
    class Factory {
        public static PlaceService makePlaceService(Context context) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY
                    : HttpLoggingInterceptor.Level.NONE);
            OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(logging).build();

            Type listType = new TypeToken<List<Place>>() {
            }.getType();

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(listType, new PlaceResponseDeserializer())
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ENDPOINT)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            return retrofit.create(PlaceService.class);
        }

    }
}
