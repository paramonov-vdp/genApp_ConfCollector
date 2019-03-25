package com.miratech.gta.environment;

import com.miratech.gta.model.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Class contains all existing environments in list,  and methods to manage them.
 */
@Component
public class EnvironmentContainer {
    private List<Environment> allEnvironments;


    public EnvironmentContainer() {
        this.allEnvironments = new ArrayList<>();
    }

    /**
     * Add new environment to collection.
     *
     * @param environment environment to add.
     */
    public void addEnvironment(Environment environment) {
        allEnvironments.add(environment);
    }

    /**
     * @return all existing environments
     */
    public List<Environment> getAllEnvironments() {
        return allEnvironments;
    }

    /**
     * Method  return one environment, founded by environment host.
     *
     * @param host - parameter to search environment
     */
    public Environment getEnvironmentWithComponents(String host) {
        for (Environment Environment : allEnvironments) {
            if (Environment.getHost().equals(host))
                return Environment;
        }
        return null;
    }

    /**
     * Method find environment by name parameter
     * @param name - name to search from all environments
     * @return  Environment - if environments with this name exist;
     * null - if it doesn't exist.
     */
    public Environment findEnvironmentByName(String name) {
        for (Environment environment : allEnvironments) {
            if (environment.getName().equals(name))
                return environment;
        }
        return null;
    }

}
