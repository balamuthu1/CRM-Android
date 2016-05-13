package fr.pds.isintheair.phonintheair.helper;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class GoogleAccountHelper {
    public static List<String> getAccountNames(Context context) {
        AccountManager manager        = AccountManager.get(context);
        Account[]      accounts       = manager.getAccountsByType("com.google");
        List<String>   possibleEmails = new ArrayList<>();

        for (Account account : accounts) {
            possibleEmails.add(account.name);
        }

        return possibleEmails;
    }
}
