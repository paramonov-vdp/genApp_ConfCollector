package com.miratech.gta.model;


import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.HashSet;
import java.util.Set;

/**
 * Pojo object, which describe component(server application). Fields: ID - unique identificator of component,
 * component name - describe component name, environment set - environments, for which belongs component.
 */
public class Component {
    private String ID;
    private String componentName;
    @JsonBackReference
    private Set<Environment> environmentSet;


    public Component(String componentName, Environment environment, String ID) {
        this.ID = ID;
        this.componentName = componentName;
        environmentSet = new HashSet<>();
        environmentSet.add(environment);
    }

    public Component(String componentName) {
        this.componentName = componentName;
        this.environmentSet = new HashSet<>();
    }


    public Component() {
        environmentSet = new HashSet<>();
    }

    public String getID() {
        return ID;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public Set<Environment> getEnvironmentSet() {
        return environmentSet;
    }

    public void setEnvironmentSet(Environment environment) {
        this.environmentSet.add(environment);
    }


}
