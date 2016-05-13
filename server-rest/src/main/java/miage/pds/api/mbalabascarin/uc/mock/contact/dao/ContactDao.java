package miage.pds.api.mbalabascarin.uc.mock.contact.dao;

import com.mongodb.DB;
import miage.pds.MongoDatastoreConfig;
import miage.pds.api.mbalabascarin.uc.mock.contact.model.Contact;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.List;

public class ContactDao {
	DB db;
	Long count;
	Morphia morphia;
	Datastore datastore;
	
	public ContactDao(){}
	
	public boolean ConnectDB(){		
		/**** Get database ****/
		// if database doesn't exists, MongoDB will create it for you
		//datastore = morphia.createDatastore(mongo, "crm");

		datastore = MongoDatastoreConfig.getDataStore();

		if(datastore != null){
			return true;
		}

		return false;

	}
	
	public Boolean createContacts(List<Contact> contacts){
		//check if connected to DB	
				if(ConnectDB()){
					for(Contact contact : contacts){
						//save report to DB
						datastore.save(contact);
					}
					return true;
				}		
		return false;
	}
	public Boolean createContact(Contact contact){
		//check if connected to DB	
				if(ConnectDB()){
						datastore.save(contact);
					
					return true;
				}		
		return false;
	}
}
