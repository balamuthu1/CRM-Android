package fr.pds.isintheair.crmtab.view.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.controller.bus.BusHandlerSingleton;
import fr.pds.isintheair.crmtab.controller.bus.event.PhoneCallEndedEvent;
import fr.pds.isintheair.crmtab.controller.bus.event.PhoneCallFailedEvent;
import fr.pds.isintheair.crmtab.controller.bus.event.PhoneCallHookedEvent;
import fr.pds.isintheair.crmtab.controller.message.CallMessageController;
import fr.pds.isintheair.crmtab.controller.message.PhoningCampaignController;
import fr.pds.isintheair.crmtab.jbide.uc.registercall.database.entity.CallEndedEvent;
import fr.pds.isintheair.crmtab.jbide.uc.registercall.enums.CallType;
import fr.pds.isintheair.crmtab.model.entity.Contact;
import fr.pds.isintheair.crmtab.model.entity.ContactCampaign;
import fr.pds.isintheair.crmtab.model.entity.Customer;
import fr.pds.isintheair.crmtab.model.entity.PhoningCampaign;
import fr.pds.isintheair.crmtab.view.activity.MainActivity;

/**
 * Created by tlacouque on 03/04/2016.
 * Fragment used to do all the call of a phoning campaign
 */
public class CallPhoningCampaignFragment extends Fragment implements StopCampaignAlertDialog.AlertPositiveListener{

    public static String KEY_PHONING_CAMPAIGN = "PHONING_CAMPAIGN";
    public static String KEY_CUSTOMER_LIST_CONTACT = "CUSTOMER_LIST_CONTACT";


    Customer customer;
    Contact contact;
    ContactCampaign contactCampaign;
    PhoningCampaign phoningCampaign;
    boolean callBegin;
    PhoningCampaignController controller;


    @Bind(R.id.create_phoning_campaign_fragment_phone)
    ImageView callButton;

    @Bind(R.id.call_phoning_campaign_fragment_title)
    TextView titre;

    @Bind(R.id.call_phoning_campaign_fragment_objective)
    TextView objective;

    @Bind(R.id.call_phoning_campaign_fragment_contact_name_firstname)
    TextView contactNameFname;

    @Bind(R.id.call_phoning_campaign_fragment_campaign_type)
    TextView type;

    @Bind(R.id.call_phoning_campaign_fragment_contact_job)
    TextView  contactJob;

    @Bind(R.id.call_phoning_campaign_fragment_customer_name)
    TextView customerName;

    @Bind(R.id.call_phoning_campaign_fragment_commentary)
    EditText commentary;

    @Bind(R.id.call_phoning_campaign_fragment_next_call)
    Button nextCallButton;


    /**
     * Can be called when a new CallPhoningCampaignFragment is needed
     *
     * @return CallPhoningCampaignFragment
     */
    public static CallPhoningCampaignFragment newInstance() {
        CallPhoningCampaignFragment fragment = new CallPhoningCampaignFragment();
        Bundle args     = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HashMap<Customer,List<Contact>> customerListHashMap =
                (HashMap<Customer, List<Contact>>)
                        this.getArguments().getSerializable(CallPhoningCampaignFragment.KEY_CUSTOMER_LIST_CONTACT);
        phoningCampaign = this.getArguments().getParcelable(CallPhoningCampaignFragment.KEY_PHONING_CAMPAIGN);
        callBegin = false;
        BusHandlerSingleton.getInstance().getBus().register(this);
        controller = new PhoningCampaignController(customerListHashMap,phoningCampaign,this);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_call_phoning_campaign, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!callBegin)
        controller.beginCampaign();
    }

    /**
     * Initialise the view of a Call. It is called before every call.
     * @param phoningCampaign
     * @param contact
     * @param customer
     */
    public void initView(PhoningCampaign phoningCampaign, Contact contact, Customer customer,ContactCampaign contactCampaign) {
        this.phoningCampaign = phoningCampaign;
        this.contact = contact;
        this.customer = customer;
        titre.setText(this.phoningCampaign.getCampaignTheme());
         objective.setText(this.phoningCampaign.getCampaignObjectives());
         contactNameFname.setText(this.contact.contactName+" "+this.contact.contactFname);
         type.setText(this.phoningCampaign.getCampaignType());
          contactJob.setText(this.contact.contactStatus);
         customerName.setText(this.customer.getName());

        if(contactCampaign.getContactInfo() == null) {
            commentary.setText("");
        } else {
            commentary.setText(""+contactCampaign.getContactInfo());
        }



    }

    /**
     * Method used to start a call
     */
    public void startCall() {
        callButton.setImageResource(R.drawable.phone_logo_red);
        CallMessageController.sendCallMessage(contact.contactTel);
        callBegin = true;
    }


    @Subscribe
    public void onPhoneCallEndedEvent(PhoneCallEndedEvent phoneCallEndedEvent) {
        Log.d("callFragment", "callEnded");
        callButton.setImageResource(R.drawable.phone_logo_green);
        callBegin = false;
    }

    @Subscribe
    public void onPhoneCallFailedEvent(PhoneCallFailedEvent phoneCallFailedEvent) {
        Log.d("callFragment", "failed");
    }

    @Subscribe
    public void onCallHooked(PhoneCallHookedEvent phoneCallHookedEvent) {
        Log.d("callFragment","Hoocked");
}

    /**
     * Called when the user click on the phone. If the call is started, it end it, or it start it.
     * @param view
     */
    @OnClick(R.id.create_phoning_campaign_fragment_phone)
    public void onPhoneClick(final View view) {
        if(callBegin) {
            CallMessageController.sendEndCallMessage();
        } else {
            startCall();
        }
    }

    /**
     * Method called when a user click on "Appeler plus tard" button. It call reset call from the controller.
     * @param view
     */
    @OnClick(R.id.call_phoning_campaign_fragment_reset_call)
    public void resetCall(final View view) {
        if(!callBegin) {
            controller.saveCurrentContactInfo(commentary.getText().toString(), ContactCampaign.STATE_DEFINED);
            controller.resetCall();
        } else {
            snackbarActionDuringCall();
        }
    }

    /**
     * Method called to pass to the next call.
     * @param view
     */
    @OnClick(R.id.call_phoning_campaign_fragment_next_call)
    public void nextCall(final View view) {
        if(!callBegin) {
            if (!commentary.getText().toString().isEmpty()) {
                controller.saveCurrentContactInfo(commentary.getText().toString(), ContactCampaign.STATE_ENDED);
            } else {
                controller.saveCurrentContactInfo("", ContactCampaign.STATE_ENDED);
            }
            controller.endCall();
        } else {
            snackbarActionDuringCall();
        }
    }

    /**
     * Method called to pause a campaign
     * @param view
     */
    @OnClick(R.id.call_phoning_campaign_fragment_pause_campaign)
    public void pauseCampaign(final View view) {
        if(!callBegin) {
            StopCampaignAlertDialog alert = new StopCampaignAlertDialog();
            alert.setAlertPositiveListener(this);
            FragmentManager manager = getFragmentManager();
            /** Creating the dialog fragment object, which will in turn open the alert dialog window */
            alert.show(manager, "StopCampaignAlertDialog");

        } else {
            snackbarActionDuringCall();
        }
    }

    /**
     * Method called by the controller when it s the end of the campaign.
     * It start the home activity.
     * @param bool
     */
    public void endCampaign(boolean bool) {
        Snackbar snackbar;
        if(bool) {
             snackbar = Snackbar.make(this.getView(), R.string.call_phoning_campaign_fragment_end_campaign,
                    Snackbar.LENGTH_LONG);
        } else {
             snackbar = Snackbar.make(this.getView(), R.string.call_phoning_campaign_fragment_end_campaign_error,
                    Snackbar.LENGTH_LONG);
        }

        ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setMaxLines(2);
        snackbar.show();
        this.getFragmentManager().popBackStack("createPhoning", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * Called by the controller when the user wants to stop the campaign.
     * It force the user to return on the home activity
     */
    public void stopCampaign() {
        this.getFragmentManager().popBackStack("createPhoning", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * Called when the user try to press on any button when a call is started. It show a snackbar.
     */
    private void snackbarActionDuringCall() {
        Snackbar snackbar = Snackbar.make(this.getView(), R.string.call_phoning_campaign_fragment_during_call,
                Snackbar.LENGTH_LONG);
        ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setMaxLines(2);
        snackbar.show();
    }

    /**
     * Called when a user want to pause a campaign.
     * It called the controller to pause the campaign
     */
    @Override
    public void onPositiveClick() {
        if (!commentary.getText().toString().isEmpty()) {
            controller.saveCurrentContactInfo(commentary.getText().toString(), ContactCampaign.STATE_ENDED);
        } else {
            controller.saveCurrentContactInfo("", ContactCampaign.STATE_ENDED);
        }
        controller.pauseCampaign();
    }
}
