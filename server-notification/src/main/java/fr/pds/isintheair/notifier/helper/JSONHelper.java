package fr.pds.isintheair.notifier.helper;

import com.google.gson.Gson;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class JSONHelper {
    @SuppressWarnings("unchecked")
    public static String serialize (Object object, Class clazz) {
        Gson gson = new Gson();

        return gson.toJson(object, clazz);
    }

    @SuppressWarnings("unchecked")
    public static Object deserialize (String json, Class clazz) {
        Gson gson = new Gson();

        return gson.fromJson(json, clazz);
    }
}
