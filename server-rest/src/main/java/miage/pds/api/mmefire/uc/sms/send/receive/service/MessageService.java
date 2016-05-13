package miage.pds.api.mmefire.uc.sms.send.receive.service;

import miage.pds.api.mmefire.uc.sms.send.receive.dao.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import miage.pds.api.mmefire.uc.sms.send.receive.dao.MessageDao;
import miage.pds.api.mmefire.uc.sms.send.receive.model.MessageDto;

/**
 * Created by Maimouna MEFIRE on 22/03/2016.
 */
public class MessageService {

    private MessageDao messageDAO;

    private static final Logger logger = LoggerFactory
            .getLogger(MessageService.class.getName());

    private static MessageService instance;

    /**
     *
     */
    public MessageService() {
        logger.debug("Crï¿½ation du singleton MessageService");
        messageDAO = MessageDao.getIntance();
    }

    /**
     * Retourne l'instance unique pour ne plus creer plusieurs objet
     *
     * @return
     */
    public static MessageService getInstance() {
        if (instance == null) {
            instance = new MessageService();
        }
        return instance;
    }
    // creer un message en BD
    public boolean addMessage(MessageDto messageDto) {

        Message message = new Message(messageDto.content , messageDto.id, messageDto.sender,messageDto.recipient );


        return messageDAO.addMessage(message);

    }
    // permet de recuperer un liste de message ï¿½ partir de l'id du contact passe en parametre
    public List<Message> getMessagesForContact(String contactID){

        return messageDAO.getAllMessageForContact(contactID);

    }
    // suuprimer un message ï¿½ partir de son identifiant
    public boolean deleteMessageById(String messageID){
        return messageDAO.deleteMesageById(messageID);
    }
}


