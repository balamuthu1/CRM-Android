package miage.pds.api.mmefire.uc.sms.dao;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.lang.String;
import java.util.Date;



@Entity
public class Message {

	@Id
	public String id;
	public String content;
	public String sender;
	public String recipient;
	public boolean statut;
	public Date date;

	public Message() {
		super();
	}

	public Message(String id, String content, String sender, String recipient, Date date) {
		this.content = content;
		this.sender = sender;
		this.recipient = recipient;
		this.date = date;
	}
}
