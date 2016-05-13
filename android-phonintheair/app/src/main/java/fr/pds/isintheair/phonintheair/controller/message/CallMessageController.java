package fr.pds.isintheair.phonintheair.controller.message;

import fr.pds.isintheair.phonintheair.helper.CallHelper;
import fr.pds.isintheair.phonintheair.helper.SharedPreferencesHelper;
import fr.pds.isintheair.phonintheair.model.entity.Call;
import fr.pds.isintheair.phonintheair.model.entity.CallMessage;
import fr.pds.isintheair.phonintheair.model.entity.DeviceType;
import fr.pds.isintheair.phonintheair.model.entity.MessageInfo;
import fr.pds.isintheair.phonintheair.model.entity.MessageType;
import fr.pds.isintheair.phonintheair.model.entity.NotificationType;
import fr.pds.isintheair.phonintheair.model.entity.SessionInfo;
import fr.pds.isintheair.phonintheair.model.websocket.WebSocketConnectionHandlerSingleton;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class CallMessageController {
    public static void handleMessage(CallMessage message) {
        switch (message.getMessageInfo().getMessageType()) {
            case CALL:
                CallHelper.call(message.getCall().getPhoneNumber());
                SharedPreferencesHelper.writeString("lastMessage", message.getMessageInfo().getMessageType().toString());
                break;
            case CALL_ENDED:
                CallHelper.endCall();
                break;
        }
    }

    public static void sendCallHookMessage() {
        Integer     userId      = SharedPreferencesHelper.readInteger("userId", 0);
        MessageInfo messageInfo = new MessageInfo.Builder().addMessageType(MessageType.CALL_HOOKED).build();
        SessionInfo sessionInfo = new SessionInfo.Builder().addDeviceType(DeviceType.PHONE).addNotificationType(NotificationType.CALL).addUserId(userId).build();
        CallMessage message     = new CallMessage.Builder().addMessageInfo(messageInfo).addSessionInfo(sessionInfo).build();

        WebSocketConnectionHandlerSingleton.getInstance().sendMessage(message);
    }

    public static void sendCallReceivedMessage(String phoneNumber) {
        Integer     userId      = SharedPreferencesHelper.readInteger("userId", 0);
        MessageInfo messageInfo = new MessageInfo.Builder().addMessageType(MessageType.CALL_RECEIVED).build();
        SessionInfo sessionInfo = new SessionInfo.Builder().addDeviceType(DeviceType.PHONE).addNotificationType(NotificationType.CALL).addUserId(userId).build();
        Call        call        = new Call(phoneNumber);
        CallMessage message     = new CallMessage.Builder().addMessageInfo(messageInfo).addSessionInfo(sessionInfo).addCall(call).build();

        WebSocketConnectionHandlerSingleton.getInstance().sendMessage(message);
    }

    public static void sendCallPassedMessage(String phoneNumber) {
        Integer     userId      = SharedPreferencesHelper.readInteger("userId", 0);
        MessageInfo messageInfo = new MessageInfo.Builder().addMessageType(MessageType.CALL_PASSED).build();
        SessionInfo sessionInfo = new SessionInfo.Builder().addDeviceType(DeviceType.PHONE).addNotificationType(NotificationType.CALL).addUserId(userId).build();
        Call        call        = new Call(phoneNumber);
        CallMessage message     = new CallMessage.Builder().addMessageInfo(messageInfo).addSessionInfo(sessionInfo).addCall(call).build();

        WebSocketConnectionHandlerSingleton.getInstance().sendMessage(message);
    }

    public static void sendEndCallMessage() {
        Integer     userId      = SharedPreferencesHelper.readInteger("userId", 0);
        MessageInfo messageInfo = new MessageInfo.Builder().addMessageType(MessageType.CALL_ENDED).build();
        SessionInfo sessionInfo = new SessionInfo.Builder().addDeviceType(DeviceType.PHONE).addNotificationType(NotificationType.CALL).addUserId(userId).build();
        CallMessage message     = new CallMessage.Builder().addMessageInfo(messageInfo).addSessionInfo(sessionInfo).build();

        WebSocketConnectionHandlerSingleton.getInstance().sendMessage(message);
    }

    public static void sendRegisterMessage() {
        Integer     userId      = SharedPreferencesHelper.readInteger("userId", 0);
        MessageInfo messageInfo = new MessageInfo.Builder().addMessageType(MessageType.REGISTER).build();
        SessionInfo sessionInfo = new SessionInfo.Builder().addDeviceType(DeviceType.PHONE).addNotificationType(NotificationType.CALL).addUserId(userId).build();
        CallMessage message     = new CallMessage.Builder().addMessageInfo(messageInfo).addSessionInfo(sessionInfo).build();

        WebSocketConnectionHandlerSingleton.getInstance().sendMessage(message);
    }
}
