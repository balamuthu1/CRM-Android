package fr.pds.isintheair.crmtab.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.controller.message.PhoningCampaignController;
import fr.pds.isintheair.crmtab.helper.CustomerHelper;
import fr.pds.isintheair.crmtab.model.entity.Contact;
import fr.pds.isintheair.crmtab.model.entity.ContactCampaign;
import fr.pds.isintheair.crmtab.model.entity.Customer;
import fr.pds.isintheair.crmtab.model.entity.PhoningCampaign;
import fr.pds.isintheair.crmtab.view.activity.MainActivity;

/**
 * Created by tlacouque on 28/03/2016.
 */
public class DetailPhoningCampaignFragment extends Fragment {


    @Bind(R.id.detail_phoning_campaign_fragment_title)
    TextView title;

    @Bind(R.id.detail_phoning_campaign_fragment_campaign_type)
    TextView type;

    @Bind(R.id.detail_phoning_campaign_fragment_objective)
    TextView objective;

    @Bind(R.id.detail_phoning_campaign_fragment_list_customer_contact)
    ListView customerContactList;

    @Bind(R.id.detail_phoning_campaign_fragment_begin_campaign_button)
    Button addContactButton;

    PhoningCampaign phoningCampaign;
    HashMap<Customer,List<Contact>> customerListHashMap;

    public DetailPhoningCampaignFragment() {
        // Required empty public constructor
    }

    /**
     * Can be called when a new DetailPhoningCampaignFragment is needed
     *
     * @return DetailPhoningCampaignFragment
     */
    public static DetailPhoningCampaignFragment newInstance() {
        DetailPhoningCampaignFragment fragment = new DetailPhoningCampaignFragment();
        Bundle args     = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerListHashMap =
                (HashMap<Customer, List<Contact>>) this.getArguments().getSerializable(AddContactPhoningCampaignFragment.KEY_CUSTOMERS_ARGS);
        phoningCampaign = this.getArguments().getParcelable(AddContactPhoningCampaignFragment.KEY_PHONING_CAMPAIGN_ARGS);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail_phoning_campaign, container, false);
        ButterKnife.bind(this, v);
        initView();
        return v;
    }

    /**
     * Initialise the view of detail fragment
     */
    public void initView() {
        title.setText(phoningCampaign.getCampaignTheme());
        type.setText(phoningCampaign.getCampaignType());
        objective.setText(phoningCampaign.getCampaignObjectives());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,
                CustomerHelper.getCustomerContactName(customerListHashMap));
        customerContactList.setAdapter(adapter);
        ViewGroup.LayoutParams params = customerContactList.getLayoutParams();

        // Set the height of the Item View
        params.height = 800;
        customerContactList.setLayoutParams(params);
    }

    /**
     * Called when the user click on the button "start campaign".
     * It initialise the contact campaign and start the phoning campaign.
     * @param view
     */
    @OnClick(R.id.detail_phoning_campaign_fragment_begin_campaign_button)
    public void beginCampaign(final View view) {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        phoningCampaign.setStatut(PhoningCampaign.STATE_BEGINED);
        phoningCampaign.setBeginDate(currentDateTimeString);
       phoningCampaign.save();

        for(Customer customer : customerListHashMap.keySet()) {
            for(Contact contact : customerListHashMap.get(customer) ) {
                ContactCampaign contactCampaign = new ContactCampaign();
                contactCampaign.setCampaignId(phoningCampaign.getCampaignId());
                contactCampaign.setContactId(contact.getContactId());
                contactCampaign.setStatus(ContactCampaign.STATE_DEFINED);
                contactCampaign.save();
            }
        }

        Bundle bundle = new Bundle();

        bundle.putSerializable(CallPhoningCampaignFragment.KEY_CUSTOMER_LIST_CONTACT, customerListHashMap);
        bundle.putParcelable(CallPhoningCampaignFragment.KEY_PHONING_CAMPAIGN,
                phoningCampaign);



        CallPhoningCampaignFragment callPhoningCampaignFragment = new CallPhoningCampaignFragment();
        callPhoningCampaignFragment.setArguments(bundle);

        getActivity().getFragmentManager().beginTransaction().addToBackStack("callCampaign")
                .replace(R.id.container, callPhoningCampaignFragment).commit();



    }



}
