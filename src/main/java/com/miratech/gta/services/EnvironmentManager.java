package com.miratech.gta.services;


import com.miratech.gta.model.Environment;

import java.util.List;

/**
 * Interface describe methods to work with environment storage.
 */
public interface EnvironmentManager {

    /**
     * Method add new environment to storage.
     */
    void addEnvironment(Environment environment);

    /**
     @return all environments from storage
     */
    List<Environment> getAllEnvironments();

    /**
     * Return information about environments and it's components
     * @param host - host of environment to return
     * @return environments class, with components inside.
     */
    Environment getEnvironmentWithComponents(String host);

}
