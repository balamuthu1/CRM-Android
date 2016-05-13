package miage.pds.api.mmefire.uc.sms.service;

import miage.pds.api.mmefire.uc.sms.dao.Message;
import miage.pds.api.mmefire.uc.sms.dao.MessageDao;
import miage.pds.api.mmefire.uc.sms.model.MessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MessageService {

    private MessageDao messageDAO;

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class.getName());

    private static MessageService instance;

    /**
     *
     */
    private MessageService () {
        logger.debug("Cr�ation du singleton MessageService");
        //  messageDAO = MessageDao.getIntance();


    }

    /**
     * Retourne l'instance unique pour ne plus creer plusieurs objet
     *
     * @return
     */
    public static MessageService getInstance () {
        if (instance == null) {
            instance = new MessageService();
        }
        return instance;
    }

    // creer un message en BD
    public void addMessage (MessageDTO messageDto) {

       /*  Message message = new Message(messageDto.content, messageDto.sender, messageDto.recipient, messageDto.date);


        messageDAO.addMessage(message); */

    }

    // permet de recuperer un liste de message � partir de l'id du contact passe en parametre
    public List<Message> getMessagesForContact (String contactID) {

        return messageDAO.getAllMessageForContact(contactID);

    }

    // suuprimer un message � partir de son identifiant
    private boolean deleteMessageById (String messageID) {
        return messageDAO.deleteMesageById(messageID);
    }
}
