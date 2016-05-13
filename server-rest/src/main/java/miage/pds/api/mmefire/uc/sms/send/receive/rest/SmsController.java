/*package miage.pds.api.mmefire.uc.sms.send.receive.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.util.List;

import miage.pds.api.mmefire.uc.sms.send.receive.dao.Message;
import miage.pds.api.mmefire.uc.sms.send.receive.model.MessageDto;
import miage.pds.api.mmefire.uc.sms.send.receive.service.MessageService;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Maimouna MEFIRE on 22/03/2016.
 */

/*@Controller
@RequestMapping (value = "/sms")//
=======

>>>>>>> dev
public class SmsController {

    boolean status = false;
    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);
    MessageService messageService = new MessageService();

    public SmsController() {
    }


    @RequestMapping(value = "/addSms", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Boolean addSms(@RequestBody MessageDto messageDto) {
        logger.info("Add new message");
        MessageService messageService = new MessageService();
        return messageService.addMessage(messageDto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Message> getMessagesForContact(@PathVariable String id) {
        logger.info("Start get Messages for contact.");
       return  messageService.getMessagesForContact(id);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    Boolean deleteMessageById(@PathVariable String id) {
        logger.info("Start delete Message by id.");
        return messageService.deleteMessageById(id);
    }
}
*/

