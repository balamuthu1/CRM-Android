package miage.pds.api.tlacouque.uc.phoning.campaign.controller;

import miage.pds.MongoDatastoreConfig;
import miage.pds.api.mbalabascarin.uc.mock.contact.model.Contact;
import miage.pds.api.tlacouque.uc.admin.ref.customer.common.TileDownloader;
import miage.pds.api.tlacouque.uc.admin.ref.customer.common.XYZCalcul;
import miage.pds.api.tlacouque.uc.admin.ref.customer.controller.LocationController;
import miage.pds.api.tlacouque.uc.admin.ref.customer.createhc.dao.HealthCenterDAO;
import miage.pds.api.tlacouque.uc.admin.ref.customer.createhc.entities.HealthCenter;
import miage.pds.api.tlacouque.uc.admin.ref.customer.createindep.entities.Independant;
import miage.pds.api.tlacouque.uc.admin.ref.customer.entities.Customer;
import miage.pds.api.tlacouque.uc.admin.ref.customer.entities.MapInfo;
import miage.pds.api.tlacouque.uc.admin.ref.customer.message.MessageRestCustomer;
import miage.pds.api.tlacouque.uc.admin.ref.customer.message.ResponseRestCustomer;
import miage.pds.api.tlacouque.uc.phoning.campaign.dao.ContactCampaignDAO;
import miage.pds.api.tlacouque.uc.phoning.campaign.dao.ContactDAO;
import miage.pds.api.tlacouque.uc.phoning.campaign.dao.PhoningCampaignDAO;
import miage.pds.api.tlacouque.uc.phoning.campaign.dto.MessageRestPhoningCampaign;
import miage.pds.api.tlacouque.uc.phoning.campaign.dto.ResponseRestPhoningCampaign;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tlacouque on 27/03/2016.
 * Rest controller used to handle message from commercials tablet for phoning campaign part.
 */

@Controller
public class RestPhoningCampaignController {

    public RestPhoningCampaignController() {
    }

    /**
     * Take a MessageRestPhoningCampaign (dto) in parameter with list of String in it.
     * Return the list of contact depends on the id pass in parameter
     * @param message
     * @return ResponseRestCustomer
     */
    @RequestMapping(value = "/phoningcampaign/contact", method = RequestMethod.POST)
    public @ResponseBody
    ResponseRestPhoningCampaign getContacts(@RequestBody MessageRestPhoningCampaign message) {

        ContactDAO contactDAO = new ContactDAO(MongoDatastoreConfig.getDataStore());
        ArrayList<Contact> lcontact = new ArrayList<Contact>();
        for(String customer:message.getCustomersId()) {
         lcontact.addAll(contactDAO.findAllWithCustomerId(customer));
         }

        ResponseRestPhoningCampaign response = new ResponseRestPhoningCampaign();
        response.setContacts(lcontact);
        return response;
    }

    /**
     * Take a MessageRestPhoningCampaign (dto) in parameter with list of String in it.
     * Return the list of contact depends on the id pass in parameter
     * @param message
     * @return ResponseRestCustomer
     */
    @RequestMapping(value = "/phoningcampaign", method = RequestMethod.POST)
    public @ResponseBody
    ResponseRestPhoningCampaign savePhoningCampaign(@RequestBody MessageRestPhoningCampaign message) {
        boolean response = true;
        try {
            ContactCampaignDAO campaignDAO = new ContactCampaignDAO(MongoDatastoreConfig.getDataStore());
            PhoningCampaignDAO phoningCampaignDAO = new PhoningCampaignDAO(MongoDatastoreConfig.getDataStore());
            campaignDAO.saveContactCampaignList(message.getContactCampaigns());
            phoningCampaignDAO.save(message.getPhoningCampaign());
        } catch (Exception e) {
            response = false;
        }

        ResponseRestPhoningCampaign responseRestPhoningCampaign = new ResponseRestPhoningCampaign();
        responseRestPhoningCampaign.setSaved(response);
        return responseRestPhoningCampaign;
    }

}
