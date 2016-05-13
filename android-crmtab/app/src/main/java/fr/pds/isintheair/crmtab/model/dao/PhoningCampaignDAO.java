package fr.pds.isintheair.crmtab.model.dao;

import com.raizlabs.android.dbflow.sql.language.Select;

import fr.pds.isintheair.crmtab.model.entity.PhoningCampaign;
import fr.pds.isintheair.crmtab.model.entity.PhoningCampaign_Table;

/**
 * Created by tlacouque on 11/04/2016.
 * Class used to return a phoning campaign which has been stopped
 */
public class PhoningCampaignDAO {

    /**
     * Method that return a phoning campaign which has been stopped.
     * @return
     */
    public static PhoningCampaign getStoppedPhoningCampaign() {
        return new Select().from(PhoningCampaign.class)
                .where(PhoningCampaign_Table.statut.notEq(PhoningCampaign.STATE_ENDED)).querySingle();
    }
}
