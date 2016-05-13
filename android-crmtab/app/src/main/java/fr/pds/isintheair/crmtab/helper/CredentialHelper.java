package fr.pds.isintheair.crmtab.helper;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

/******************************************
 * Created by        :                    *
 * Creation date     : 03/20/16            *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class CredentialHelper {
    public static String getBase64Credentials(String login, String password) {
        String credentials = login + ":" + password;
        byte[] data        = credentials.getBytes(StandardCharsets.UTF_8);

        return Base64.encodeToString(data, Base64.NO_WRAP);
    }

}
