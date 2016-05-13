package api.ctruong.uc.suggestion.prospect;

import junit.framework.Assert;
import miage.pds.api.ctruong.uc.prospect.suggest.controller.ProspectController;
import miage.pds.api.ctruong.uc.prospect.suggest.rest.ProspectRestController;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Truong on 2/10/2016.
 */
public class ProspectRestControllerTest {


    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    ProspectRestController controller;
    ModelAndView modelAndView;


    @Before
    public void setUp() throws Exception {
        this.controller = new ProspectRestController();
        this.modelAndView = new ModelAndView();
    }


    @Test
    public void helloWorld() throws Exception {
        Assert.assertEquals(null, modelAndView.getView());
    }

    @Test
    public void createNewClient() throws Exception {
        Assert.assertEquals(null, modelAndView.getView());
    }

    @Test
    public void analyseProcess() throws Exception {
        Assert.assertNotNull(controller.analyseProcess());
        ProspectController prospectController = new ProspectController();
        Assert.assertEquals(prospectController.analyseProspect().size(), controller.analyseProcess().size());
    }


}