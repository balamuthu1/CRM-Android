package fr.pds.isintheair.notifier.controller;

import fr.pds.isintheair.notifier.entity.Call;
import fr.pds.isintheair.notifier.entity.CallMessage;
import fr.pds.isintheair.notifier.entity.MessageInfo;
import fr.pds.isintheair.notifier.entity.MessageType;
import fr.pds.isintheair.notifier.helper.JSONHelper;

import javax.websocket.Session;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class CallController {
    public static void call (Session phoneSession, String phoneNumber) {
        MessageInfo messageInfo = new MessageInfo.Builder().addMessageType(MessageType.CALL).build();
        Call        call        = new Call(phoneNumber);
        CallMessage callMessage = new CallMessage.Builder().addMessageMeta(messageInfo).addCall(call).build();

        phoneSession.getAsyncRemote().sendText(JSONHelper.serialize(callMessage, CallMessage.class));
    }

    public static void endCall (Session peerSession) {
        MessageInfo messageInfo = new MessageInfo.Builder().addMessageType(MessageType.CALL_ENDED).build();
        CallMessage callMessage = new CallMessage.Builder().addMessageMeta(messageInfo).build();

        peerSession.getAsyncRemote().sendText(JSONHelper.serialize(callMessage, CallMessage.class));
    }

    public static void notifyCallHooked (Session tabletSession) {
        MessageInfo messageInfo = new MessageInfo.Builder().addMessageType(MessageType.CALL_HOOKED).build();
        CallMessage callMessage = new CallMessage.Builder().addMessageMeta(messageInfo).build();

        tabletSession.getAsyncRemote().sendText(JSONHelper.serialize(callMessage, CallMessage.class));
    }

    public static void notifyCallPassed (Session tabletSession, String phoneNumber) {
        MessageInfo messageInfo = new MessageInfo.Builder().addMessageType(MessageType.CALL_PASSED).build();
        Call        call        = new Call(phoneNumber);
        CallMessage callMessage = new CallMessage.Builder().addMessageMeta(messageInfo).addCall(call).build();

        tabletSession.getAsyncRemote().sendText(JSONHelper.serialize(callMessage, CallMessage.class));
    }

    public static void notifyCallReceived (Session tabletSession, String phoneNumber) {
        MessageInfo messageInfo = new MessageInfo.Builder().addMessageType(MessageType.CALL_RECEIVED).build();
        Call        call        = new Call(phoneNumber);
        CallMessage callMessage = new CallMessage.Builder().addMessageMeta(messageInfo).addCall(call).build();

        tabletSession.getAsyncRemote().sendText(JSONHelper.serialize(callMessage, CallMessage.class));
    }
}
