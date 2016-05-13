package miage.pds.api.jbide.uc.notifypresence;

import miage.pds.MongoDatastoreConfig;
import miage.pds.api.common.model.ClockinObject;
import miage.pds.api.jbide.uc.notifypresence.dao.TagDAO;
import miage.pds.api.jbide.uc.notifypresence.model.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class NotifyPresenceController {

    private static final Logger logger = LoggerFactory.getLogger(NotifyPresenceController.class);
    boolean status = false;
    TagDAO  TagDao = new TagDAO(MongoDatastoreConfig.getDataStore());

    public NotifyPresenceController () {
        TagDao.getDatastore().getCollection(Tag.class).drop();
        Tag tag = new Tag();
        tag.setId("entree");
        tag.setLocation("Entrée Bâtiment");
        TagDao.addTag(tag);
        tag.setId("poste");
        tag.setLocation("A son poste de travail");
        TagDao.addTag(tag);
        tag.setId("cafetaria");
        tag.setLocation("Cafétaria");
        TagDao.addTag(tag);
        tag.setId("etage");
        tag.setLocation("Bât 1 , Etage 2");
        TagDao.addTag(tag);

        //TagDao.addTag(new Tag());
    }

    @RequestMapping(value = "/notifypresence/clockin", method = RequestMethod.POST)
    @ResponseBody
    public ClockinObject clock_in (@RequestBody ClockinObject clockin) {

        Tag    tag  = TagDao.checkTag(clockin.getTagId());
        String time = clockin.getTime();
        //change time to UTC + 2
        clockin.setTime(String.valueOf(Integer.parseInt(time.substring(0, time.indexOf(":"))) + 2) + ":" + time.substring(time.indexOf(":") + 1));
        if (tag != null) {    	
        	clockin.setUser(TagDao.updateLocation(clockin, tag.getLocation()));
            return clockin;
        }
        else return null;
    }

    @RequestMapping(value = "/notifypresence/addnewtag", method = RequestMethod.POST)
    @ResponseBody
    public Boolean addNewTag (@RequestBody Tag tag) {

        logger.info("ADD NEW TAG");
        TagDao.addTag(tag);
        return true;
    }


}

