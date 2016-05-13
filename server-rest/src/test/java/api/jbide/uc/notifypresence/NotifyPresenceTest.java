package api.jbide.uc.notifypresence;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import miage.pds.MongoDatastoreConfig;
import miage.pds.api.common.LoggingDao.LoggingDAO;
import miage.pds.api.common.model.ClockinObject;
import miage.pds.api.common.model.User;
import miage.pds.api.jbide.uc.notifypresence.dao.TagDAO;
import miage.pds.api.jbide.uc.notifypresence.model.Tag;

public class NotifyPresenceTest {

	@Test
	public void testTagCreation(){

		TagDAO  TagDao = new TagDAO(MongoDatastoreConfig.getDataStore());
		Tag tag = new Tag();
		tag.setId("a");
        tag.setLocation("b");
        TagDao.addTag(tag);
		assertEquals(true, TagDao.checkTag("a").getLocation().equals("b"));
		
		TagDao.deleteTag("a");

	}
	
	@Test
	public void updateUserLocation(){

		TagDAO  TagDao = new TagDAO(MongoDatastoreConfig.getDataStore());
		Tag tag = new Tag();
		tag.setId("a");
        tag.setLocation("b");
        TagDao.addTag(tag);
		
		String id = "jksrfio123454603KLJKFGJKEZN8776jlkhugygsgjkz";
		LoggingDAO dao = new LoggingDAO(MongoDatastoreConfig.getDataStore());
		User user = new User();
		user.setId(id);
		dao.addUser(user);
		
		user = TagDao.updateLocation(new ClockinObject(user, "a"), tag.getLocation());
		
		assertEquals(true, user.getLocation().equals("b"));
		
		dao.delete(user);
		TagDao.deleteTag("a");

	}
	
	
}
