package miage.pds.api.tlacouque.uc.phoning.campaign.dao;

import miage.pds.api.mbalabascarin.uc.mock.contact.model.Contact;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import java.util.List;

/**
 * Created by tlacouque on 27/03/2016.
 */
public class ContactDAO extends BasicDAO<Contact,ObjectId> {
    public ContactDAO( Datastore ds) {
        super(ds);
    }

    /**
     * Return a list of contact from the db without all lines
     * where the clientId is equals to the id pass in parameter
     * @return List<Independant>
     */
    public List<Contact> findAllWithCustomerId(String id) {
        return getDatastore().createQuery(Contact.class).filter("clientId =",Long.valueOf(id)).asList();
    }
}
