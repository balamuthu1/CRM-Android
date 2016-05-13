package fr.pds.isintheair.crmtab.helper;

/******************************************
 * Created by        :                    *
 * Creation date     : 05/10/16            *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

import android.content.SharedPreferences;

import fr.pds.isintheair.crmtab.CrmTabApplication;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 01/24/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class SharedPreferenceHelper {
    public static Integer readInteger(String key, Integer value) {
        SharedPreferences sharedPreferences = CrmTabApplication.context.getSharedPreferences("pref", 0);

        return sharedPreferences.getInt(key, value);
    }

    public static void writeInteger(String key, Integer value) {
        SharedPreferences        sharedPreferences = CrmTabApplication.context.getSharedPreferences("pref", 0);
        SharedPreferences.Editor editor            = sharedPreferences.edit();

        editor.putInt(key, value);
        editor.apply();
    }

    public static Long readLong(String key, Integer value) {
        SharedPreferences sharedPreferences = CrmTabApplication.context.getSharedPreferences("pref", 0);

        return sharedPreferences.getLong(key, value);
    }

    public static void writeLong(String key, Long value) {
        SharedPreferences        sharedPreferences = CrmTabApplication.context.getSharedPreferences("pref", 0);
        SharedPreferences.Editor editor            = sharedPreferences.edit();

        editor.putLong(key, value);
        editor.apply();
    }

    public static String readString(String key, String value) {
        SharedPreferences sharedPreferences = CrmTabApplication.context.getSharedPreferences("pref", 0);

        return sharedPreferences.getString(key, value);
    }

    public static void writeString(String key, String value) {
        SharedPreferences        sharedPreferences = CrmTabApplication.context.getSharedPreferences("pref", 0);
        SharedPreferences.Editor editor            = sharedPreferences.edit();

        editor.putString(key, value);
        editor.apply();
    }
}
