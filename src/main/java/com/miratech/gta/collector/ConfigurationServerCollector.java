package com.miratech.gta.collector;

import com.genesyslab.platform.applicationblocks.com.objects.CfgApplication;
import com.genesyslab.platform.applicationblocks.com.objects.CfgPortInfo;
import com.genesyslab.platform.applicationblocks.com.queries.CfgApplicationQuery;
import com.genesyslab.platform.configuration.protocol.types.CfgAppType;
import com.miratech.gta.model.Environment;
import com.miratech.gta.model.State;
import com.miratech.gta.model.wrappers.CustomException;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Implementation of abstract collector, this class work with configuration server of genesys
 * environment.
 */
@Component
public class ConfigurationServerCollector extends AbstractCollector {

    /**
     * All collectors creating with default name  - "first", and date format.
     */
    public ConfigurationServerCollector() {
        this.name = "first";
        this.dateFormat = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss ");
    }

    /**
     * Method create new states object of configuration server. Used by putStateToStateManager method to put generated
     * states to buffer.
     *
     * @return created states.
     */
    @Override
    protected List<State> getStateFromComponent() {
        List<State> toReturnState = new ArrayList<>();
        Map<String, Object> toReturn;
        Integer maxConnections = 0;
        String objectWithmaxConnections = null;
        Integer appTotal = 0;
        Integer appsIsServer = 0;
        Integer totalNumberOfPorts = 0;
        StringBuilder securedPorts = new StringBuilder();
        StringBuilder unsecuredPorts = new StringBuilder();
        try {
            CfgApplicationQuery query = new CfgApplicationQuery();
            Set allComponents = new HashSet(CfgAppType.values());

            for (Object component : allComponents) {
                query.setAppType((CfgAppType) component);
                Collection<CfgApplication> apps = confService.retrieveMultipleObjects(CfgApplication.class, query);
                if (apps == null) {
                    continue;
                } else {
                    for (CfgApplication app : apps) {
                        appTotal += 1;
                        try {
                            Collection<CfgPortInfo> portsMap = app.getPortInfos();
                            if (portsMap != null && portsMap.size() > 0) {
                                for (CfgPortInfo port : portsMap) {
                                    totalNumberOfPorts += 1;
                                    if (port.isSaved()) {
                                        securedPorts.append(port.getPort().toString() + " ");
                                    } else unsecuredPorts.append(port.getPort().toString() + " ");
                                }
                            }

                            if (app.getAppServers().size() > maxConnections) {
                                objectWithmaxConnections = app.getName();
                                maxConnections = app.getAppServers().size();
                            }
                            if (app.getIsServer().name().equals("CFGTrue")) {
                                appsIsServer += 1;
                            }
                        } catch (NullPointerException exc) {
                            continue;
                        }
                    }
                }
            }

        } catch (Exception e) {
            try {
                throw new CustomException("something wrong");
            } catch (CustomException e1) {
                e1.getMessage();
            }
        }
        toReturn = new LinkedHashMap<>();
        toReturn.put("Ports total: ", totalNumberOfPorts);
        toReturn.put("Secured ports: ", securedPorts.toString());
        toReturn.put("Unsecured ports: ", unsecuredPorts.toString());
        toReturn.put("Applications total: ", appTotal);
        toReturn.put("Server applications: ", appsIsServer);
        toReturn.put("Application with largest number of connections: ", objectWithmaxConnections);
        toReturn.put("Number of connection : ", maxConnections);
        toReturnState.add(new State(toReturn, dateFormat.format(new Date()), "99"));
        return toReturnState;
    }


    /**
     * Method get system state of component.
     */
    protected void getSystemState() {
        //todo collect system data
    }


    /**
     * Method used to collect components of environment and fill environment container.
     */
    protected void fillEnvironment() {
        Environment environment = environmentContainer.findEnvironmentByName(this.name);
        try {
            CfgApplicationQuery query = new CfgApplicationQuery();
            Set allComponents = new HashSet(CfgAppType.values());
            for (Object allComponent : allComponents) {
                query.setAppType((CfgAppType) allComponent);
                Collection<CfgApplication> apps = confService.retrieveMultipleObjects(CfgApplication.class, query);
                if (apps == null) {
                    continue;
                } else {
                    for (CfgApplication app : apps) {
                        environment.setComponent(new com.miratech.gta.model.Component(app.getName(),
                                environment, app.getDBID().toString()));
                    }
                }
            }
        } catch (Exception e) {
            try {
                throw new CustomException("something wrong");
            } catch (CustomException e1) {
                e1.getMessage();
            }

        }
    }

    @Override
    protected void connectToStateManager() {
        System.out.println("connected");
    }


    @Override
    public void run() {
        while (!isInterrupted) {
            try {
                fillEnvironment();
                putStateToStateManager();
                Thread.sleep(period);
            } catch (InterruptedException e) {
                System.out.println(this.name + " collector was stopped");
                break;
            }
        }
    }
}
