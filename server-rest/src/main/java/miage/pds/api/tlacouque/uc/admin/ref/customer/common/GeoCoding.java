package miage.pds.api.tlacouque.uc.admin.ref.customer.common;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import miage.pds.api.tlacouque.uc.admin.ref.customer.entities.Customer;

/**
 * Created by tlacouque on 01/02/2016.
 * Class used to return the lat and long for a customer by using google geocoding service/api.
 */
public class GeoCoding {

    public static String API_KEY = "AIzaSyAYHy7wBNArP5_53EEVMVuNq6uqmJhK-pg";

    /**
     * Method called to set Lat and Long for a customer
     * @param customer
     */
    public static void getLocation(Customer customer) {

        try {

            GeoApiContext context = new GeoApiContext().setApiKey(API_KEY);
          //  Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress("proxy.inside.esiag.info",3128));

            //context.setProxy(proxy);
            GeocodingResult[] results =  GeocodingApi.geocode(context,
                    customer.getAdress()).await();
            customer.setLattitude(results[0].geometry.location.lat);
            customer.setLongitude(results[0].geometry.location.lng);
        } catch (Exception e) {
            System.out.println("error");
        }

    }



}
