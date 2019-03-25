package com.miratech.gta.web.controller;

import com.miratech.gta.model.Component;
import com.miratech.gta.model.Environment;
import com.miratech.gta.model.wrappers.Wrapper;
import com.miratech.gta.services.EnvironmentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsible for operations with environments. Operations possible by EnvironmentManager interface, which
 * describe all this functions.
 */
@Controller
@RequestMapping("/gta.1.0/environments/")
public class EnvironmentController {

    @Autowired
    EnvironmentManager environmentManager;

    /**
     * Rest path - /gta.1.0/environments/, method GET. Method  return information about  all environments with it's components,
     * if user define at least one of environments. If user define environment, but doesn't created and started
     * any collectors, or not added any custom component object - component field will be empty.
     *
     * @return List of environments, if exist at least one environment object.
     * "It's seems you doesn't register environment yet" - if user doesn't crate environment yet.
     */
    @RequestMapping(path = "/", method = RequestMethod.GET)
    @ResponseBody
    public Object getAllEnvironments() {
        List<Environment> requestResponce = environmentManager.getAllEnvironments();
        if (requestResponce.size() == 0) {
            return "It's seems you doesn't register environment yet";
        } else return requestResponce;
    }

    /**
     * Rest path - /gta.1.0/environments/{environmentHost}, method GET.  Method return one environment, which host
     * is passed by PathVariable parameter. If user define environment, but doesn't created and started
     * any collectors, or not added any custom component object - component field will be empty.
     *
     * @param host - ip v4 address of environment.
     * @return Environment object, if environment with specified ip founded.
     * String object "No environments with this ip founded" if no environment with specified ip were founded.
     */
    @RequestMapping(path = "/{environmentHost}", method = RequestMethod.GET)
    @ResponseBody
    public Object getEnvironment(@PathVariable(value = "environmentHost") String host) {
        Environment environment = environmentManager.getEnvironmentWithComponents(host);
        if (environment == null) {
            return "No environments with this ip founded";
        }
        return environment;
    }

    /**
     * Rest path -  /gta.1.0/environments/, method POST. Method add new environment object in system.
     *
     * @param environment environment object, created with fields, passed by json: "host", "name", "mainServerport",
     *                    "userLogin","password".
     * @return String object for more  informative response.
     */
    @RequestMapping(path = "/", method = RequestMethod.POST)
    @ResponseBody
    public String addEnvironment(@RequestBody Environment environment) {
        environmentManager.addEnvironment(environment);
        return "Environment " + environment.getName() + " succesfully added";
    }

    /**
     * Rest path - /gta.1.0/environments/{environmentHost}. Method add new component in environment.
     *
     * @param wrapper wrapper object, to pass component object fields: componentID, componentName.
     * @param host    ip v4 address of environment.
     * @return String object: "Ni such environment" - if no environments with host parameter were founded.
     * "Component with ID already exist" - in environment with this ip address already exist component with ID, passed
     * by wrapper.
     * "Component added" - if component with required id added succesfully.
     */
    @RequestMapping(path = "/{environmentHost}", method = RequestMethod.POST)
    @ResponseBody
    public String addComponent(@RequestBody Wrapper wrapper, @PathVariable(value = "environmentHost") String host) {
        Environment environment = environmentManager.getEnvironmentWithComponents(host);
        if (environment == null) {
            return "No such environment";
        }
        if (environment.IsContainComponent(wrapper.getComponentName())) {
            return "Component with ID: " + wrapper.getComponentID() + " already exist";
        } else {
            environment.setComponent(new Component(wrapper.getComponentName(), environment,
                    wrapper.getComponentID()));
        }

        return "Component added";
    }


}
