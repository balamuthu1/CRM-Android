package miage.pds.api.tlacouque.uc.phoning.campaign.dao;

import miage.pds.api.tlacouque.uc.phoning.campaign.entity.ContactCampaign;
import miage.pds.api.tlacouque.uc.phoning.campaign.entity.PhoningCampaign;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

/**
 * Created by tlacouque on 08/04/2016.
 * Class used to save a phoning campaign
 */
public class PhoningCampaignDAO extends BasicDAO<PhoningCampaign,ObjectId> {
    public PhoningCampaignDAO( Datastore ds) {
        super(ds);
    }
}
