package miage.pds.api.tlacouque.uc.phoning.campaign.dao;

import miage.pds.api.mbalabascarin.uc.mock.contact.model.Contact;
import miage.pds.api.tlacouque.uc.phoning.campaign.entity.ContactCampaign;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.List;

/**
 * Created by tlacouque on 08/04/2016.
 * ContactCampaignDAO used to save a contact campaign list
 */
public class ContactCampaignDAO extends BasicDAO<ContactCampaign,ObjectId> {
    public ContactCampaignDAO( Datastore ds) {
        super(ds);
    }

    /**
     * Save a list of contact campaign pass in paramater
     * @param contactCampaigns
     */
    public void saveContactCampaignList(List<ContactCampaign> contactCampaigns) {
        for(ContactCampaign campaign : contactCampaigns) {
            this.save(campaign);
        }
    }

}
