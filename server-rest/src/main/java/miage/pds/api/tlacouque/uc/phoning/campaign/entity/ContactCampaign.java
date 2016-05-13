package miage.pds.api.tlacouque.uc.phoning.campaign.entity;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

/**
 * Created by tlacouque on 08/04/2016.
 */
@Entity("contactcampaign")
public class ContactCampaign {

    @Id
    protected ObjectId id;

    @Property
    int contactId;

    @Property
    long campaignId;

    @Property
    String contactInfo;

    @Property
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


}
