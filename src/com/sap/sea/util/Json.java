package com.sap.sea.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public class Json {
    private static final Gson gson = new GsonBuilder().create();

    public static String stringify(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T parse(String raw, Class<T> type) {
        return gson.fromJson(raw, type);
    }

    public static JsonElement parse(String raw) {
        return parse(raw, JsonElement.class);
    }
}
