package fr.pds.isintheair.crmtab.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tlacouque on 27/03/2016.
 * Class used has DTO. It transmit message between android and rest server.
 */
public class MessageRestPhoningCampaign {
    private ArrayList<String> customersId;
    private PhoningCampaign phoningCampaign;
    private List<ContactCampaign> contactCampaigns;

    public ArrayList<String> getCustomersId() {
        return customersId;
    }

    public void setCustomersId(ArrayList<String> customersId) {
        this.customersId = customersId;
    }

    public PhoningCampaign getPhoningCampaign() {
        return phoningCampaign;
    }

    public void setPhoningCampaign(PhoningCampaign phoningCampaign) {
        this.phoningCampaign = phoningCampaign;
    }

    public List<ContactCampaign> getContactCampaigns() {
        return contactCampaigns;
    }

    public void setContactCampaigns(List<ContactCampaign> contactCampaigns) {
        this.contactCampaigns = contactCampaigns;
    }
}
