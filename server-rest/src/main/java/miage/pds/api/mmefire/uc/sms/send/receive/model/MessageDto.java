package miage.pds.api.mmefire.uc.sms.send.receive.model;

import java.util.Date;

/**
 * Created by Maimouna MEFIRE on 22/03/2016.
 */
public class MessageDto {

    public long id;
    public String content;
    public String sender;
    public String recipient;
    public Date date;

    public MessageDto() {
    }

    public MessageDto(String id ,String content, String sender, String recipient, Date date) {
        this.content = content;
        this.sender = sender;
        this.recipient = recipient;
        this.date = date;
    }
}
