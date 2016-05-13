package fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.service;

import android.accounts.Account;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.account.AccountGeneral;
import fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.account.Authenticator;

public class AuthenticatorService extends Service {
    private static final String TAG = "GenericAccountService";
    private Authenticator authenticator;
    public AuthenticatorService() {
        authenticator = new Authenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return authenticator.getIBinder();
    }

    public static Account getAccount(){
        final String accountName = AccountGeneral.ACCOUNT_NAME;
        return new Account(accountName, AccountGeneral.ACCOUNT_TYPE);
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Service created");
        authenticator = new Authenticator(this);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service destroyed");
    }
}
