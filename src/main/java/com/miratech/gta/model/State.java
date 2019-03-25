package com.miratech.gta.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Map;
import java.util.Objects;

/**
 * Pojo object, which describe state of component object. Fields: state - Map with state name in key and value of state
 * in value, dateSnapshot - date, when state is received, componentID - identificator of component, for which state is
 * received, issystem - flag, used to recognize system state of component.
 */
public class State {

    private Map<String, Object> state;
    private String dateSnapshot;
    private String componentID;
    private boolean isSystem;


    public State(Map<String, Object> state, String dateFormat, String componentID) {
        this.state = state;
        this.dateSnapshot = dateFormat;
        this.componentID = componentID;
    }


    public State() {
    }

    public Map<String, Object> getState() {
        return state;
    }

    public void setState(Map<String, Object> state) {
        this.state = state;
    }

    public String getDateSnapshot() {
        return dateSnapshot;
    }

    public void setDateSnapshot(String dateSnapshot) {
        this.dateSnapshot = dateSnapshot;
    }

    public String getComponentID() {
        return componentID;
    }

    public void setComponentID(String componentID) {
        this.componentID = componentID;
    }

    @JsonIgnore
    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;
        State state = (State) o;
        return Objects.equals(componentID, state.componentID);
    }


    @Override
    public int hashCode() {
        return Objects.hash(componentID);
    }


}
