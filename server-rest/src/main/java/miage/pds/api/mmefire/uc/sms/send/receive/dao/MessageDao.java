package miage.pds.api.mmefire.uc.sms.send.receive.dao;

import com.mongodb.DB;
import com.mongodb.MongoClient;

import miage.pds.api.mbalabascarin.uc.editcrv.dao.Config;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Maimouna MEFIRE on 22/03/2016.
 */
public class MessageDao {DB db;
    Long count;
    Morphia morphia;
    Datastore datastore;

    public MessageDao(){}

    public boolean ConnectDB(){
        /**** Connect to MongoDB ****/
        MongoClient mongo;

        try {

            mongo = new MongoClient(Config.URL, Config.PORT);
            morphia = new Morphia();

            /**** Get database ****/
            // if database doesn't exists, MongoDB will create it for you
            datastore = morphia.createDatastore(mongo, "crm");
            return true;

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;

    }

    //insert a message
    public boolean addMessage(Message message){

        //check if connected to DB
        if(ConnectDB()){
            datastore.save(message);
            return true;
        }
        return false;
    }

    //get all reports for a specific client
    public List<Message> getAllMessageForContact(String id){
        List<Message> list = new ArrayList<Message>();
        if(ConnectDB()){
            list = datastore.createQuery(Message.class)
                    .filter("contact =", id)
                    .asList();
        }
        return list;
    }

    //delete a message by given id
    public Boolean deleteMesageById(String id){
        Message message;
        if(ConnectDB()){
            try {
                message = datastore.find(Message.class).field("_id").equal(id).get();
                datastore.delete(message);
                return true;
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return false;
    }

    public static MessageDao getIntance() {
        // TODO Auto-generated method stub
        return null;
    }
}
