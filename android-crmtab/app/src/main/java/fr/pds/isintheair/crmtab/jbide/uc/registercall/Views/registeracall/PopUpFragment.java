package fr.pds.isintheair.crmtab.jbide.uc.registercall.Views.registeracall;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import fr.pds.isintheair.crmtab.jbide.uc.registercall.Events.DisplayAddLogFragmentEvent;
import fr.pds.isintheair.crmtab.jbide.uc.registercall.Events.DisplayPopUpFragmentEvent;
import fr.pds.isintheair.crmtab.view.activity.MainActivity;

/**
 * Created by j-d on 18/12/2015.
 */

public class PopUpFragment extends DialogFragment {

    private static DisplayPopUpFragmentEvent callevent;
    int mNum;
    //private CallEndedEvent callevent;

    /**
     * Create a new instance of MyDialogFragment, with  the callEnded event params
     * as arguments.
     */
    public static PopUpFragment newInstance(DisplayPopUpFragmentEvent event) {
        PopUpFragment f = new PopUpFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        callevent = event;


        f.setArguments(args);

        return f;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       super.onCreateDialog(savedInstanceState);
        //two buttons yes and no
        return 	new AlertDialog.Builder(getActivity()).setTitle("Enregistrer dernier appel?").setMessage("Enregistrer dernier appel ?")
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //hide popup , addlog fragment  is below
                       // Constant.setPopUpDisplayed(false);
                        ((MainActivity)getActivity()).showaddlogfragment(new DisplayAddLogFragmentEvent(callevent.getCallEndedEvent(),false));

                    }
                }).setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                      //  Constant.setPopUpDisplayed(false);
                    }
                }).show();

    }

}
