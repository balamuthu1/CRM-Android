package fr.pds.isintheair.notifier.entity;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 01/24/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class Message {
    private MessageInfo messageInfo;
    private SessionInfo sessionInfo;

    protected Message (Builder messageBuilder) {
        this.messageInfo = messageBuilder.messageInfo;
        this.sessionInfo = messageBuilder.sessionInfo;
    }

    public MessageInfo getMessageInfo () {
        return messageInfo;
    }

    public SessionInfo getSessionInfo () {
        return sessionInfo;
    }

    public abstract static class Builder<T extends Builder<T>> {
        public MessageInfo messageInfo;
        public SessionInfo sessionInfo;

        protected abstract T getThis ();

        public T addMessageMeta (MessageInfo messageInfo) {
            this.messageInfo = messageInfo;
            return getThis();
        }

        public T addSessionInfo (SessionInfo sessionInfo) {
            this.sessionInfo = sessionInfo;
            return getThis();
        }

        public Message build () {
            return new Message(this);
        }
    }
}