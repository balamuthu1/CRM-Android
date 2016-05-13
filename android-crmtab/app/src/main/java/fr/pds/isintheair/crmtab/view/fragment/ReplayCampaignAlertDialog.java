package fr.pds.isintheair.crmtab.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.model.entity.CustomerType;

/**
 * Created by tlacouque on 11/04/2016.
 */
public class ReplayCampaignAlertDialog  extends DialogFragment {


    AlertPositiveListener alertPositiveListener;
    /**
     * This is the OK button listener for the alert dialog,
     * which in turn invokes the method onNegativeClick
     * of the hosting activity which is supposed to implement it
     */
    DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            AlertDialog alert    = (AlertDialog) dialog;

            alertPositiveListener.onPositiveClick();
        }
    };

    /**
     * This is the cancel button listener for the alert dialog,
     * which in turn invokes the method onPositiveClick(position)
     * of the hosting activity which is supposed to implement it
     */
    DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            AlertDialog alert    = (AlertDialog) dialog;

            alertPositiveListener.onNegativeClick();
        }
    };


    /**
     * This is a callback method which will be executed
     * on creating this fragment
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        /** Getting the arguments passed to this fragment */
        Bundle bundle   = getArguments();
        // int    position = bundle.getInt("position");

        /** Creating a builder for the alert dialog window */
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());

        /** Setting a title for the window */
        b.setTitle(R.string.reload_campaign_alert_dialog_title);


        /** Setting items to the alert dialog */
        // b.setSingleChoiceItems(CreateCustomerAlertDialog.customerTypeList, position, null);

        /** Setting a positive button and its listener */
        b.setPositiveButton(R.string.customer_list_dialog_validate, positiveListener);

        /** Setting a positive button and its listener */
        b.setNegativeButton(R.string.customer_list_dialog_cancel, negativeListener);

        /** Creating the alert dialog window using the builder class */
        AlertDialog d = b.create();

        /** Return the alert dialog window */
        return d;
    }

    public void setAlertPositiveListener(AlertPositiveListener alertPositiveListener) {
        this.alertPositiveListener = alertPositiveListener;
    }

    public interface AlertPositiveListener {
        public void onPositiveClick();
        public void onNegativeClick();
    }
}



