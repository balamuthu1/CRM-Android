package fr.pds.isintheair.crmtab.helper;

import android.support.v4.content.ContextCompat;

import fr.pds.isintheair.crmtab.CrmTabApplication;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 01/23/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class ResourceHelper {
    public static String getString(int stringId) {
        return CrmTabApplication.context.getResources().getString(stringId);
    }

    public static Integer getColor(int colorId) {
        return ContextCompat.getColor(CrmTabApplication.context, colorId);
    }
}
