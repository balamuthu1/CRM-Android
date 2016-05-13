package api.mbalabascarin.uc.editcrv;

import miage.pds.api.mbalabascarin.uc.editcrv.dao.CrvMorphiaDao;
import miage.pds.api.mbalabascarin.uc.editcrv.model.Report;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by: Muthu
 * Comment author: Muthu
 */

//Commented to check jenkins configuration and some other jenkins plugins
//will be uncommented once jenkins is finalised


@RunWith(MockitoJUnitRunner.class)
public class CrvDaoTestClass {
	private CrvMorphiaDao CrvDaoObject;
	private Report report;

	@Before
	public void initDao(){
		System.out.println("Launching tests...");
		CrvDaoObject = new CrvMorphiaDao();
		report = new Report();
	}



	@Test
	public void testDaoConnection(){
		assertEquals(true, CrvDaoObject.ConnectDB());

	}



	@Test
	public void testDaoCreate(){
		//create mock
		//CrvDaoObject = new CrvMorphiaDao();



		report.setId("1");
		report.setClient("1");
		report.setComment("test comment");
		report.setCommercial("0778801708");
		report.setContact("1");
		report.setDate("02/01/2015");
		report.setSatisfaction("Oui");
		report.setVisit("1");


		assertEquals(true, CrvDaoObject.createOrModifyCrv(report));

	}
	@Test
	public void testDaoCreateError(){

		//testing report object without any values 
		//the method should return false which means no bugs and no insertion in DB
		assertFalse(CrvDaoObject.createOrModifyCrv(report));
	}

	@Test
	public void testDaoCreateWithSameId(){
		//create mock
		//CrvDaoObject = new CrvMorphiaDao();



		report.setId("1");
		report.setClient("1");
		report.setComment("test comment");
		report.setCommercial("0778801708");
		report.setContact("1");
		report.setDate("02/01/2015");
		report.setSatisfaction("Oui");
		report.setVisit("1");


		assertTrue(CrvDaoObject.createOrModifyCrv(report));

		//same report id but with different comment value
		report.setComment("test commentttttt");
		//should always return true without bugs
		assertTrue(CrvDaoObject.createOrModifyCrv(report));

		//same report id but with another different comment value
		report.setComment("test");
		//should always return true without bugs
		assertTrue(CrvDaoObject.createOrModifyCrv(report));

	}

	@Test
	public void testGetAllReportsForClient(){

		List<Report> reportList = CrvDaoObject.getAllReportsForClient("48400813100019");
		int size = reportList.size();
		boolean isReportInstance = false;



		if(reportList.get(0).getClass().isInstance(Report.class)){
			isReportInstance = true;
		}

		assertTrue(isReportInstance);
		assertTrue(size>0);
	}
	
	@Test
	public void testErrorGetAllReportsForClient(){

		List<Report> reportList = CrvDaoObject.getAllReportsForClient("48400813100019");
		int size = reportList.size();
		
		assertFalse(size == 0);
	}
	
	@Test
	public void testErrorGetAllReportsForWrongClient(){

		List<Report> reportList = CrvDaoObject.getAllReportsForClient("1234");
		int size = reportList.size();
		
		assertFalse(size > 0);
	}
	

	@Test
	public void testDeleteReportById(){

		assertTrue(CrvDaoObject.deleteReportById("1"));
	}

	@Test
	public void testDeleteReportByIdError(){

		assertFalse(CrvDaoObject.deleteReportById("0"));
	}
} 
