package fr.pds.isintheair.phonintheair.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcF;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.pds.isintheair.phonintheair.R;
import fr.pds.isintheair.phonintheair.controller.bus.handler.BusHandlerSingleton;
import fr.pds.isintheair.phonintheair.helper.SharedPreferencesHelper;
import fr.pds.isintheair.phonintheair.model.entity.Agenda;
import fr.pds.isintheair.phonintheair.model.provider.CalendarProvider;
import fr.pds.isintheair.phonintheair.notifyPresence.ClockinController;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/21/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class DashboardActivity extends Activity {
    @Bind(R.id.call_connection_state)
    ImageView callConnectionState;

    @Bind(R.id.call_autorisation_state)
    ImageView callAutorisationState;

    @Bind(R.id.calendar_connection_state)
    ImageView calendarConnectionState;

    @Bind(R.id.calendar_autorisation_state)
    ImageView calendarAutorisationState;

    @Bind(R.id.calendar_chosen_textview)
    TextView calendarChosenTextview;


    @OnClick(R.id.calendar_choice_button)
    public void onCalendarChoiceButton() {
        CalendarProvider    calendarProvider = new CalendarProvider(this);
        final List<Agenda>  agendas          = calendarProvider.getAgendas("");
        AlertDialog.Builder builder          = new AlertDialog.Builder(this);

        List<String> agendaNames = new ArrayList<>();

        for (Agenda agenda : agendas) {
            agendaNames.add(agenda.getName());
        }

        builder.setTitle("Veuillez choisir un agenda")
               .setItems(agendaNames.toArray(new CharSequence[agendaNames.size()]), new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
                       Agenda agendaChosen = agendas.get(which);

                       SharedPreferencesHelper.writeLong("agendaId", agendaChosen.getId());

                       calendarChosenTextview.setText(agendaChosen.getName());
                   }
               }).create().show();
    }

    private PendingIntent mPendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mNFCTechLists;
    private ImageView imgStatus;
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ButterKnife.bind(this);
        BusHandlerSingleton.getInstance().getBus().register(this);

        List<String> permissions = new ArrayList<>();

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CALL_PHONE);
            }

            if (checkSelfPermission(Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_CALENDAR);
            }

            if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_CALENDAR);
            }

            if (checkSelfPermission(Manifest.permission.NFC) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.NFC);
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
            }

        }

        // Badging us
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // set an intent filter for all MIME data
        IntentFilter ndefIntent = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefIntent.addDataType("*/*");
            mIntentFilters = new IntentFilter[] { ndefIntent };
        } catch (Exception e) {
            Log.e("TagDispatch", e.toString());
        }

        mNFCTechLists = new String[][] { new String[] { NfcF.class.getName() } };

    }

    //Badging
    @Override
    public void onNewIntent(Intent intent) {
        tagdDiscovered(intent);
    }


    @Override
    public void onPause() {
        super.onPause();

        //Badging us
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Long agendaId = SharedPreferencesHelper.readLong("agendaId", 42);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                calendarAutorisationState.setImageResource(R.drawable.red_circle);
            }

            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                callAutorisationState.setImageResource(R.drawable.red_circle);
            }
        }

        if (agendaId != 42) {
            CalendarProvider calendarProvider = new CalendarProvider(this);
            Agenda agenda = calendarProvider.getAgendaById(agendaId);

            calendarChosenTextview.setText(agenda.getName());
        }

        //Badging us
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callAutorisationState.setImageResource(R.drawable.green_circle);
            }

            if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                calendarAutorisationState.setImageResource(R.drawable.green_circle);
            }
        }
    }

    /* @Subscribe
    public void onCalendarWebSocketConnectionLost(CalendarWebSocketConnectionLostEvent event) {
        calendarConnectionState.setImageResource(R.drawable.red_circle);
    }

    @Subscribe
    public void onCalendarWebSocketConnectionRetrevied(CalendarWebSocketConnectionRetrievedEvent event) {
        calendarConnectionState.setImageResource(R.drawable.green_circle);
    }

    @Subscribe
    public void onCallWebSocketConnectionLost(CallWebSocketConnectionLostEvent event) {
        callConnectionState.setImageResource(R.drawable.red_circle);
    }

    @Subscribe
    public void onCallWebSocketConnectionRetrevied(CallWebSocketConnectionRetrievedEvent event) {
        callConnectionState.setImageResource(R.drawable.green_circle);
    } */

    private void tagdDiscovered(Intent intent) {
        String action = intent.getAction();
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        //String s = action + "\n\n" + tag.toString();
        String s="";

        // parse through all NDEF messages and their records and pick text type only
        Parcelable[] data = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        if (data != null) {
            try {
                for (int i = 0; i < data.length; i++) {
                    NdefRecord[] recs = ((NdefMessage)data[i]).getRecords();
                    for (int j = 0; j < recs.length; j++) {
                        if (recs[j].getTnf() == NdefRecord.TNF_WELL_KNOWN &&
                                Arrays.equals(recs[j].getType(), NdefRecord.RTD_TEXT)) {

                            byte[] payload = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                            int langCodeLen = payload[0] & 0077;

                            s = ( new String(payload, langCodeLen + 1,
                                    payload.length - langCodeLen - 1, textEncoding));

                            ClockinController.clockin(s, this);

                        }
                    }
                }
            } catch (Exception e) {
                Log.e("TagDispatch", e.toString());
            }

        }


    }
}
