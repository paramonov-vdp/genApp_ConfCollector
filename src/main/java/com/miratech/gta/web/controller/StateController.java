package com.miratech.gta.web.controller;

import com.miratech.gta.model.State;
import com.miratech.gta.services.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Controller responsible for work with components states. That possible by autowired StateManager interface, which
 * describe methods for work with states.
 */
@Controller
@RequestMapping("/gta.1.0/environments/components/")
public class StateController {

    @Autowired
    StateManager stateManager;

    /**
     * Rest path - /gta.1.0/environments/components/state, Method GET
     * Method return all real time states(real time means last collected, period of collecting can be specified in
     * CollectorsController).
     *
     * @return List of states, which can contains system or components states(only components in this version)
     */
    @RequestMapping("/state")
    @ResponseBody
    public List<State> getState() {
        return stateManager.getAllRealtimeStates();
    }

    /**
     * Rest path - /gta.1.0/environments/components/history , method GET.
     * Method return all  states, collected for period of collector started(with defined interval of collecting)
     *
     * @return List of states, which can contains system or components states(only components in this version)
     */
    @RequestMapping("/history")
    @ResponseBody
    public List<State> getHistory() {
        return stateManager.getAllHistoryOfStates();
    }

    /**
     * Rest path - /gta.1.0/environments/components/state{component}, method - GET.
     * Method return all states, collected for period of collector started(with defined interval of collecting)
     *
     * @param component - id to find component
     * @return List of component states
     */
    @RequestMapping("/state/{component}")
    @ResponseBody
    public List<State> getStateByComponent2(@PathVariable(value = "component") String component) {
        return stateManager.getRealtimeStateByName(component);
    }

    /**
     * Rest path - gta.1.0/environments/components/history{component}
     * Method return all  states of one component, collected for period of collector started(with defined interval of collecting)
     *
     * @param component ID of component to find it's states
     * @return List of states of component.
     */
    @RequestMapping("/history/{component}")
    @ResponseBody
    public List<State> getStateByComponent(@PathVariable(value = "component") String component) {
        return stateManager.getHistoryOfStates(component);
    }


}
