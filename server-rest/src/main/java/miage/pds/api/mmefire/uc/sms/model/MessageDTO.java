package miage.pds.api.mmefire.uc.sms.model;

import java.util.Date;



public class MessageDTO {

	public long id;
	public String content;
	public String sender;
	public String recipient;
	public Date date;

	public MessageDTO() {
	}

	public MessageDTO(String id ,String content, String sender, String recipient, Date date) {
		this.content = content;
		this.sender = sender;
		this.recipient = recipient;
		this.date = date;
	}
}
