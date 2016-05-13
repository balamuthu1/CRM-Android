package fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.view.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.adapter.SyncAdapter;

public class SynchronisationActivity extends Activity implements View.OnClickListener {

    private String TAG = this.getClass().getSimpleName();
    private AccountManager mAccountManager;
    public static final String DEMO_ACCOUNT_NAME = "Demo Account";
    public static final String DEMO_ACCOUNT_PASSWORD = "Demo123";

    @Bind(R.id.tv1)
    TextView tv1;

    @Bind(R.id.tv2)
    TextView tv2;

    @Bind(R.id.tv3)
    TextView tv3;

    @Bind(R.id.tv4)
    TextView tv4;

    @Bind(R.id.tv5)
    TextView tv5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_synchronisation);
        ButterKnife.bind(this);
        mAccountManager = AccountManager.get(this);

        ((Button) findViewById(R.id.button3)).setOnClickListener(this);
        ((Button) findViewById(R.id.button4)).setOnClickListener(this);

        createDemoAccount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(syncStaredReceiver, new IntentFilter(SyncAdapter.SYNC_STARTED));
        registerReceiver(syncFinishedReceiver, new IntentFilter(SyncAdapter.SYNC_FINISHED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(syncStaredReceiver);
        unregisterReceiver(syncFinishedReceiver);
    }

    private BroadcastReceiver syncFinishedReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Sync finished!");
            Toast.makeText(getApplicationContext(), "Sync Finished", Toast.LENGTH_SHORT).show();
            tv1.setText("ok");
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    tv2.setText("ok");
                    tv3.setText("ok");
                    handler.postDelayed(this, 2000);
                }
            }, 1500);
            final Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    tv4.setText("ok");
                    tv5.setText("ok");
                    handler.postDelayed(this, 5000);
                }
            }, 3000);


        }
    };

    private BroadcastReceiver syncStaredReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Sync started!");
            Toast.makeText(getApplicationContext(), "Sync started...", Toast.LENGTH_SHORT).show();
            tv1.setText("sync ...");
            tv2.setText("sync ...");
            tv3.setText("sync ...");
            tv4.setText("sync ...");
            tv5.setText("sync ...");

        }
    };

    public void startForceSyncing() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        Account account = new Account(DEMO_ACCOUNT_NAME, getString(R.string.auth_type));
        ContentResolver.requestSync(account, getString(R.string.content_authority), bundle);

        ContentResolver.setIsSyncable(account, getString(R.string.content_authority), 1);
        ContentResolver.setSyncAutomatically(account, getString(R.string.content_authority), true);
    }

    public void scheduleSync() {
        Bundle bundle = new Bundle();
        Account account = new Account(DEMO_ACCOUNT_NAME, getString(R.string.auth_type));
        ContentResolver.requestSync(account, getString(R.string.content_authority), bundle);
        ContentResolver.addPeriodicSync(account, getString(R.string.content_authority), bundle, 15 * 60);
    }

    public Account createDemoAccount() {
        Account account = new Account(DEMO_ACCOUNT_NAME, getString(R.string.auth_type));
        boolean accountCreated = mAccountManager.addAccountExplicitly(account, DEMO_ACCOUNT_PASSWORD, null);
        if (accountCreated) {
            showMessage("Account Created");
        }
        return account;
    }

    private void showMessage(final String msg) {
        if (TextUtils.isEmpty(msg))
            return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button3) {
            startForceSyncing();
        }
        if (v.getId() == R.id.button4) {
            scheduleSync();
        }
    }
}
