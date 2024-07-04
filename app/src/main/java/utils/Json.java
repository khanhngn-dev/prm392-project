package utils;

import com.google.gson.Gson;

public class Json {
    private static final Gson gson = new Gson();

    public static String stringify(Object object) {
        return gson.toJson(object);
    }

    public static <T> T parse(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}
