package fr.pds.isintheair.phonintheair.model.entity;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class MessageInfo {
    private String      errorDescription;
    private MessageType messageType;
    private Integer     statusCode;

    public MessageInfo(Builder builder) {
        this.errorDescription = builder.errorDescription;
        this.messageType = builder.messageType;
        this.statusCode = builder.statusCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public static class Builder {
        private String      errorDescription;
        private MessageType messageType;
        private Integer     statusCode;

        public Builder addErrorDescription(String errorDescription) {
            this.errorDescription = errorDescription;

            return this;
        }

        public Builder addMessageType(MessageType messageType) {
            this.messageType = messageType;

            return this;
        }

        public Builder addStatusCode(Integer statusCode) {
            this.statusCode = statusCode;

            return this;
        }

        public MessageInfo build() {
            return new MessageInfo(this);
        }
    }
}
