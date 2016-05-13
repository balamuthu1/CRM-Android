package miage.pds.api.jbide.uc.notifypresence.dao;

import miage.pds.api.common.model.ClockinObject;
import miage.pds.api.common.model.User;
import miage.pds.api.jbide.uc.notifypresence.model.Tag;
import miage.pds.api.jbide.uc.registercall.model.Cra;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jbide on 30/03/2016.
 */

public class TagDAO extends BasicDAO<Cra, ObjectId> {

    private static final Logger logger = LoggerFactory.getLogger(TagDAO.class);

    public TagDAO (Datastore datastore) {
        super(datastore);
    }

    // Tag insertion
    public boolean addTag (Tag tag) {
        getDatastore().save(tag);
        return true;
    }

    //
    public Tag checkTag (String idtag) {
        return getDatastore().createQuery(Tag.class).disableValidation().field("id").equal(idtag).get();
    }

    public User updateLocation (ClockinObject cl, String location) {
        User user = getDatastore().createQuery(User.class).field("id").equal(cl.getUser().getId()).get();
        user.setLocation(location);//
        getDatastore().save(user);
        return user;

    }


    public boolean deleteTag (String idTag) {
        getDatastore().delete(getDatastore().createQuery(Tag.class).field("id").equal(idTag));
        return true;
    }


}
