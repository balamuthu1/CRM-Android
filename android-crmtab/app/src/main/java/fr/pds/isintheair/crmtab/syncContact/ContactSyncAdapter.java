package fr.pds.isintheair.crmtab.syncContact;

import android.accounts.Account;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.adapter.SyncAdapter;

/**
 * Created by jbide on 10/05/2016.
 */
public class ContactSyncAdapter extends SyncAdapter {
    public ContactSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        super.onPerformSync(account, extras, authority, provider, syncResult);
        //TODO
    }
}
