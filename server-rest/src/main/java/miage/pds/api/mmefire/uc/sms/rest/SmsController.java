package miage.pds.api.mmefire.uc.sms.rest;

import miage.pds.api.mmefire.uc.sms.dao.Message;
import miage.pds.api.mmefire.uc.sms.model.MessageDTO;
import miage.pds.api.mmefire.uc.sms.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// fait le traitement � partir de l'url passe en parammetre
@Controller
@RequestMapping(value = "/sms")// defini l'url
public class SmsController {

    boolean status = false;
    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);
    MessageService messageService = MessageService.getInstance();

    public SmsController () {
    }


    @RequestMapping(value = "/addSms", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Boolean addSms (@RequestBody MessageDTO messageDto) {
        logger.info("Add new message");
        //return messageService.addMessage(messageDto);

        return true;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Message> getMessagesForContact (@PathVariable String id) {
        logger.info("Start get Messages.");
        return messageService.getMessagesForContact(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public
    @ResponseBody
    Boolean deleteMessageById (@PathVariable String id) {
        logger.info("Start delete Message by id.");
        //  return messageService.deleteMessageById(id);

        return true;
    }


}
