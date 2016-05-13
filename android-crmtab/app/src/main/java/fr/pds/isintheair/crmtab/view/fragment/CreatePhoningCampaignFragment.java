package fr.pds.isintheair.crmtab.view.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.controller.message.CallBackGetContactsForPhoningCampaign;
import fr.pds.isintheair.crmtab.controller.message.CallBackGetOtherCustomers;
import fr.pds.isintheair.crmtab.controller.message.CallBackSaveUnfinishedPhoningCampaign;
import fr.pds.isintheair.crmtab.helper.CheckInternetConnexion;
import fr.pds.isintheair.crmtab.helper.CustomerHelper;
import fr.pds.isintheair.crmtab.model.dao.ContactCampaignDAO;
import fr.pds.isintheair.crmtab.model.dao.PhoningCampaignDAO;
import fr.pds.isintheair.crmtab.model.dao.UserDAO;
import fr.pds.isintheair.crmtab.model.entity.Contact;
import fr.pds.isintheair.crmtab.model.entity.ContactCampaign;
import fr.pds.isintheair.crmtab.model.entity.Customer;
import fr.pds.isintheair.crmtab.model.entity.HealthCenter;
import fr.pds.isintheair.crmtab.model.entity.Independant;
import fr.pds.isintheair.crmtab.model.entity.MessageRestPhoningCampaign;
import fr.pds.isintheair.crmtab.model.entity.PhoningCampaign;
import fr.pds.isintheair.crmtab.model.entity.PhoningCampaignType;
import fr.pds.isintheair.crmtab.model.entity.ResponseRestCustomer;
import fr.pds.isintheair.crmtab.model.entity.ResponseRestPhoningCampaign;
import fr.pds.isintheair.crmtab.model.rest.RESTCustomerHandlerSingleton;
import fr.pds.isintheair.crmtab.model.rest.RESTPhoningCampaignHandlerSingleton;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by tlacouque on 26/03/2016.
 */
public class CreatePhoningCampaignFragment extends Fragment  implements Validator.ValidationListener,
        ReplayCampaignAlertDialog.AlertPositiveListener {


    public String idUser = UserDAO.getCurrentUser().getId();

    @Bind(R.id.create_phoning_campaign_fragment_title)
    @NotEmpty(messageResId = R.string.create_phoning_campaign_fragment_error_title_empty)
    EditText title;

    @Bind(R.id.create_phoning_campaign_fragment_objective)
    @NotEmpty(messageResId = R.string.create_phoning_campaign_fragment_error_objective_empty)
    EditText objective;

    @Bind(R.id.create_phoning_campaign_fragment_campaign_type)
    Spinner type;

    @Bind(R.id.create_phoning_campaign_fragment_list_customer)
    ListView customer;

    @Bind(R.id.create_phoning_campaign_fragment_contact_list_button)
    Button addContactButton;

    List<Customer> customers;

    List<Customer> customersAdded;

    Validator       validator;

    PhoningCampaign phoningCampaign;

    public CreatePhoningCampaignFragment() {
        // Required empty public constructor
    }

    /**
     * Can be called when a new CreatePhoningCampaignFragment is needed
     *
     * @return CreatePhoningCampaignFragment
     */
    public static CreatePhoningCampaignFragment newInstance() {
        CreatePhoningCampaignFragment fragment = new CreatePhoningCampaignFragment();
        Bundle args     = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customersAdded = new ArrayList<Customer>();
        validator = new Validator(this);
        validator.setValidationListener(this);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_phoning_campaign, container, false);
        ButterKnife.bind(this, v);

        initSpinner();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        initCustomers();
    }

    /**
     * Initialise the list of customer. if the user have an internet connexion
     * a rest call is send to have customer added by other commercials.
     */
    public void initCustomers() {

        final List<Customer> customers        = (List<Customer>) (List<?>) new Select().from(HealthCenter.class).queryList();
        final List<Customer>     customersindeps  = (List<Customer>) (List<?>) new Select().from(Independant.class).queryList();
        final List<HealthCenter> healthCenterList = new Select().from(HealthCenter.class).queryList();
        final List<Independant>  independantList  = new Select().from(Independant.class).queryList();
        customers.addAll(customersindeps);
        //Check if there is a network available
        if (CheckInternetConnexion.isNetworkAvailable(this.getActivity().getApplicationContext())) {
            callRest(customers, healthCenterList, independantList);
        }
        else {
            callRestFailed();
        }
    }

    /**
     * Called to do a rest call to have customer added by other commercials.
     *
     * @param customers
     */
    private void callRest(final List<Customer> customers, final List<HealthCenter> healthCenterList,
                          final List<Independant> independantList) {
        Call<ResponseRestCustomer> call = RESTCustomerHandlerSingleton.getInstance()
                .getCustomerService().getCustomers(this.idUser);
        CallBackGetOtherCustomers cb = new CallBackGetOtherCustomers(this,healthCenterList,customers);
        call.enqueue(cb);
    }

    /**
     * Method called to initialiase the list of Customer
     *
     * @param customers
     */
    public void initAdapter(List<Customer> customers) {

        this.customers = customers;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_multiple_choice, CustomerHelper.getCustomersName(customers));
        customer.setAdapter(adapter);
        customer.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ViewGroup.LayoutParams params = customer.getLayoutParams();

        // Set the height of the Item View
        params.height = 1000;
        customer.setLayoutParams(params);

    }

    /**
     * Check if a campaign is already set, if it's true it show a pop up to know if the commercial
     * whants to continue the campaign or start a new one.
     */
    public void isCampaignAlreadySet() {
        phoningCampaign = PhoningCampaignDAO.getStoppedPhoningCampaign();
        if (phoningCampaign != null) {
            ReplayCampaignAlertDialog alert = new ReplayCampaignAlertDialog();
            alert.setAlertPositiveListener(this);
            FragmentManager manager = getFragmentManager();
            /** Creating the dialog fragment object, which will in turn open the alert dialog window */
            alert.show(manager, "ReplayCampaignAlertDialog");
        }
    }


    /**
     * Initialise the phoning campaign type spinner
     */
    private void initSpinner() {
        type.setAdapter(new ArrayAdapter<PhoningCampaignType>
                (getActivity().getApplicationContext(), R.layout.create_customer_spinner_view, PhoningCampaignType.values()));

    }

    /**
     * Called when the user wants to add contact to the phoning campaign
     * @param view
     */
    @OnClick(R.id.create_phoning_campaign_fragment_contact_list_button)
    public void onButtonClick(final View view) {
        validator.validate(true);
    }

    /**
     * Method called when a call rest failed (by the server, or the network), display an error message
     * and return to the home page
     */
    public void callRestFailed() {
        Snackbar snackbar = Snackbar.make(this.getView(),
                R.string.create_phoning_campaign_fragment_rest_server_error,
                Snackbar.LENGTH_LONG);
        ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setMaxLines(2);
        snackbar.show();
        CreatePhoningCampaignFragment.this.getFragmentManager()
                .popBackStack("createPhoning", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * Method called when the user wants to add contact and all the validation passed.
     * It start the add contact fragment and pass the good parameters for it.
     */
    @Override
    public void onValidationSucceeded() {
        SparseBooleanArray position = customer.getCheckedItemPositions();
        int len = customer.getCount();

            for (int i = 0; i < len; i++) {
                if (position.get(i)) {
                    customersAdded.add(CustomerHelper.getCustomerFromName(
                            (String) customer.getItemAtPosition(i), customers));
                }
            }
        if(!(customersAdded.size() == 0)) {
            String typeString = type.getSelectedItem().toString();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(AddContactPhoningCampaignFragment.KEY_CUSTOMERS_ARGS,
                    (ArrayList<? extends Parcelable>) customersAdded);
            bundle.putParcelable(AddContactPhoningCampaignFragment.KEY_PHONING_CAMPAIGN_ARGS,
                    new PhoningCampaign(title.getText().toString(), typeString, objective.getText().toString()));
            AddContactPhoningCampaignFragment addContactPhoningCampaignFragment = new AddContactPhoningCampaignFragment();
            addContactPhoningCampaignFragment.setArguments(bundle);

            ((AppCompatActivity) getActivity()).getFragmentManager().beginTransaction().addToBackStack("detailHc")
                    .replace(R.id.container, addContactPhoningCampaignFragment).commit();
        }
    }


    /**
     * Method called when the user wants to add contact and at least one validation failed.
     * It display all errors on a pop up
     * @param errors
     */
    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        String errorString = "";

        // Concat error strings
        for (ValidationError error : errors) {
            errorString = errorString + error.getCollatedErrorMessage
                    (getActivity().getApplicationContext()) + ".\n";
        }

        ErrorCustomerAlertDialog alertDialog =
                new ErrorCustomerAlertDialog(getContext());
        alertDialog.setTitle(R.string.create_phoning_campaign_fragment_alert_dialog_error);
        alertDialog.setMessage(errorString);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }


    /**
     * Method called when a phoning campaign is already set, and the user wants to continue it.
     * It do a rest call to have all the contact needed
     */
    @Override
    public void onPositiveClick() {
        MessageRestPhoningCampaign message = new MessageRestPhoningCampaign();
        List<Independant> independants = new ArrayList<>();
        List<HealthCenter> healthCenters = new ArrayList<>();
        CustomerHelper.addHCIndependantIntoList(customers, independants, healthCenters);
        ArrayList<String> customersId = CustomerHelper.getCustomerIds(independants, healthCenters);

        message.setCustomersId(customersId);
        Call<ResponseRestPhoningCampaign> call = RESTPhoningCampaignHandlerSingleton.getInstance()
                .getPhoningCampaignService().getContacts(message);
        CallBackGetContactsForPhoningCampaign cb = new CallBackGetContactsForPhoningCampaign(this);
        call.enqueue(cb);
    }

    /**
     * Method called after all the contact has been send by the rest server.
     * It restart the campaign and begin a new call.
     * @param contacts
     */
    public void restartCampaign(List<Contact> contacts) {
        HashMap<Customer,List<Contact>> hashMap = CustomerHelper.getCustomerContactsMap(customers,contacts);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CallPhoningCampaignFragment.KEY_CUSTOMER_LIST_CONTACT, hashMap);
        bundle.putParcelable(CallPhoningCampaignFragment.KEY_PHONING_CAMPAIGN,
                phoningCampaign);
        CallPhoningCampaignFragment callPhoningCampaignFragment = new CallPhoningCampaignFragment();
        callPhoningCampaignFragment.setArguments(bundle);
        getActivity().getFragmentManager().beginTransaction().addToBackStack("callCampaign")
                .replace(R.id.container, callPhoningCampaignFragment).commit();
    }

    /**
     * Method called when a phoning campaign is already set, and the user wants to stop it.
     * It do a rest call to save the phoning campaign that is stopped.
     */
    @Override
    public void onNegativeClick() {
        phoningCampaign.setStatut(PhoningCampaign.STATE_ENDED);
        phoningCampaign.save();
        List<ContactCampaign> contactCampaigns =
                ContactCampaignDAO.getContactCampaignFromCampaignIdNoStatus(phoningCampaign.getCampaignId());
        MessageRestPhoningCampaign messageRestPhoningCampaign = new MessageRestPhoningCampaign();
        messageRestPhoningCampaign.setPhoningCampaign(phoningCampaign);
        messageRestPhoningCampaign.setContactCampaigns(contactCampaigns);
        Call<ResponseRestPhoningCampaign> call = RESTPhoningCampaignHandlerSingleton.getInstance().getPhoningCampaignService()
                .savePhoningCampaign(messageRestPhoningCampaign);
        CallBackSaveUnfinishedPhoningCampaign cb = new CallBackSaveUnfinishedPhoningCampaign(this);
        call.enqueue(cb);

    }

    /**
     * Called when the rest call to save the phoning campaign is done well.
     * It show a snack bar which said that the phoning campaign is stopped
     * @param bool
     */
    public void stopCampaign(boolean bool) {
        Snackbar snackbar;
        if(bool) {
            snackbar = Snackbar.make(this.getView(), R.string.call_phoning_campaign_fragment_stopped_campaign,
                    Snackbar.LENGTH_LONG);
        } else {
            snackbar = Snackbar.make(this.getView(), R.string.call_phoning_campaign_fragment_end_campaign_error,
                    Snackbar.LENGTH_LONG);
        }

        ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setMaxLines(2);
        snackbar.show();
    }
}
