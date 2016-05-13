package miage.pds.api.mmefire.uc.sms.send.receive.dao;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Date;

/**
 * Created by Maimouna MEFIRE on 22/03/2016.
 */
@Entity
public class Message {

    @Id
    public String id;
    public String content;
    public String sender;
    public String recipient;
    public boolean statut;
    public Date date;

    public Message(String content, long id, String sender, String recipient) {
        super();
    }

    public Message(String id, String content, String sender, String recipient, Date date) {
        this.content = content;
        this.sender = sender;
        this.recipient = recipient;
        this.date = date;
    }
}



