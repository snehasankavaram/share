package com.example.james.sharedclasses;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by james on 12/3/15.
 */
public class ServerDeserializer<T> implements JsonDeserializer {

    @Override
    public T deserialize(JsonElement arg0, Type arg1,
                         JsonDeserializationContext arg2)
            throws JsonParseException {
        JsonElement content = arg0.getAsJsonObject();
        return new Gson().fromJson(content, arg1);

    }
}
