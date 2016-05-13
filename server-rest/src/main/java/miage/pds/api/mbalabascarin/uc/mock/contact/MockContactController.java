package miage.pds.api.mbalabascarin.uc.mock.contact;

import miage.pds.api.mbalabascarin.uc.mock.contact.dao.ContactDao;
import miage.pds.api.mbalabascarin.uc.mock.contact.model.Contact;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MockContactController {

	/**
	 * Simply returns a status string.
	 */
	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public @ResponseBody String home() {

		//logger.info("REST SERVER IS RUNNING :)");
		return "contact webservice :)";
	}
	
	@RequestMapping(value = "/contact/addContact", method = RequestMethod.POST, headers="Accept=application/json")
	public @ResponseBody Boolean createContact(@RequestBody Contact contact) {
		//return new ContactDao().createContacts(contactList);
		return new ContactDao().createContact(contact);
		
	}
}
