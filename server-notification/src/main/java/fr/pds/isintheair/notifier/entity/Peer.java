package fr.pds.isintheair.notifier.entity;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class Peer<L, R> {

    public L tablet;
    public R phone;

    @Override
    public int hashCode () {
        return tablet.hashCode() ^ phone.hashCode();
    }
}