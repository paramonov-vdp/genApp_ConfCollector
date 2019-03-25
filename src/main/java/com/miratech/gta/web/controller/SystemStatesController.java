package com.miratech.gta.web.controller;

import com.miratech.gta.services.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Controller responsible for return real time and historical system state of component. In this version
 * of program controllers doesn't collect state of system, so that methods of controller return String messages.
 */
@Controller
@RequestMapping("/gta.1.0/environments/components")
public class SystemStatesController {

    @Autowired
    StateManager stateManager;
    private String response = "Function will be  available in next version";

    @RequestMapping(path = "/system/history", method = RequestMethod.GET)
    @ResponseBody
    public String getSystemStateHistory() {
        return response;
    }

    @RequestMapping(path = "/system", method = RequestMethod.GET)
    @ResponseBody
    public String getSystemState() {
        return response;
    }
}
