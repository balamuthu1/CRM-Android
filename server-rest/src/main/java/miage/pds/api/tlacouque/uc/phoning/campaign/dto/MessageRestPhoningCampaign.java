package miage.pds.api.tlacouque.uc.phoning.campaign.dto;

import miage.pds.api.tlacouque.uc.admin.ref.customer.createhc.entities.HealthCenter;
import miage.pds.api.tlacouque.uc.admin.ref.customer.createindep.entities.Independant;
import miage.pds.api.tlacouque.uc.admin.ref.customer.entities.Customer;
import miage.pds.api.tlacouque.uc.phoning.campaign.entity.ContactCampaign;
import miage.pds.api.tlacouque.uc.phoning.campaign.entity.PhoningCampaign;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tlacouque on 27/03/2016.
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
