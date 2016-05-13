package miage.pds.api.mbalabascarin.uc.mock.contact.model;

import java.util.List;

/**
 * Created by Muthu on 04/03/2016.
 */
public class ContactContainer {
    List<Contact> contactList;
    public void setContacts(List<Contact> contacts){
        contactList = contacts;
    }

    public List<Contact> getContacts(){
        return contactList;
    }
}
