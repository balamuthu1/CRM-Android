package fr.pds.isintheair.crmtab.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.annotation.Unique;
import com.raizlabs.android.dbflow.structure.BaseModel;

import fr.pds.isintheair.crmtab.model.database.OrmTabDataBase;

/**
 * Created by tlacouque on 29/03/2016.
 */
@Table(database = OrmTabDataBase.class)
public class ContactCampaign extends BaseModel implements Parcelable {

    public static String STATE_DEFINED = "Cree";
    public static String STATE_ENDED = "Termine";

    @Column
    @PrimaryKey
    int contactId;

    @Column
    long campaignId;

    @Column
    String contactInfo;

    @Column
    String status;


    public ContactCampaign() {
    }

    public ContactCampaign(int contactId, long campaignId, String contactInfo) {
        this.contactId = contactId;
        this.campaignId = campaignId;
        this.contactInfo = contactInfo;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(long campaignId) {
        this.campaignId = campaignId;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static final Creator<ContactCampaign> CREATOR = new Creator<ContactCampaign>() {
        @Override
        public ContactCampaign createFromParcel(Parcel in) {
            return new ContactCampaign(in);
        }

        @Override
        public ContactCampaign[] newArray(int size) {
            return new ContactCampaign[size];
        }
    };

    protected ContactCampaign(Parcel in) {
        contactId = in.readInt();
        campaignId = in.readLong();
        contactInfo = in.readString();
        status = in.readString();

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
        dest.writeLong(campaignId);
        dest.writeString(contactInfo);
        dest.writeString(status);

    }

}
