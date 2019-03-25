package com.miratech.gta.web.controller;

import com.miratech.gta.collector.AbstractCollector;
import com.miratech.gta.collector.CollectorManager;
import com.miratech.gta.collector.ConfigurationServerCollector;
import com.miratech.gta.model.wrappers.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller, responsible for operations with collectors. Operations possible by autowired Collector manager interface,
 * which describe all this functions.
 */
@Controller
@RequestMapping("/gta.1.0/environments/collectors")
public class CollectorsController {
    @Autowired
    CollectorManager collectorManager;

    /**
     * Rest path - /gta.1.0/environments/collectors/, method PUT
     * method accept for user period, which actually time of cycle working of collectors. By default collectors collect
     * states once in two minutes.
     *
     * @param wrapper - wrapper object, used to accept json field - period.
     */
    @RequestMapping(path = "/", method = RequestMethod.PUT)
    @ResponseBody
    public String setPeriod(@RequestBody Wrapper wrapper
    ) {
        collectorManager.setPeriodOFCollecting(wrapper.getPeriod());
        return "Period accepted";
    }

    /**
     * Rest path - /gta.1.0/environments/collectors/ , method POST
     * Method create new collector object, which not work right away.
     *
     * @param wrapper - wrapper object, used to accept json field - collectorName.
     * @return "Collector added succesfully message" if collector with name, passed by wrapper isn't exist.
     * "Collector already exist", if collector with name, passed by wrapper already exist.
     */
    @RequestMapping(path = "/", method = RequestMethod.POST)
    @ResponseBody
    public String addNewCollector(@RequestBody Wrapper wrapper) {
        AbstractCollector collector = null;
        if (!collectorManager.collectorExist(wrapper.getCollectorName())) {
            collector = new ConfigurationServerCollector();
        } else return "Collector " + wrapper.getCollectorName() + " already exist";

        collector.setName(wrapper.getCollectorName());
        collectorManager.addCollector(collector);
        return "Collector " + wrapper.getCollectorName() + " added succesfully";
    }

    /**
     * Rest path - /gta.1.0/environments/collectors/startCollecting, method POST
     * Method switch one of collector, defined by name to working mode.
     *
     * @param wrapper wrapper object, used to accept json field - collectorName
     * @return "Collector already working" - if collector with name, passed by wrapper already working.
     * "Collector start working succesfully" - if collector with name, passed by wrapper succesfully started collect states.
     */
    @RequestMapping(path = "/startCollecting", method = RequestMethod.POST)
    @ResponseBody
    public String startCollector(@RequestBody Wrapper wrapper) {
        if (collectorManager.collectorWorking(wrapper.getCollectorName())) {
            return "Collector " + wrapper.getCollectorName() + " already working!";
        } else {
            collectorManager.startCollect(wrapper.getEnvironmentName(), wrapper.getCollectorName());
            return "Collector " + wrapper.getCollectorName() + " start collecting succesfully";
        }
    }

    /**
     * Rest path - /gta.1.0/environments/collectors/, method DELETE
     * Method switch already working collector to not working mode. In this case collector will be exist, but not collecting
     * states.
     *
     * @param wrapper - wrapper wrapper object, used to accept json field - collectorName.
     * @return "Collector is off now" if collector succesfully switch to offline mode.
     * "Collector doesn't working" - if collector with name, passed by wrapper already in offline mode.
     */
    @RequestMapping(path = "/", method = RequestMethod.DELETE)
    @ResponseBody
    public String shutDownCollector(@RequestBody Wrapper wrapper
    ) {
        if (collectorManager.collectorWorking(wrapper.getCollectorName())) {
            collectorManager.shutDownCollector(wrapper.getCollectorName());
            return "Collector is off now";
        } else
            return "Collector doesn't working";
    }


}
