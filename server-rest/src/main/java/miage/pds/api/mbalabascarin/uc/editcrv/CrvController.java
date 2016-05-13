package miage.pds.api.mbalabascarin.uc.editcrv;


import miage.pds.api.mbalabascarin.uc.editcrv.dao.CrvMorphiaDao;
import miage.pds.api.mbalabascarin.uc.editcrv.model.Report;
import miage.pds.api.mbalabascarin.uc.editcrv.model.Reporting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CrvController {

    private static final Logger logger = LoggerFactory.getLogger(CrvController.class);
    boolean status = false;
    CrvMorphiaDao dao = new CrvMorphiaDao();

    public CrvController () {
        // TODO Auto-generated constructor stub
    }

    /**
     * Simply returns a status string.
     */
    @RequestMapping(value = "/crv", method = RequestMethod.GET)
    public
    @ResponseBody
    String home () {

        //logger.info("REST SERVER IS RUNNING :)");
        return "TEST PAGE :)";
    }

    @RequestMapping(value = "/crv/addCrv", method = RequestMethod.POST, headers = "Accept=application/json")
    public
    @ResponseBody
    Boolean createOrModifyCrv (@RequestBody Reporting crv) {
        logger.info("Start create Report.");
        return dao.createOrModifyCrv(crv.getReport());


    }

    @RequestMapping(value = "/crv/getCrv/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Report> getReportForCommercial (@PathVariable String id) {
        logger.info("Start get Reports.");
        return dao.getAllReportsForClient(id);

    }

    @RequestMapping(value = "/crv/deleteCrv/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Boolean deleteReportById (@PathVariable String id) {

        logger.info("Start delete Report by id.");
        return dao.deleteReportById(id);

    }


}
