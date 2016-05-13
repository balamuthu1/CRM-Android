package fr.pds.isintheair.crmtab.controller.message;

import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.helper.CustomerHelper;
import fr.pds.isintheair.crmtab.helper.PhoningCampaignHelper;
import fr.pds.isintheair.crmtab.model.dao.ContactCampaignDAO;
import fr.pds.isintheair.crmtab.model.dao.ContactDAO;
import fr.pds.isintheair.crmtab.model.entity.Contact;
import fr.pds.isintheair.crmtab.model.entity.ContactCampaign;
import fr.pds.isintheair.crmtab.model.entity.Customer;
import fr.pds.isintheair.crmtab.model.entity.MessageRestPhoningCampaign;
import fr.pds.isintheair.crmtab.model.entity.PhoningCampaign;
import fr.pds.isintheair.crmtab.model.entity.ResponseRestPhoningCampaign;
import fr.pds.isintheair.crmtab.model.rest.RESTPhoningCampaignHandlerSingleton;
import fr.pds.isintheair.crmtab.view.activity.MainActivity;
import fr.pds.isintheair.crmtab.view.fragment.CallPhoningCampaignFragment;
import fr.pds.isintheair.crmtab.view.fragment.DetailPhoningCampaignFragment;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by tlacouque on 03/04/2016.
 * Class used to controll a phoning campaign
 */
public class PhoningCampaignController  {
    LinkedHashMap<Customer,List<Contact>> customerListHashMap;
    int currentCustomerposition;
    int currentContactPosition;
    PhoningCampaign phoningCampaign;
    CallPhoningCampaignFragment fragment;
    Customer currentCustomer;
    ContactCampaign contactCampaign;
    Contact currentContact;
    List<Contact> resetContact;
    LinkedHashMap<Customer,List<Contact>> customerListReseted;
    List<ContactCampaign> restContactCampaign;
    boolean boolReponse;


    public PhoningCampaignController(HashMap<Customer, List<Contact>> customerListHashMap,
                                     PhoningCampaign phoningCampaign, CallPhoningCampaignFragment fragment) {
        this.customerListHashMap = new LinkedHashMap<>(customerListHashMap);
        this.customerListReseted = new LinkedHashMap<>();
        this.phoningCampaign = phoningCampaign;
        this.fragment = fragment;
        currentCustomerposition = 0;
        currentContactPosition = 0;
        resetContact = new ArrayList<>();
        restContactCampaign = new ArrayList<>();
         boolReponse = false;
    }

    /**
     * Start the new campaign
     */
    public void beginCampaign() {
        phoningCampaign.setStatut(PhoningCampaign.STATE_BEGINED);
        phoningCampaign.save();
        updateCurrentCustomer();
        beginCall();


    }

    /**
     * Start a new call
     */
    public void beginCall() {

         currentContact = customerListHashMap.get(currentCustomer)
                .get(currentContactPosition);
         contactCampaign = ContactCampaignDAO
                .getContactCampaignFromIds(currentContact.getContactId(), phoningCampaign.getCampaignId());
        // check if the contact campaign has not already be saved
        if(contactCampaign == null) {
            endCall();
        } else {
            PhoningCampaignHelper.contactCampaignInList(contactCampaign, restContactCampaign);
            fragment.initView(phoningCampaign, currentContact, currentCustomer, contactCampaign);
            fragment.startCall();
        }
    }

    /**
     * Update the current customer if it is needed.
     */
    public void updateCurrentCustomer() {
        currentCustomer = CustomerHelper.
                getCustomerByIndex(currentCustomerposition, customerListHashMap);
    }


    /**
     * Called when the user click on the button "next call". Test if the next contact is the last of the current customer.
     * If it s false, it pass to the next contact. If t s true, it check if the customer is the last customer to call.
     * If it s true it call endCampaign(). if it s false it pass to the next customer
     */
    public void endCall() {
        // Test if the next contact is the last contact of the current customer
        if(PhoningCampaignHelper.isLastContact(customerListHashMap,currentContactPosition,currentCustomer)) {

            if(resetContact.size() != 0) {
                customerListReseted.put(currentCustomer,resetContact);
                resetContact = new ArrayList<>();
            }

            // Check if the customer is the last customer of the list of all customer to call
            if(CustomerHelper.getCustomerByIndex(customerListHashMap.size()-1,customerListHashMap) == currentCustomer) {
                if(customerListReseted.size() != 0) {
                    currentCustomerposition = 0;
                    currentContactPosition = 0;
                    customerListHashMap = customerListReseted;
                    customerListReseted = new LinkedHashMap<>();
                    updateCurrentCustomer();
                    beginCall();
                } else {
                    endCampaign();
                }


            } else {
                // Pass to the next customer and set the contact position to 0
                currentCustomerposition++;
                currentContactPosition = 0;

                updateCurrentCustomer();
                beginCall();
            }
        } else {
            // Called if there is an another contact for a customer, and start a new call
            currentContactPosition++;
            updateCurrentCustomer();
            beginCall();
        }
    }

    /**
     * Called when there is no contact left to call, it end the campaign
     */
    public void endCampaign() {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        phoningCampaign.setStatut(PhoningCampaign.STATE_ENDED);
        phoningCampaign.setEndDate(currentDateTimeString);
        phoningCampaign.save();
        endRestCampaign(new CallBackSavedResponseRestPhoningCampaign(this));
    }

    /**
     * called to save a campaign
     */
    public void endRestCampaign(CallBackSavedResponseRestPhoningCampaign cb) {
        MessageRestPhoningCampaign messageRestPhoningCampaign = new MessageRestPhoningCampaign();
        messageRestPhoningCampaign.setPhoningCampaign(phoningCampaign);
        messageRestPhoningCampaign.setContactCampaigns(restContactCampaign);
        Call<ResponseRestPhoningCampaign> call = RESTPhoningCampaignHandlerSingleton.getInstance().getPhoningCampaignService()
                .savePhoningCampaign(messageRestPhoningCampaign);

        call.enqueue(cb);
    }

    /**
     * Save commentary written by the commercial pass by the CallPhoningCampaignFragment
     * @param info
     */
    public void saveCurrentContactInfo(String info, String status) {
        contactCampaign.setContactInfo(info);
        contactCampaign.setStatus(status);
        contactCampaign.save();
    }

    /**
     * Add the contact to the contact list which the user want to call later on
     */
    public void resetCall() {
        resetContact.add(currentContact);
        endCall();
    }

    /**
     * Called to pause a campaign
     */
    public void pauseCampaign() {
        phoningCampaign.setStatut(PhoningCampaign.STATE_STOPPED);
        phoningCampaign.save();
        fragment.stopCampaign();
    }


    public LinkedHashMap<Customer, List<Contact>> getCustomerListReseted() {
        return customerListReseted;
    }

    public void setCustomerListReseted(LinkedHashMap<Customer, List<Contact>> customerListReseted) {
        this.customerListReseted = customerListReseted;
    }

    public List<Contact> getResetContact() {
        return resetContact;
    }

    public void setResetContact(List<Contact> resetContact) {
        this.resetContact = resetContact;
    }

    public Contact getCurrentContact() {
        return currentContact;
    }

    public void setCurrentContact(Contact currentContact) {
        this.currentContact = currentContact;
    }

    public ContactCampaign getContactCampaign() {
        return contactCampaign;
    }

    public void setContactCampaign(ContactCampaign contactCampaign) {
        this.contactCampaign = contactCampaign;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public CallPhoningCampaignFragment getFragment() {
        return fragment;
    }

    public void setFragment(CallPhoningCampaignFragment fragment) {
        this.fragment = fragment;
    }

    public PhoningCampaign getPhoningCampaign() {
        return phoningCampaign;
    }

    public void setPhoningCampaign(PhoningCampaign phoningCampaign) {
        this.phoningCampaign = phoningCampaign;
    }

    public int getCurrentContactPosition() {
        return currentContactPosition;
    }

    public void setCurrentContactPosition(int currentContactPosition) {
        this.currentContactPosition = currentContactPosition;
    }

    public int getCurrentCustomerposition() {
        return currentCustomerposition;
    }

    public void setCurrentCustomerposition(int currentCustomerposition) {
        this.currentCustomerposition = currentCustomerposition;
    }

    public LinkedHashMap<Customer, List<Contact>> getCustomerListHashMap() {
        return customerListHashMap;
    }

    public void setCustomerListHashMap(LinkedHashMap<Customer, List<Contact>> customerListHashMap) {
        this.customerListHashMap = customerListHashMap;
    }

    public List<ContactCampaign> getRestContactCampaign() {
        return restContactCampaign;
    }

    public void setRestContactCampaign(List<ContactCampaign> restContactCampaign) {
        this.restContactCampaign = restContactCampaign;
    }

    public boolean isBoolReponse() {
        return boolReponse;
    }

    public void setBoolReponse(boolean boolReponse) {
        this.boolReponse = boolReponse;
    }
}