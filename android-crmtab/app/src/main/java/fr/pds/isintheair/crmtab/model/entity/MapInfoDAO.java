package fr.pds.isintheair.crmtab.model.entity;

import com.raizlabs.android.dbflow.sql.language.Select;


/**
 * Created by tlacouque on 09/03/2016.
 * Class used to know if there is an instance of a mapInfo on the device
 */
public class MapInfoDAO {

    /**
     * Method used to know if there is an instance of mapinfo which has the siretNumber in parameter.
     * if there is a mapInfo, so i might have a map on the device.
     * @param siretNumber
     * @return boolean, true if there is a mapinfo, false if not.
     */
    public static boolean isMapInfoExist(long siretNumber) {
        return new Select().from(MapInfo.class).where(MapInfo_Table.siretNumber.eq(siretNumber)).hasData();
    }

    /**
     * Return a map info which have the current siret number.
     * @param siretNumber
     * @return
     */
    public static MapInfo getMapInfo(long siretNumber) {
        return new Select().from(MapInfo.class).where(MapInfo_Table.siretNumber.eq(siretNumber)).querySingle();
    }
}
