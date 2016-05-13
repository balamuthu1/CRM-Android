package miage.pds.api.mbalabascarin.uc.editcrv.dao;

import com.mongodb.DB;
import miage.pds.MongoDatastoreConfig;
import miage.pds.api.mbalabascarin.uc.editcrv.model.Report;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.ArrayList;
import java.util.List;

public class CrvMorphiaDao {
	DB db;
	Long count;
	Morphia morphia;
	Datastore datastore;

	public CrvMorphiaDao(){}

	public boolean ConnectDB(){		
		

		/**** Get database ****/
		// if database doesn't exists, MongoDB will create it for you
		//datastore = morphia.createDatastore(mongo, "crm");

		datastore = MongoDatastoreConfig.getDataStore();

		if(datastore != null){
			return true;
		}

		return false;

	}

	//insert or update a report 
	public boolean createOrModifyCrv(Report report){

		//check if connected to DB
		if(ConnectDB()){
			if(report.getId().equals("")){
				report.setId(Long.toString(datastore.getCount(Report.class)));
			}
			//save report to DB
			datastore.save(report);
			return true;
		}
		return false;

	}

	//get all reports for a specific client
	public List<Report> getAllReportsForClient(String id){
		List<Report> list = new ArrayList<Report>();
		if(ConnectDB()){
			list = datastore.createQuery(Report.class)
					.filter("client =", id)
					.asList();
		}


		return list;
	}

	//delete a report by given id
	public Boolean deleteReportById(String id){
		Report report;
		if(ConnectDB()){
			try {
				report = datastore.find(Report.class).field("_id").equal(id).get();
				datastore.delete(report);
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.getMessage();
			}

		}
		return false;
	}

}
