package fr.pds.isintheair.crmtab;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import fr.pds.isintheair.crmtab.helper.CustomerHelper;
import fr.pds.isintheair.crmtab.model.entity.Contact;
import fr.pds.isintheair.crmtab.model.entity.Customer;
import fr.pds.isintheair.crmtab.model.entity.HealthCenter;
import fr.pds.isintheair.crmtab.model.entity.Independant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by tlacouque on 30/03/2016.
 */
public class CustomerHelperTest {

    @Test
    public void testGetCustomerFromName() throws Exception {

        String test1Name = "Test1";
        String test2Name = "Test2";
        String test3Name = "Test3";

        Customer customerTest1 = Mockito.mock(Customer.class);
        when(customerTest1.getName()).thenReturn(test1Name);

        Customer customerTest2 = Mockito.mock(Customer.class);
        when(customerTest2.getName()).thenReturn(test2Name);

        Customer customerTest3 = Mockito.mock(Customer.class);
        when(customerTest3.getName()).thenReturn(test3Name);

        ArrayList<Customer> customerArrayList = new ArrayList<>();
        customerArrayList.add(customerTest1);
        customerArrayList.add(customerTest2);
        customerArrayList.add(customerTest3);

        Customer customerReturned = CustomerHelper.getCustomerFromName(test2Name,customerArrayList);

        assertEquals(customerReturned.getName(),customerTest2.getName());

    }

    @Test
    public void testaddHCIndependantIntoList() throws Exception {
        HealthCenter healthCenter1 = new HealthCenter();
        HealthCenter healthCenter2 = new HealthCenter();
        HealthCenter healthCenter3 = new HealthCenter();

        Independant independant1 = new Independant();
        Independant independant2 = new Independant();

        ArrayList<Customer> customers = new ArrayList<>();
        ArrayList<HealthCenter> healthCenters = new ArrayList<>();
        ArrayList<Independant> independants = new ArrayList<>();

        customers.add(healthCenter1);
        customers.add(independant1);
        customers.add(healthCenter2);
        customers.add(independant2);
        customers.add(healthCenter3);

        CustomerHelper.addHCIndependantIntoList(customers, independants, healthCenters);

        assertEquals(healthCenters.size(), 3);
        assertEquals(independants.size(), 2);

    }

    @Test
    public void testGetCustomerContactsMap() throws Exception {
        long indepSiret = 1L;
        long hcSiret = 2L;

        HealthCenter healthCenter = new HealthCenter();
        Independant independant = new Independant();
        healthCenter.setSiretNumber(hcSiret);
        independant.setSiretNumber(indepSiret);


        Contact contactHC1 = new Contact();
        contactHC1.setClientId(hcSiret);
        Contact contactHC2 = new Contact();
        contactHC2.setClientId(hcSiret);

        Contact contactIndep1 = new Contact();
        contactIndep1.setClientId(indepSiret);
        Contact contactIndep2 = new Contact();
        contactIndep2.setClientId(indepSiret);
        Contact contactIndep3 = new Contact();
        contactIndep3.setClientId(indepSiret);

        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(contactHC1);
        contacts.add(contactIndep1);
        contacts.add(contactIndep2);
        contacts.add(contactHC2);
        contacts.add(contactIndep3);

        ArrayList<Customer> customers = new ArrayList<>();
        customers.add(independant);
        customers.add(healthCenter);

        HashMap<Customer,List<Contact>> hashMap = CustomerHelper.getCustomerContactsMap(customers, contacts);

        assertEquals(hashMap.get(healthCenter).size(),2);
        assertEquals(hashMap.get(independant).size(),3);
        assertEquals(hashMap.keySet().size(),2);


    }

    @Test
    public void testGetCustomersName() throws Exception {
        String hcName = "HcName";
        String indepName = "IndepName";

        HealthCenter healthCenter = new HealthCenter();
        Independant independant = new Independant();

        healthCenter.setName(hcName);
        independant.setName(indepName);

        ArrayList<Customer> customers = new ArrayList<>();
        customers.add(independant);
        customers.add(healthCenter);

        List<String> strings = CustomerHelper.getCustomersName(customers);
        assertEquals(strings.get(0),indepName);
        assertEquals(strings.get(1),hcName);

    }

    @Test
    public void testGetCustomerContactName() throws Exception {
        String customerName1 = "Customer 1";
        String customerName2 = "Customer 2";
        String contactName1 = "Contact 1";
        String contactName2 = "Contact 2";
        String contactName3 = "Contact 3";
        String contactName4 = "Contact 4";
        String contactName5 = "Contact 5";
        String contactFName1 = "Fname1";
        String contactFName2 = "Fname2";
        String contactFName3 = "Fname3";
        String contactFName4 = "Fname4";
        String contactFName5 = "Fname5";

        Customer customer1 = Mockito.mock(Customer.class);
        when(customer1.getName()).thenReturn(customerName1);

        Customer customer2 = Mockito.mock(Customer.class);
        when(customer2.getName()).thenReturn(customerName2);

        Contact contact1 = new Contact();
        contact1.setContactName(contactName1);
        contact1.setContactFname(contactFName1);

        Contact contact2 = new Contact();
        contact2.setContactName(contactName2);
        contact2.setContactFname(contactFName2);

        Contact contact3 = new Contact();
        contact3.setContactName(contactName3);
        contact3.setContactFname(contactFName3);

        Contact contact4 = new Contact();
        contact4.setContactName(contactName4);
        contact4.setContactFname(contactFName4);

        Contact contact5 = new Contact();
        contact5.setContactName(contactName5);
        contact5.setContactFname(contactFName5);

        List<Contact> contactsCustomer1 = new ArrayList<>();
        contactsCustomer1.add(contact1);
        contactsCustomer1.add(contact5);
        List<Contact> contactsCustomer2 = new ArrayList<>();
        contactsCustomer2.add(contact3);
        contactsCustomer2.add(contact4);
        contactsCustomer2.add(contact2);

        HashMap<Customer,List<Contact>> hashMap = new HashMap<Customer,List<Contact>>();

        hashMap.put(customer1, contactsCustomer1);
        hashMap.put(customer2,contactsCustomer2);

        List<String> strings = CustomerHelper.getCustomerContactName(hashMap);

        assertEquals(5,strings.size());


    }

    @Test
    public void testGetContactFromName() throws Exception {

        String test1Name = "Test1";
        String test2Name = "Test2";
        String test3Name = "Test3";
        String test1FName = "Test1";
        String test2FName = "Test2";
        String test3FName = "Test3";

        Contact contact1 = new Contact();
        contact1.setContactName(test1Name);
        contact1.setContactFname(test1FName);

        Contact contact2 = new Contact();
        contact2.setContactName(test2Name);
        contact2.setContactFname(test2FName);

        Contact contact3 = new Contact();
        contact3.setContactName(test3Name);
        contact3.setContactFname(test3FName);

        ArrayList<Contact> customerArrayList = new ArrayList<>();
        customerArrayList.add(contact1);
        customerArrayList.add(contact2);
        customerArrayList.add(contact3);

        Contact customerReturned = CustomerHelper.getContactFromName(" : "+test2Name+" "+test2FName, customerArrayList);

        assertEquals(test2Name+" "+test2FName,customerReturned.contactName+" "+customerReturned.contactFname);

    }

    @Test
    public void testGetCustomerByIndex() throws Exception {
        String customerName1 = "Customer 1";
        String customerName2 = "Customer 2";
        String contactName1 = "Contact 1";
        String contactName2 = "Contact 2";
        String contactName3 = "Contact 3";
        String contactName4 = "Contact 4";
        String contactName5 = "Contact 5";
        String contactFName1 = "Fname1";
        String contactFName2 = "Fname2";
        String contactFName3 = "Fname3";
        String contactFName4 = "Fname4";
        String contactFName5 = "Fname5";

        Customer customer1 = Mockito.mock(Customer.class);
        when(customer1.getName()).thenReturn(customerName1);

        Customer customer2 = Mockito.mock(Customer.class);
        when(customer2.getName()).thenReturn(customerName2);

        Contact contact1 = new Contact();
        contact1.setContactName(contactName1);
        contact1.setContactFname(contactFName1);

        Contact contact2 = new Contact();
        contact2.setContactName(contactName2);
        contact2.setContactFname(contactFName2);

        Contact contact3 = new Contact();
        contact3.setContactName(contactName3);
        contact3.setContactFname(contactFName3);

        Contact contact4 = new Contact();
        contact4.setContactName(contactName4);
        contact4.setContactFname(contactFName4);

        Contact contact5 = new Contact();
        contact5.setContactName(contactName5);
        contact5.setContactFname(contactFName5);

        List<Contact> contactsCustomer1 = new ArrayList<>();
        contactsCustomer1.add(contact1);
        contactsCustomer1.add(contact5);
        List<Contact> contactsCustomer2 = new ArrayList<>();
        contactsCustomer2.add(contact3);
        contactsCustomer2.add(contact4);
        contactsCustomer2.add(contact2);

        LinkedHashMap<Customer,List<Contact>> listLinkedHashMap = new LinkedHashMap<>();

        listLinkedHashMap.put(customer1, contactsCustomer1);
        listLinkedHashMap.put(customer2,contactsCustomer2);

        Customer customer = CustomerHelper.getCustomerByIndex(1,listLinkedHashMap);
        assertEquals(customer2.getName(),customer.getName());


    }

    @Test
    public void testGetCustomerIds() throws Exception {
        Independant independant = new Independant();
        independant.setSiretNumber(1);
        Independant independant2 = new Independant();
        independant.setSiretNumber(2);
        Independant independant3 = new Independant();
        independant.setSiretNumber(3);

        ArrayList<Independant> independants = new ArrayList<>();
        independants.add(independant);
        independants.add(independant2);
        independants.add(independant3);

        HealthCenter healthCenter = new HealthCenter();
        healthCenter.setSiretNumber(4);
        HealthCenter healthCenter2 = new HealthCenter();
        healthCenter2.setSiretNumber(5);
        HealthCenter healthCenter3 = new HealthCenter();
        healthCenter3.setSiretNumber(6);

        ArrayList<HealthCenter> healthcenters = new ArrayList<>();
        healthcenters.add(healthCenter);
        healthcenters.add(healthCenter2);
        healthcenters.add(healthCenter3);

        ArrayList<String> strings = CustomerHelper.getCustomerIds(independants,healthcenters);
        assertNotNull(strings);
        assertEquals(6,strings.size());
    }
}
