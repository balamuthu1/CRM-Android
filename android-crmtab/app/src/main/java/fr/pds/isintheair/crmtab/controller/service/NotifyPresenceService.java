package fr.pds.isintheair.crmtab.controller.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.Arrays;

import fr.pds.isintheair.crmtab.model.ClockinObject;
import fr.pds.isintheair.crmtab.model.dao.UserDAO;
import fr.pds.isintheair.crmtab.model.rest.RetrofitHandlerSingleton;
import fr.pds.isintheair.crmtab.model.rest.service.NotifyPresenceRetrofitService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by jbide on 25/03/2016.
 */
public class NotifyPresenceService extends Service implements Callback<ClockinObject> {


    private TextView       mTextView;
    private NfcAdapter     mNfcAdapter;
    private PendingIntent  mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][]     mNFCTechLists;
    private ImageView      imgStatus;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //registering service for events on the bus
//        BusHandlerSingleton.getInstance().getBus().register(this);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter != null) {
            // mTextView.setText("Read an NFC tag");
        }
        else {
            //mTextView.setText("This phone is not NFC enabled.");
        }


        // set an intent filter for all MIME data
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefIntent.addDataType("*/*");
            mIntentFilters = new IntentFilter[]{ndefIntent};
        }
        catch (Exception e) {
            Log.e("TagDispatch", e.toString());
        }

        mNFCTechLists = new String[][]{new String[]{NfcF.class.getName()}};

        return START_STICKY;
    }


    @Subscribe
    public void onNewIntent(Intent intent) {
        String action = intent.getAction();
        Tag    tag    = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        String s = "";

        // parse through all NDEF messages and their records and pick text type only
        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        if (data != null) {
            try {
                for (int i = 0; i < data.length; i++) {
                    NdefRecord[] recs = ((NdefMessage) data[i]).getRecords();
                    for (int j = 0; j < recs.length; j++) {
                        if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                                Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)) {

                            byte[] payload      = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                            int    langCodeLen  = payload[0] & 0077;

                            //Tag id
                            s = (new String(payload, langCodeLen + 1,
                                            payload.length - langCodeLen - 1, textEncoding));


                            clockin(s);

                        }
                    }
                }
            }
            catch (Exception e) {
                Log.e("TagDispatch", e.toString());
            }

        }

        // mTextView.setText(s);
    }


    private void clockin(String tagid) {

        NotifyPresenceRetrofitService service = RetrofitHandlerSingleton.getInstance().getNotifyPresenceService();
        Call<ClockinObject>           call    = service.clockin(new ClockinObject(UserDAO.getCurrentUser(), tagid));
        call.enqueue(this);

    }

    @Override
    public void onResponse(Response<ClockinObject> response, Retrofit retrofit) {

    }

    @Override
    public void onFailure(Throwable t) {

    }


    @Override
    public void onDestroy() {

        // Tell the user we stopped.
        Toast.makeText(this, "NotifyPresenceServiceService stopped", Toast.LENGTH_SHORT).show();
    }


    public class NotifyServiceBinder extends Binder {
        NotifyPresenceService getService() {
            return NotifyPresenceService.this;
        }
    }

}

