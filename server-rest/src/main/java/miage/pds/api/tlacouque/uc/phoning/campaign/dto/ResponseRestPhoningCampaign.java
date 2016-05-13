package miage.pds.api.tlacouque.uc.phoning.campaign.dto;

import miage.pds.api.mbalabascarin.uc.mock.contact.model.Contact;
import miage.pds.api.tlacouque.uc.admin.ref.customer.entities.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tlacouque on 27/03/2016.
 */
public class ResponseRestPhoningCampaign {
    boolean saved;
    ArrayList<Contact> contacts;


    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }
}
