package miage.pds.api.ctruong.uc.sync.controller;

import miage.pds.api.ctruong.uc.prospect.suggest.rest.ProspectRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Truong on 5/10/2016.
 */
@Controller
public class SynchronisationRestController {
    private static final Logger log = LoggerFactory.getLogger(ProspectRestController.class);

    public SynchronisationRestController() {
        log.info("Sync is running ...");
    }

    @RequestMapping(value = "/sync/demo", method = RequestMethod.GET)
    public
    @ResponseBody
    String syncOk() {
        return "ok";
    }

}
