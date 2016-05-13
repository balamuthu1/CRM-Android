package fr.pds.isintheair.phonintheair.helper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import fr.pds.isintheair.phonintheair.PhonintheairApp;

public class CallHelper {
    public static void call(String phoneNumber) {
        Intent  callIntent         = new Intent(Intent.ACTION_CALL);
        Context applicationContext = PhonintheairApp.context;

        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            if (applicationContext.checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                applicationContext.startActivity(callIntent);
            }
        }

        else {
            applicationContext.startActivity(callIntent);
        }
    }

    public static void endCall() {
        TelephonyManager tm = (TelephonyManager) PhonintheairApp.context.getSystemService(Context.TELEPHONY_SERVICE);

        try {
            Class c = Class.forName(tm.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");

            m.setAccessible(true);

            Object telephonyService = m.invoke(tm);

            c = Class.forName(telephonyService.getClass().getName());
            m = c.getDeclaredMethod("endCall");

            m.setAccessible(true);
            m.invoke(telephonyService);
        }
        catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            //TODO handle exception
        }
    }
}
