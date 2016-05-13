package fr.pds.isintheair.crmtab.syncContact;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ContactAuthenticatorService extends Service {

    private ContactAuthenticator mAuthenticator;

    public ContactAuthenticatorService() {
    }

    @Override
    public void onCreate() {
        mAuthenticator = new ContactAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
