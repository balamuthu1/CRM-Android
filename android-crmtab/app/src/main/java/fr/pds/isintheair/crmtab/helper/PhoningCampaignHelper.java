package fr.pds.isintheair.crmtab.helper;

import java.util.LinkedHashMap;
import java.util.List;

import fr.pds.isintheair.crmtab.model.entity.Contact;
import fr.pds.isintheair.crmtab.model.entity.ContactCampaign;
import fr.pds.isintheair.crmtab.model.entity.Customer;

/**
 * Created by tlacouque on 03/04/2016.
 * Class used to help the phoning campaign controller
 */
public class PhoningCampaignHelper {

    /**
     * Static method used to know if the currentContact pass by the position is the last contact
     * of a customer pass in parameter in the linked list in parameter too.
     * @param customerListHashMap
     * @param currentContactPosition
     * @param currentCustomer
     * @return
     */
    public static boolean isLastContact(LinkedHashMap<Customer,List<Contact>> customerListHashMap,
                                        int currentContactPosition,Customer currentCustomer) {
        int futurContactPosition = currentContactPosition + 1;
        return customerListHashMap.get(currentCustomer).size() == futurContactPosition;
    }

    /**
     * Check if the contact campaign is already in the last pass in parameter. If it s true it
     * replace the old contact campaign by the new.
     * @param cp
     * @param restContactCampaign
     * @return
     */
    public static void contactCampaignInList(ContactCampaign cp, List<ContactCampaign> restContactCampaign) {
       boolean bool = false;
        ContactCampaign cpToRemove = null;
        for(ContactCampaign  contactCampaign : restContactCampaign) {
            if(contactCampaign.getContactId() == cp.getContactId()) {
                bool = true;
                cpToRemove = contactCampaign;
            }
        }
      if(bool) {
          restContactCampaign.remove(cpToRemove);
      }
        restContactCampaign.add(cp);

    }
}
