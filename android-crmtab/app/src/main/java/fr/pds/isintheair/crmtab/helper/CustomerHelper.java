package fr.pds.isintheair.crmtab.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import fr.pds.isintheair.crmtab.model.entity.Contact;
import fr.pds.isintheair.crmtab.model.entity.Customer;
import fr.pds.isintheair.crmtab.model.entity.HealthCenter;
import fr.pds.isintheair.crmtab.model.entity.Independant;

/**
 * Created by tlacouque on 27/03/2016.
 */
public class CustomerHelper {

    /**
     * Return a customer from a list search by is name
     * @param name
     * @param customers
     * @return
     */
    public static Customer getCustomerFromName(String name,List<Customer> customers) {
        Customer customerReturn = null;
        for(Customer customerfor : customers) {
            if(name.equals(customerfor.getName())) {
                customerReturn = customerfor;
            }
        }
        return customerReturn;
    }

    /**
     * Add all independants and healthcenter into two list pass in parameter from the list
     * of customer pass in parameter too
     * @param customers
     * @param independants
     * @param healthCenters
     */
    public static void addHCIndependantIntoList(List<Customer> customers,List<Independant>independants
                                             , List<HealthCenter> healthCenters) {
        for(Customer customer:customers) {
            if(customer instanceof HealthCenter) {
                healthCenters.add((HealthCenter) customer);
            } else if(customer instanceof Independant)  {
                independants.add((Independant) customer);
            }
        }
    }

    /**
     * Get a  MAP of a customer (key) and his list of contact (value). Those two list are past in
     * parameter.
     * @param customers
     * @param contacts
     * @return
     */
    public static HashMap<Customer,List<Contact>> getCustomerContactsMap
            (List<Customer> customers,List<Contact> contacts) {
        HashMap<Customer,List<Contact>> customerListHashMap = new HashMap<Customer,List<Contact>>();

        for(Customer customer : customers) {
            List<Contact> customerContactList = new ArrayList<Contact>();
            for(Contact contact : contacts) {
                if(customer.getSiretNumber() == contact.clientId) {
                    customerContactList.add(contact);
                    customerListHashMap.put(customer,customerContactList);
                }
            }
        }

        return customerListHashMap;
    }

    /**
     * Return the list of customers name pass in parameter. Used to display it in list view.
     * @param customers
     * @return
     */
    public static List<String> getCustomersName(List<Customer> customers) {
        List<String> customersName = new ArrayList<>();
        for(Customer customer : customers) {
            customersName.add(customer.getName());
        }
        return customersName;
    }

    /**
     * Return the list of customers name with a contact name pass in parameter.
     * Used to display it in list view.
     * @param hashMap
     * @return
     */
    public static List<String> getCustomerContactName(HashMap<Customer,List<Contact>> hashMap) {
        List<String> customerContactName = new ArrayList<>();
        for(Customer customer : hashMap.keySet()) {
            for(Contact contact : hashMap.get(customer)) {
                customerContactName.add(customer.getName()+" : "+contact.contactName + " "+contact.contactFname);
            }
        }

        return customerContactName;
    }

    /**
     * Return a contact from a list search by is name
     * @param name
     * @param contacts
     * @return
     */
    public static Contact getContactFromName(String name,List<Contact> contacts) {
        Contact contactReturn = null;
        String[] str_array = name.split(" : ");
        name = str_array[1];
        for(Contact contact : contacts) {
            if(name.equals(contact.contactName + " "+contact.contactFname)) {
                contactReturn = contact;
            }
        }
        return contactReturn;
    }

    /**
     * Return a list of a customer id from lists pass in parameter
     * @param independants
     * @param healthCenters
     * @return
     */
    public static ArrayList<String> getCustomerIds(List<Independant> independants,List<HealthCenter> healthCenters) {
        ArrayList<String> customersId = new ArrayList<>();


        for(Independant independant : independants) {
            customersId.add(String.valueOf(independant.getSiretNumber()));
        }
        for(HealthCenter healthCenter : healthCenters) {
            customersId.add(String.valueOf(healthCenter.getSiretNumber()));
        }


        return customersId;
    }

    /**
     * Get a key (Customer) by is index.
     * @param currentCustomer
     * @param customerListHashMap
     * @return
     */
    public static Customer getCustomerByIndex(int currentCustomer,
                                       LinkedHashMap<Customer,List<Contact>> customerListHashMap) {
        List<Customer> customers =  new ArrayList<>(customerListHashMap.keySet());
        return customers.get(currentCustomer);
    }



}
