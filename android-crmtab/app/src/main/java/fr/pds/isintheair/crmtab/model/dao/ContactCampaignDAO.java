package fr.pds.isintheair.crmtab.model.dao;

import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

import fr.pds.isintheair.crmtab.model.entity.ContactCampaign;
import fr.pds.isintheair.crmtab.model.entity.ContactCampaign_Table;
import fr.pds.isintheair.crmtab.model.mock.Contact;

/**
 * Created by tlacouque on 03/04/2016.
 * DAO used to get ContactCampaign
 */
public class ContactCampaignDAO {


    /**
     * Get a single ContactCampaign from is contactId and is campaignId which state is defined
     * @param contactId
     * @param campaignId
     * @return
     */
    public static ContactCampaign getContactCampaignFromIds(int contactId,long campaignId) {
        return new Select().from(ContactCampaign.class)
                .where(ContactCampaign_Table.campaignId.eq(campaignId))
                .and(ContactCampaign_Table.contactId.eq(contactId))
                .and(ContactCampaign_Table.status.eq(ContactCampaign.STATE_DEFINED))
                .querySingle();
    }

    /**
     * Get a single ContactCampaign from is contactId and is campaignId which state is defined
     * @param campaignId
     * @return
     */
    public static List<ContactCampaign> getContactCampaignFromCampaignId(long campaignId) {
        return new Select().from(ContactCampaign.class)
                .where(ContactCampaign_Table.campaignId.eq(campaignId))
                .and(ContactCampaign_Table.status.eq(ContactCampaign.STATE_DEFINED))
                .queryList();
    }

    /**
     * Get a single ContactCampaign from is contactId and is campaignId which state is defined
     * @param campaignId
     * @return
     */
    public static List<ContactCampaign> getContactCampaignFromCampaignIdNoStatus(long campaignId) {
        return new Select().from(ContactCampaign.class)
                .where(ContactCampaign_Table.campaignId.eq(campaignId))
                .queryList();
    }

}
