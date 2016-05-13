package fr.pds.isintheair.crmtab.controller.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import fr.pds.isintheair.crmtab.helper.CheckInternetConnexion;
import fr.pds.isintheair.crmtab.view.fragment.DetailFragmentNetworkInterface;

/**
 * Created by tlacouque on 02/03/2016.
 * Used to detect if the network connectivity change
 */
public class NetworkReceiver extends BroadcastReceiver {

    DetailFragmentNetworkInterface fragmentNetworkInterface;
    public boolean networkAvailable;

    public NetworkReceiver(DetailFragmentNetworkInterface fragment) {
        fragmentNetworkInterface = fragment;
    }

    public NetworkReceiver() {
    }

    /**
     * Called when internet connexion changed, it will change the fragment.
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if(CheckInternetConnexion.isNetworkAvailable(context)) {
            fragmentNetworkInterface.initMap();
            networkAvailable = true;
        } else {
            fragmentNetworkInterface.initMap();
            networkAvailable = false;
        }
    }
}
