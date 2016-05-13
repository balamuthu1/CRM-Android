package fr.pds.isintheair.crmtab.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Muthu on 02/03/2016.
 * Modified by tlacouque on 03/04/2016
 */
public class Contact implements Parcelable {
    public int contactId;
    public long clientId;
    public String contactName;
    public String contactFname;
    public String contactTel;
    public String contactJob;

    public Contact() {
    }

    public String getContactJob() {
        return contactJob;
    }

    public void setContactJob(String contactJob) {
        this.contactJob = contactJob;
    }

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

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    protected Contact(Parcel in) {
        contactId = in.readInt();
        clientId = in.readLong();
        contactName = in.readString();
        contactFname = in.readString();
        contactTel = in.readString();
        contactJob = in.readString();
    }

    /**
     * Return the number of serializable attributes to needed to parse an contact campaign object
     *
     * @return int
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(contactId);
        dest.writeLong(clientId);
        dest.writeString(contactName);
        dest.writeString(contactFname);
        dest.writeString(contactTel);
        dest.writeString(contactJob);
    }

}
