package com.miratech.gta.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Pojo object, which describe environment, on which components is running. Fields: host - ip v4, where environment
 * deployed, name - name to describe environment, mainServerPort - port of main server, configuration server in genesys
 * environments. Userlogin, password - fields to provide access to main server, components - set of components, which running
 * in this environment.
 */
public class Environment {
    private String host;
    private String name;
    private int mainServerPort;
    private String userLogin;
    private String password;
    @JsonManagedReference
    private Set<Component> components;


    public Environment(String host, String name, int mainServerPort, String userLogin, String password) {
        this.host = host;
        this.name = name;
        this.mainServerPort = mainServerPort;
        this.userLogin = userLogin;
        this.password = password;
        components = new HashSet<>();
    }

    public Environment(String host, String name) {
        this.host = host;
        this.name = name;
    }

    public Environment() {
        components = new HashSet<>();
    }

    public boolean IsContainComponent(String name) {
        Component component = findComponent(name);
        if (component == null) return false;
        else return true;
    }

    public Component findComponent(String componentName) {
        for (Component component : components) {
            if (component.getComponentName().equals(componentName))
                return component;
        }
        return null;
    }

    public int getMainServerPort() {
        return mainServerPort;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Component> getComponents() {
        return components;
    }

    /**
     * Method add new component to environment.
     *
     * @param component component, created by user or collectors.
     */
    public void setComponent(Component component) {
        this.components.add(component);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Environment)) return false;
        Environment that = (Environment) o;
        return Objects.equals(host, that.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host);
    }
}
