package fr.pds.isintheair.crmtab.model.entity;

import android.os.Parcelable;

/**
 * Created by tlacouque on 12/12/2015.
 * Interface created to display health center and independant in the same list
 */


public interface Customer extends Parcelable{
    long getSiretNumber();
    String getName();
    String getAdress();
    double getLattitude();
    double getLongitude();

}
