package fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.adapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.squareup.okhttp.ResponseBody;

import fr.pds.isintheair.crmtab.model.rest.service.ProspectRestConfig;
import fr.pds.isintheair.crmtab.model.rest.service.SyncRetrofitAPI;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Truong on 4/24/2016.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";
    // Global variables
    // Define a variable to contain a content resolver instance
    public static final String SYNC_FINISHED = "SyncFinished";
    public static final String SYNC_STARTED = "SyncStarted";
    final ContentResolver mContentResolver;
    Context context;



    /**
     * Set up a Sync Adapter
     *
     * @param context
     * @param autoInitialize
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.context = context;
        mContentResolver = context.getContentResolver();
        Log.i("SyncAdapter", "SyncAdapter");
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     *
     * @param context
     * @param autoInitialize
     * @param allowParallelSyncs
     */
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        String[] result = getData();
        Log.i(TAG, "onPerformSync: " + result[0]);
        Log.i(TAG, "onPerformSync: " + getData());
        if (result[0] == null) {
            Intent i = new Intent(SYNC_STARTED);
            context.sendBroadcast(i);
            Log.i("SyncAdapter", "onPerformSync");
            i = new Intent(SYNC_FINISHED);
            context.sendBroadcast(i);
        }

    }

    /**
     *
     * @return
     */
    public String[] getData() {
        final String[] test = new String[1];
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ProspectRestConfig.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        SyncRetrofitAPI api = retrofit.create(SyncRetrofitAPI.class);
        Call<ResponseBody> result = api.getSyncData();
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                try {
                    test[0] = response.body().string();
                    Log.i(TAG, "onResponse: " + test[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
        return test;
    }
}
