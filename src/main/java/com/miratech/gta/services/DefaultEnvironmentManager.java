package com.miratech.gta.services;


import com.miratech.gta.environment.EnvironmentContainer;
import com.miratech.gta.model.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Class has storage of environments - environmentscontainer field, and implements methods to work with this storage.
 * Methods wrap environment container methods.
 */
@Service
public class DefaultEnvironmentManager implements EnvironmentManager {

    @Autowired
    EnvironmentContainer environmentContainer;

    /**
     * Method add new environment object to environment container storage.
     *
     * @param environment environment, created by user.
     */
    @Override
    public void addEnvironment(Environment environment) {
        environmentContainer.addEnvironment(environment);
    }

    /**
     * Method return all environments for environment container storage
     */
    @Override
    public List<Environment> getAllEnvironments() {
        return environmentContainer.getAllEnvironments();
    }

    /**
     * Method return one environment from environment container storage, founded by host parameter.
     *
     * @param host - host of environment to return
     * @return Environment object, founded by host.
     */
    @Override
    public Environment getEnvironmentWithComponents(String host) {
        return environmentContainer.getEnvironmentWithComponents(host);
    }
}
