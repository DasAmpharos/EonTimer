package com.github.dylmeadows.common.javafx.beans;

import com.github.dylmeadows.common.gson.DurationTypeAdapter;
import com.google.gson.Gson;
import org.hildan.fxgson.FxGson;

import java.time.Duration;

public class ModelSerializer {

    private static final Gson serializer = getSerializer();

    private static Gson getSerializer() {
        return FxGson.fullBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .setPrettyPrinting()
                .create();
    }

    public static String toJson(Object o) {
        return serializer.toJson(o);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return serializer.fromJson(json, clazz);
    }

}
