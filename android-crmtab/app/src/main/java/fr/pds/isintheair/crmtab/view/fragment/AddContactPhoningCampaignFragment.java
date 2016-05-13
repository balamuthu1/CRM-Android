package fr.pds.isintheair.crmtab.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.mobsandgeeks.saripaar.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.helper.CustomerHelper;
import fr.pds.isintheair.crmtab.model.entity.Contact;
import fr.pds.isintheair.crmtab.model.entity.Customer;
import fr.pds.isintheair.crmtab.model.entity.HealthCenter;
import fr.pds.isintheair.crmtab.model.entity.Independant;
import fr.pds.isintheair.crmtab.model.entity.MessageRestPhoningCampaign;
import fr.pds.isintheair.crmtab.model.entity.PhoningCampaign;
import fr.pds.isintheair.crmtab.model.entity.ResponseRestCustomer;
import fr.pds.isintheair.crmtab.model.entity.ResponseRestPhoningCampaign;
import fr.pds.isintheair.crmtab.model.rest.RESTCustomerHandlerSingleton;
import fr.pds.isintheair.crmtab.model.rest.RESTPhoningCampaignHandlerSingleton;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by tlacouque on 27/03/2016.
 */
public class AddContactPhoningCampaignFragment extends Fragment {

    @Bind(R.id.add_contact_phoning_campaign_fragment_list_contact)
    ListView contacts;

    @Bind(R.id.add_contact_phoning_campaign_fragment_button)
    Button createCampaign;

    public static final String KEY_CUSTOMERS_ARGS = "CUSTOMERS";
    public static final String KEY_PHONING_CAMPAIGN_ARGS = "PHONING_CAMPAING";

    List<Customer> customerList;
    List<Contact> restContacts;
    PhoningCampaign phoningCampaign;

    public AddContactPhoningCampaignFragment() {
        // Required empty public constructor
    }

    /**
     * Can be called when a new AddContactPhoningCampaignFragment is needed
     *
     * @return AddContactPhoningCampaignFragment
     */
    public static AddContactPhoningCampaignFragment newInstance() {
        AddContactPhoningCampaignFragment fragment = new AddContactPhoningCampaignFragment();
        Bundle args     = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      customerList = this.getArguments().getParcelableArrayList(KEY_CUSTOMERS_ARGS);
        phoningCampaign = this.getArguments().getParcelable(KEY_PHONING_CAMPAIGN_ARGS);
        restContacts = new ArrayList<Contact>();
        callRest();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_contact_phoning_campaign, container, false);
        ButterKnife.bind(this, v);
        return v;
    }



    /**
     * Called to do a rest call to have contact for customers selected.
     */
    private void callRest() {
        MessageRestPhoningCampaign message = new MessageRestPhoningCampaign();
        List<Independant> independants = new ArrayList<>();
        List<HealthCenter> healthCenters = new ArrayList<>();
        CustomerHelper.addHCIndependantIntoList(customerList, independants, healthCenters);
        ArrayList<String> customersId = CustomerHelper.getCustomerIds(independants,healthCenters);

        message.setCustomersId(customersId);
        Call<ResponseRestPhoningCampaign> call = RESTPhoningCampaignHandlerSingleton.getInstance()
                .getPhoningCampaignService().getContacts(message);

        call.enqueue(new Callback<ResponseRestPhoningCampaign>() {
            @Override
            public void onResponse(Response<ResponseRestPhoningCampaign> response, Retrofit retrofit) {

                if (response.errorBody() == null) {
                    if (response.body() != null) {
                        restContacts = response.body().getContacts();
                        initContactList(CustomerHelper.getCustomerContactsMap
                                (customerList, response.body().getContacts()));

                    }

                }

            }

            @Override
            public void onFailure(Throwable t) {
                //initAdapter(customers);

            }
        });
    }

    /**
     * Method called to initialise the contact list
     * @param customerListHashMap
     */
    public void initContactList(HashMap<Customer,List<Contact>> customerListHashMap) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_multiple_choice,
                CustomerHelper.getCustomerContactName(customerListHashMap));
        contacts.setAdapter(adapter);
        contacts.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ViewGroup.LayoutParams params = contacts.getLayoutParams();

        // Set the height of the Item View
        params.height = 1000;
        contacts.setLayoutParams(params);
    }

    /**
     * Method called when the user click on the button "create campaign".
     * @param view
     */
    @OnClick(R.id.add_contact_phoning_campaign_fragment_button)
    public void createCampaign(final View view) {
        SparseBooleanArray position = contacts.getCheckedItemPositions();
        int len = contacts.getCount();
        List<Contact> contactsAdded = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            if (position.get(i)) {
                contactsAdded.add(CustomerHelper.getContactFromName(
                        (String) contacts.getItemAtPosition(i), restContacts));
            }
        }
        if(!(contactsAdded.size() == 0)) {
            HashMap<Customer, List<Contact>> customerListHashMap = CustomerHelper.getCustomerContactsMap(
                    customerList, contactsAdded);
            Bundle bundle = new Bundle();
            bundle.putSerializable(AddContactPhoningCampaignFragment.KEY_CUSTOMERS_ARGS,
                    customerListHashMap);
            phoningCampaign.setStatut(PhoningCampaign.STATE_DEFINED);
            phoningCampaign.save();
            bundle.putParcelable(AddContactPhoningCampaignFragment.KEY_PHONING_CAMPAIGN_ARGS,
                    phoningCampaign);
            DetailPhoningCampaignFragment detailPhoningCampaignFragment = new DetailPhoningCampaignFragment();
            detailPhoningCampaignFragment.setArguments(bundle);

            ((AppCompatActivity) getActivity()).getFragmentManager().beginTransaction().addToBackStack("detailHc")
                    .replace(R.id.container, detailPhoningCampaignFragment).commit();
        }
    }
}
