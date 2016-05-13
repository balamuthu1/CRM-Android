package fr.pds.isintheair.crmtab.model.entity;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class CallMessage extends Message {
    private Call call;

    private CallMessage(Builder builder) {
        super(builder);

        this.call = builder.call;
    }

    public Call getCall() {
        return call;
    }

    public static class Builder extends Message.Builder<Builder> {
        public Call call;

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder addCall(Call call) {
            this.call = call;
            return this;
        }

        public CallMessage build() {
            return new CallMessage(this);
        }
    }
}