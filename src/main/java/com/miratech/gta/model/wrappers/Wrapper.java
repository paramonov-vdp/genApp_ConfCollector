package com.miratech.gta.model.wrappers;

/**
 Class are wrapper for fields from classes : - Environment(environmentName field), AbstractCollector(collectorName, period
 fields), Component(componentName, componentID fields)
 */
public class Wrapper {
    private String environmentName;
    private String collectorName;
    private int period;
    private String componentName;
    private String componentID;


    /**
     *getters and setters for wrapper use
     *
     */

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getEnvironmentName() {
        return environmentName;
    }

    public void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getComponentID() {
        return componentID;
    }

    public void setComponentID(String componentID) {
        this.componentID = componentID;
    }
}
