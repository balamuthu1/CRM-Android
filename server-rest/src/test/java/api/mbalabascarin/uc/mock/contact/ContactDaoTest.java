package api.mbalabascarin.uc.mock.contact;

import miage.pds.api.mbalabascarin.uc.mock.contact.dao.ContactDao;
import miage.pds.api.mbalabascarin.uc.mock.contact.model.Contact;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ContactDaoTest {
	private ContactDao dao;
	private Contact contact;
	
	@Before
	public void initDao(){
		System.out.println("Launching tests...");
		dao = new ContactDao();
	}
	

	@Test
	public void testDaoConnection(){
		assertEquals(true, dao.ConnectDB());
		
	}
	
	@Test
	public void testCreateContact(){
		Boolean status = false;
		contact = new Contact();
		contact.setContactId(1273);
		contact.setContactName("Muthu");
		contact.setContactFname("BALA");
		contact.setContactStatus("Commercial");
		
		status = dao.createContact(contact);
		assertTrue(status);
		
	}
		
	
}
