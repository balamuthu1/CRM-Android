package miage.pds.api.mbalabascarin.uc.mock.contact.model;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.util.List;

/**
 * Created by Muthu on 04/03/2016.
 * Modified by tlacouque on 27/03/2016
 */

@Entity("contact")
public class Contact {
    @Id
    public int contactId;
    @Property
    public long clientId;
    @Property
    public String contactName;
    @Property
    public String contactFname;
    @Property
    public String contactTel;
    @Property
    public String contactJob;

    public String getContactJob() {
        return contactJob;
    }

    public void setContactJob(String contactJob) {
        this.contactJob = contactJob;
    }

    @Property
    public String contactStatus;

    public List<Contact> getContactList() {
        return contactList;
    }

    public void setContactList(List<Contact> contactList) {
        this.contactList = contactList;
    }

    public List<Contact> contactList;

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactFname() {
        return contactFname;
    }

    public void setContactFname(String contactFname) {
        this.contactFname = contactFname;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(String contactStatus) {
        this.contactStatus = contactStatus;
    }
}
