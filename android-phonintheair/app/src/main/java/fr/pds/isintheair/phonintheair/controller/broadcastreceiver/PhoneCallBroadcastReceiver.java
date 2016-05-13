package fr.pds.isintheair.phonintheair.controller.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import fr.pds.isintheair.phonintheair.controller.message.CallMessageController;
import fr.pds.isintheair.phonintheair.helper.SharedPreferencesHelper;
import fr.pds.isintheair.phonintheair.model.entity.MessageType;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 01/24/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class PhoneCallBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String lastMessage   = SharedPreferencesHelper.readString("lastMessage", "");
        String previousState = SharedPreferencesHelper.readString("previousTelephonyState", "");
        String state         = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);

        if (state != null) {
            String phoneNumber = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                CallMessageController.sendCallReceivedMessage(phoneNumber);
            }

            else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                CallMessageController.sendEndCallMessage();
            }

            else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK) && previousState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                CallMessageController.sendCallHookMessage();
            }

            else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK) && previousState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                if (!lastMessage.equals(MessageType.CALL.toString()))
                    CallMessageController.sendCallPassedMessage(phoneNumber);
            }

            SharedPreferencesHelper.writeString("lastMessage", "");
            SharedPreferencesHelper.writeString("previousTelephonyState", state);
        }
    }
}
