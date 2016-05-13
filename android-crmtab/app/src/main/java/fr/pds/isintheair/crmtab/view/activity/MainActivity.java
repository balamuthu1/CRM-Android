package fr.pds.isintheair.crmtab.view.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
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
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.controller.bus.BusHandlerSingleton;
import fr.pds.isintheair.crmtab.controller.message.ClockinController;
import fr.pds.isintheair.crmtab.controller.message.CrvController;
import fr.pds.isintheair.crmtab.controller.service.CalendarService;
import fr.pds.isintheair.crmtab.controller.service.CallService;
import fr.pds.isintheair.crmtab.controller.service.ListennerCallEndedEvent;
import fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.notification.service.NotificationIntentService;
import fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.view.activity.ProspectActivity;
import fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.view.activity.SynchronisationActivity;
import fr.pds.isintheair.crmtab.jbide.uc.registercall.Events.DisplayAddLogFragmentEvent;
import fr.pds.isintheair.crmtab.jbide.uc.registercall.Events.DisplayPopUpFragmentEvent;
import fr.pds.isintheair.crmtab.jbide.uc.registercall.Rest.Model.Cra;
import fr.pds.isintheair.crmtab.jbide.uc.registercall.Views.callsnotregistered.PendingLogsFragment;
import fr.pds.isintheair.crmtab.jbide.uc.registercall.Views.displaycalls.CallDetailsFragment;
import fr.pds.isintheair.crmtab.jbide.uc.registercall.Views.displaycalls.DisplayCallLogFragment;
import fr.pds.isintheair.crmtab.jbide.uc.registercall.Views.registeracall.AddLogFragment;
import fr.pds.isintheair.crmtab.jbide.uc.registercall.Views.registeracall.PopUpFragment;
import fr.pds.isintheair.crmtab.jbide.uc.registercall.enums.CallType;
import fr.pds.isintheair.crmtab.mmefire.uc.sms.send.receive.activity.ActivityHome;
import fr.pds.isintheair.crmtab.model.dao.UserDAO;
import fr.pds.isintheair.crmtab.model.entity.User;
import fr.pds.isintheair.crmtab.view.fragment.ContactListFragment;
import fr.pds.isintheair.crmtab.view.fragment.CreateCustomerAlertDialog;
import fr.pds.isintheair.crmtab.view.fragment.CreateHCFragment;
import fr.pds.isintheair.crmtab.view.fragment.CreateIndepFragment;
import fr.pds.isintheair.crmtab.view.fragment.CreatePhoningCampaignFragment;
import fr.pds.isintheair.crmtab.view.fragment.ListCustomerFragment;
import fr.pds.isintheair.crmtab.view.fragment.MainLogoFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DisplayCallLogFragment.OnListFragmentInteractionListener,
        PendingLogsFragment.OnListFragmentInteractionListener, CreateCustomerAlertDialog.AlertPositiveListener,
        ListCustomerFragment.OnListFragmentInteractionListener {

    public static String TagAddLogFragment = "FRAGMENT_AJOUT";
    Toolbar toolbar;
    User    currentUser;
    // UC Register a call
    private PendingLogsFragment pend;
    private TextView            mTextView;
    private NfcAdapter          mNfcAdapter;
    private PendingIntent       mPendingIntent;
    private IntentFilter[]      mIntentFilters;
    private String[][]          mNFCTechLists;
    private ImageView           imgStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.NFC) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.NFC);
            }
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_CONTACTS);
            }
            if (checkSelfPermission(Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_CONTACTS);
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
            }
        }
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Crm Tab");

        BusHandlerSingleton.getInstance().getBus().register(this);

        currentUser = new User();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        MainLogoFragment    mainLogoFragment = new MainLogoFragment();
        FragmentTransaction transaction      = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, mainLogoFragment, "TAG");
        transaction.addToBackStack(null);
        transaction.commit();

        if (getIntent().hasExtra("msg"))
            showNotificationListFrag();

        //Badging
        // mTextView = (TextView)findViewById(R.id.tag);
        imgStatus = (ImageView) findViewById(R.id.imgStatus);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter != null) {
            // mTextView.setText("Read an NFC tag");
        }
        else {
            //mTextView.setText("This phone is not NFC enabled.");
        }

        // create an intent with tag data and deliver to this activity
        mPendingIntent = PendingIntent.getActivity(this, 0,
                                                   new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

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
    }

    //Badging
    @Override
    public void onNewIntent(Intent intent) {
        tagdDiscovered(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }


    /**
     * @Override public void onBackPressed() {
     * DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
     * if (drawer.isDrawerOpen(GravityCompat.START)) {
     * drawer.closeDrawer(GravityCompat.START);
     * } else {
     * super.onBackPressed();
     * }
     * }
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.deco) {

            UserDAO.getCurrentUser().delete();
            stopService(new Intent(this, CallService.class));
            stopService(new Intent(this, CalendarService.class));
            stopService(new Intent(this, NotificationIntentService.class));
            stopService(new Intent(this, ListennerCallEndedEvent.class));
            startActivity(new Intent(this, LoginActivity.class));
        }

        if (id == R.id.import_contact) {
            startActivity(new Intent(this, ImportContactActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.nav_passer_appel) {
            fragment = new ContactListFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment).addToBackStack(null);
            transaction.commit();

        }

        else if (id == R.id.nav_lister_appel_non_enreg) {
            showNotificationListFrag();
        }

        else if (id == R.id.nav_lister_appel) {
            fragment = DisplayCallLogFragment.newInstance();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim);
            ft.replace(R.id.container, fragment, "FRAGMENT_LISTE_CRA").addToBackStack(null).commit();
        }
        else if (id == R.id.nav_editer_crv) {
            new CrvController().getClientsForUser(currentUser.getId(), this);
           /* ClientListFragment clientListFragment = new ClientListFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.container, clientListFragment);
            transaction.addToBackStack(null);
            transaction.commit();*/

        }

        else if (id == R.id.nav_ref_client) {
            // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
            // setSupportActionBar(toolbar);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, new ListCustomerFragment());
            fragmentTransaction.addToBackStack("menu");
            fragmentTransaction.commit();

        }
        else if (id == R.id.nav_envoyer_sms) {

            startActivity(new Intent(this, ActivityHome.class));

        }
        else if (id == R.id.nav_suggestion_prospect) {
            startActivity(new Intent(this, ProspectActivity.class));

        }
        else if (id == R.id.nav_synchronisation) {
            startActivity(new Intent(this, SynchronisationActivity.class));

        }

        else if (id == R.id.nav_normalize) {
            startActivity(new Intent(this, NormalizeDemoActivity.class));

        }

        else if (id == R.id.nav_phoning_campaign) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, new CreatePhoningCampaignFragment());
            fragmentTransaction.addToBackStack("createPhoning");
            fragmentTransaction.commit();
        }

        else if (id == R.id.agenda) {
            startActivity(new Intent(this, AgendaActivity.class));
        }
        else if (id == R.id.nav_event_subscription) {
            startActivity(new Intent(this, EventSubscriptionActivity.class));
        }
        else if (id == R.id.nav_admin) {
            startActivity(new Intent(this, AdminActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void showNotificationListFrag() {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(10000);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        pend = PendingLogsFragment.newInstance();
        ft.replace(R.id.container, pend, "FRAGMENT_LISTE_NOTIF").commit();
    }


    @Subscribe
    public void showpopup(DisplayPopUpFragmentEvent event) {
        PopUpFragment pop = PopUpFragment.newInstance(event);
        pop.show(getFragmentManager(), "dialog");
        //make popup not cancellable
        pop.setCancelable(false);
    }

    @Subscribe
    public void showaddlogfragment(DisplayAddLogFragmentEvent event) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim);

        AddLogFragment fragment = AddLogFragment.newInstance(event.getCallEndedEvent().getIdcontact()
                , event.getCallEndedEvent().getDate()
                , event.getCallEndedEvent().getDuration()
                , event.getCallEndedEvent().getCalltype() == CallType.INCOMING ? "Entrant" : "Sortant"
                , true, event.pend, event.getCallEndedEvent().getId());

        ft.replace(R.id.container, fragment, TagAddLogFragment).addToBackStack(null).commit();
    }


    @Override
    public void onListFragmentInteraction(Cra cra) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.enter_anim, R.animator.exit_anim);
        CallDetailsFragment fragment = CallDetailsFragment.newInstance(cra);
        ft.replace(R.id.container, fragment).addToBackStack(null).commit();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }


    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        }

    }

    @Override
    public void onPositiveClick(int position) {
        switch (position) {
            case 0:
                CreateHCFragment createHCFragment = new CreateHCFragment();
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
                toolbar.setTitle(R.string.create_he_fragment_title_action_bar);
                getFragmentManager().beginTransaction()
                                    .replace(R.id.create_customer_fragment_container, createHCFragment).commit();
                break;
            case 1:

                CreateIndepFragment createIndepFragment = new CreateIndepFragment();
                Toolbar toolbarindep = (Toolbar) findViewById(R.id.toolbar1);
                toolbarindep.setTitle(R.string.create_indep_fragment_title_action_bar);
                getFragmentManager().beginTransaction()
                                    .replace(R.id.create_customer_fragment_container, createIndepFragment)
                                    .commit();
                break;
        }
    }

    private void tagdDiscovered(Intent intent) {
        String action = intent.getAction();
        Tag    tag    = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

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

                            byte[] payload      = recs[j].getPayload();
                            String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
                            int    langCodeLen  = payload[0] & 0077;

                            s = (new String(payload, langCodeLen + 1,
                                            payload.length - langCodeLen - 1, textEncoding));

                            ClockinController.clockin(s, this);

                        }
                    }
                }
            }
            catch (Exception e) {
                Log.e("TagDispatch", e.toString());
            }

        }


    }

}

