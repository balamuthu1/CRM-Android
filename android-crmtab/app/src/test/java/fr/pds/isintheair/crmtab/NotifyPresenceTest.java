package fr.pds.isintheair.crmtab;


import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import org.junit.Test;

import java.util.Arrays;

import fr.pds.isintheair.crmtab.model.ClockinObject;
import fr.pds.isintheair.crmtab.model.entity.User;
import fr.pds.isintheair.crmtab.model.rest.RetrofitHandlerSingleton;
import fr.pds.isintheair.crmtab.model.rest.service.NotifyPresenceRetrofitService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static junit.framework.Assert.assertTrue;

/**
 * Created by jbide on 11/05/2016.
 */
public class NotifyPresenceTest {

    final int TECH_NFC_A = 1;
    final String EXTRA_NFC_A_SAK = "sak";    // short (SAK byte value)
    final String EXTRA_NFC_A_ATQA = "atqa";  // byte[2] (ATQA value)

    final int TECH_NFC_B = 2;
    final String EXTRA_NFC_B_APPDATA = "appdata";    // byte[] (Application Data bytes from ATQB/SENSB_RES)
    final String EXTRA_NFC_B_PROTINFO = "protinfo";  // byte[] (Protocol Info bytes from ATQB/SENSB_RES)

    final int TECH_ISO_DEP = 3;
    final String EXTRA_ISO_DEP_HI_LAYER_RESP = "hiresp";  // byte[] (null for NfcA)
    final String EXTRA_ISO_DEP_HIST_BYTES = "histbytes";  // byte[] (null for NfcB)

    final int TECH_NFC_F = 4;
    final String EXTRA_NFC_F_SC = "systemcode";  // byte[] (system code)
    final String EXTRA_NFC_F_PMM = "pmm";        // byte[] (manufacturer bytes)

    final int TECH_NFC_V = 5;
    final String EXTRA_NFC_V_RESP_FLAGS = "respflags";  // byte (Response Flag)
    final String EXTRA_NFC_V_DSFID = "dsfid";           // byte (DSF ID)

    final int TECH_NDEF = 6;
    final String EXTRA_NDEF_MSG = "ndefmsg";              // NdefMessage (Parcelable)
    final String EXTRA_NDEF_MAXLENGTH = "ndefmaxlength";  // int (result for getMaxSize())
    final String EXTRA_NDEF_CARDSTATE = "ndefcardstate";  // int (1: read-only, 2: read/write, 3: unknown)
    final String EXTRA_NDEF_TYPE = "ndeftype";            // int (1: T1T, 2: T2T, 3: T3T, 4: T4T, 101: MF Classic, 102: ICODE)

    final int TECH_NDEF_FORMATABLE = 7;

    final int TECH_MIFARE_CLASSIC = 8;

    final int TECH_MIFARE_ULTRALIGHT = 9;
    final String EXTRA_MIFARE_ULTRALIGHT_IS_UL_C = "isulc";  // boolean (true: Ultralight C)

    final int TECH_NFC_BARCODE = 10;
    final String EXTRA_NFC_BARCODE_BARCODE_TYPE = "barcodetype";  // int (1: Kovio/ThinFilm)



    @Test
    public void testTagDiscovered() {
        Intent intent = new Intent();

        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        //String s = action + "\n\n" + tag.toString();
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

                            byte[] payload = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                            int langCodeLen = payload[0] & 0077;

                            s = (new String(payload, langCodeLen + 1,
                                    payload.length - langCodeLen - 1, textEncoding));

                            assertTrue(s == null);


                        }
                    }
                }
            } catch (Exception e) {
                Log.e("TagDispatch", e.toString());
            }

        }
    }

    @Test
    public void testClockin(){
        //Interceptor
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient();
        // add logging as last interceptor
        httpClient.interceptors().add(logging);

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.REST_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build();

        NotifyPresenceRetrofitService service = RetrofitHandlerSingleton.getInstance().getNotifyPresenceService();
        User user = new User();
        user.setId("bd299fa2-244c-4b6b-9966-49a84192cc8c");
        //NotifyPresenceRetrofitService service =  retrofit.create(NotifyPresenceRetrofitService.class);
        Call<ClockinObject> call = service.clockin(new ClockinObject(user, "entree"));

        call.enqueue(new Callback<ClockinObject>() {
            @Override
            public void onResponse(Response<ClockinObject> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    assertTrue(response.isSuccess());
                } else {
                    assertTrue(false);

                }

            }

            @Override
            public void onFailure(Throwable t) {
                assertTrue(false);
            }
        });}

}


