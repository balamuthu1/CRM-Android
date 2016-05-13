package fr.pds.isintheair.crmtab.controller.message;

import android.app.Activity;
import android.content.Intent;

import fr.pds.isintheair.crmtab.CrmTabApplication;
import fr.pds.isintheair.crmtab.controller.bus.BusHandlerSingleton;
import fr.pds.isintheair.crmtab.controller.bus.event.PhoneCallEndedEvent;
import fr.pds.isintheair.crmtab.controller.bus.event.PhoneCallFailedEvent;
import fr.pds.isintheair.crmtab.controller.bus.event.PhoneCallHookedEvent;
import fr.pds.isintheair.crmtab.model.dao.UserDAO;
import fr.pds.isintheair.crmtab.model.entity.Call;
import fr.pds.isintheair.crmtab.model.entity.CallMessage;
import fr.pds.isintheair.crmtab.model.entity.DeviceType;
import fr.pds.isintheair.crmtab.model.entity.MessageInfo;
import fr.pds.isintheair.crmtab.model.entity.MessageType;
import fr.pds.isintheair.crmtab.model.entity.NotificationType;
import fr.pds.isintheair.crmtab.model.entity.SessionInfo;
import fr.pds.isintheair.crmtab.model.websocket.WebSocketConnectionHandlerSingleton;
import fr.pds.isintheair.crmtab.view.activity.CallActivity;

public class CallMessageController {
    public static void handleMessage(CallMessage message) {
        switch (message.getMessageInfo().getMessageType()) {
            case CALL:
                CallMessageController.call(message.getCall().getPhoneNumber());
                break;
            case CALL_ENDED:
                CallMessageController.endCall();
                break;
            case CALL_FAILED:
                CallMessageController.callFailed();
                break;
            case CALL_HOOKED:
                CallMessageController.callHooked();
                break;
            case CALL_PASSED:
            case CALL_RECEIVED:
                String phoneNumber = null;

                if (message.getCall() != null)
                    phoneNumber = message.getCall().getPhoneNumber();

                CallMessageController.notifyCallFromPhone(phoneNumber, message.getMessageInfo().getMessageType());
                break;
        }
    }

    public static void call(String phoneNumber) {
        CallMessageController.sendCallMessage(phoneNumber);
        startCallActivity(phoneNumber, MessageType.CALL_PASSED);
    }

    public static void callFailed() {
        Activity callActivity = CallActivity.instance;

        if (callActivity != null)
            callActivity.finish();

        BusHandlerSingleton.getInstance().getBus().post(new PhoneCallFailedEvent(""));
    }

    public static void callHooked() {
        BusHandlerSingleton.getInstance().getBus().post(new PhoneCallHookedEvent());
    }

    public static void endCall() {
        BusHandlerSingleton.getInstance().getBus().post(new PhoneCallEndedEvent());
    }

    public static void notifyCallFromPhone(String phoneNumber, MessageType messageType) {
        startCallActivity(phoneNumber, messageType);
    }

    private static void startCallActivity(String phoneNumber, MessageType messageType) {
        Intent intent = new Intent();

        intent.setClass(CrmTabApplication.context, CallActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("phoneNumber", phoneNumber);
        intent.putExtra("messageType", messageType);

        CrmTabApplication.context.startActivity(intent);
    }

    public static void sendCallMessage(String phoneNumber) {
        Integer     userId      = UserDAO.getCurrentUser().getEmail().hashCode();
        MessageInfo messageInfo = new MessageInfo.Builder().addMessageType(MessageType.CALL).build();
        SessionInfo sessionInfo = new SessionInfo.Builder().addDeviceType(DeviceType.TABLET).addNotificationType(NotificationType.CALL).addUserId(userId).build();
        Call        call        = new Call(phoneNumber);
        CallMessage message     = new CallMessage.Builder().addMessageInfo(messageInfo).addSessionInfo(sessionInfo).addCall(call).build();

        WebSocketConnectionHandlerSingleton.getInstance().sendMessage(message);
    }

    public static void sendEndCallMessage() {
        Integer     userId      = UserDAO.getCurrentUser().getEmail().hashCode();
        MessageInfo messageInfo = new MessageInfo.Builder().addMessageType(MessageType.CALL_ENDED).build();
        SessionInfo sessionInfo = new SessionInfo.Builder().addDeviceType(DeviceType.TABLET).addNotificationType(NotificationType.CALL).addUserId(userId).build();
        CallMessage message     = new CallMessage.Builder().addMessageInfo(messageInfo).addSessionInfo(sessionInfo).build();

        WebSocketConnectionHandlerSingleton.getInstance().sendMessage(message);
    }

    public static void sendRegisterMessage() {
        Integer     userId      = UserDAO.getCurrentUser().getEmail().hashCode();
        MessageInfo messageInfo = new MessageInfo.Builder().addMessageType(MessageType.REGISTER).build();
        SessionInfo sessionInfo = new SessionInfo.Builder().addDeviceType(DeviceType.TABLET).addNotificationType(NotificationType.CALL).addUserId(userId).build();
        CallMessage message     = new CallMessage.Builder().addMessageInfo(messageInfo).addSessionInfo(sessionInfo).build();

        WebSocketConnectionHandlerSingleton.getInstance().sendMessage(message);
    }
}
