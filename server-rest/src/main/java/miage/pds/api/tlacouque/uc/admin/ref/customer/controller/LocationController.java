package miage.pds.api.tlacouque.uc.admin.ref.customer.controller;

import miage.pds.api.tlacouque.uc.admin.ref.customer.common.GeoCoding;
import miage.pds.api.tlacouque.uc.admin.ref.customer.entities.Customer;
import miage.pds.api.tlacouque.uc.admin.ref.customer.message.ResponseRestCustomer;

/**
 * Created by tlacouque on 13/02/2016.
 * Controller used to controle the location of a customer
 */
public class LocationController {

    /**
     * Method called to  get a location of a customer and set the value in it and in the
     * responseRestCustomer object
     * @param responseRestCustomer
     * @param customer
     */
    public static void getLocation(ResponseRestCustomer responseRestCustomer, Customer customer) {
        try {
            GeoCoding.getLocation(customer);
            responseRestCustomer.setLat(customer.getLattitude());
            responseRestCustomer.setLng(customer.getLongitude());
        }catch (Exception e) {
            responseRestCustomer.setLat(0);
            responseRestCustomer.setLng(0);
        }
    }

}
