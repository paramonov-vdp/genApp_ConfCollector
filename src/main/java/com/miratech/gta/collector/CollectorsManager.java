package com.miratech.gta.collector;

import com.miratech.gta.environment.EnvironmentContainer;
import com.miratech.gta.model.Environment;
import com.miratech.gta.model.wrappers.CustomException;
import com.miratech.gta.provider.ConfServiceProvider;
import com.miratech.gta.services.StateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Class contains all collectors and has methods to manage them, also has link to buffer, to set it to
 * collectors. By default period of collecting  state from components - once in two minutes. User can
 * chang this parameter by invoke set method.
 */
@Component
public class CollectorsManager implements CollectorManager {
    private static boolean firstinit = true;
    List<AbstractCollector> collectors;
    @Autowired
    EnvironmentContainer environmentContainer;
    @Autowired
    private StateManager stateManager;
    private Long periodOFCollecting = 120_000L;

    /**
     * If first initialization - this manager will not be used.
     */
    public CollectorsManager() {
        if (firstinit) {
            System.out.println();
            firstinit = false;
        } else collectors = new ArrayList<>();
    }

    /**
     * Method to change already exist buffer for collectors
     *
     * @param stateManager - new buffer.
     */
    public void changeStateManager(StateManager stateManager) {
        this.stateManager = stateManager;
        for (AbstractCollector collector : collectors) {
            collector.setStateManager(stateManager);
        }
    }


    /**
     * Shutdown one of collectors.
     *
     * @param collectorName name of collector.
     */
    @Override
    public void shutDownCollector(String collectorName) {
        for (AbstractCollector collector : collectors) {
            if (collector.name.equals(collectorName))
                collector.shutdownCollector();
        }
    }

    /**
     * Changing period of collecting
     *
     * @param periodOFCollecting - integer value, given like a minutes, converting to milliseconds.
     */
    @Override
    public void setPeriodOFCollecting(int periodOFCollecting) {
        this.periodOFCollecting = new Long(periodOFCollecting * 60_000);
        for (AbstractCollector collector : collectors) {
            collector.setPeriod(this.periodOFCollecting);
        }
    }

    /**
     * Add new collector to collection.
     */
    @Override
    public void addCollector(AbstractCollector collector) {
        collectors.add(collector);
    }

    /**
     * In this method one of existing collector receive command to start working. Manager give him connection to server,
     * set period of collection(2 minutes by default), set buffer, and all environments, so that collector can fill
     * environments by components, and buffer by states.
     *
     * @param environmentName - name of environment which contain connection parameters.
     * @param collectorName   - name of collector to start.
     */
    @Override
    public void startCollect(String environmentName, String collectorName) {
        Environment environment = environmentContainer.findEnvironmentByName(environmentName);
        AbstractCollector collector = findCollector(collectorName);
        ConfServiceProvider connection = new ConfServiceProvider(environment.getHost(),
                environment.getMainServerPort(),
                environment.getUserLogin(),
                environment.getPassword());
        connection.initConnection();

        collector.setActive(true);
        collector.setPeriod(periodOFCollecting);
        collector.setStateManager(stateManager);
        collector.setEnvironmentContainer(environmentContainer);
        try {
            collector.setConfService(connection.getConfService());
        } catch (Exception e) {
            try {
                throw new CustomException("Some problems with connection");
            } catch (CustomException e1) {
                e1.getMessage();
            }
        }

        new Thread(collector).start();
    }

    /**
     * Check state of collector - working or not.
     *
     * @param collectorName - name of collector to check
     * @return true if collector working now, false if not.
     */
    @Override
    public boolean collectorWorking(String collectorName) {
        try {
            AbstractCollector collector = findCollector(collectorName);
            if (collector != null && collector.isActive) return true;
        } catch (NullPointerException ex) {
            return false;
        }
        return false;
    }

    /**
     * Check - collector exist or not.
     *
     * @param collectorName - name of collector to check.
     * @return true if exist, false if not.
     */
    @Override
    public boolean collectorExist(String collectorName) {
        collectors.contains(collectorName);
        AbstractCollector collector = findCollector(collectorName);
        if (collector == null) {
            System.out.println(collector);
            return false;
        }
        return true;
    }

    /**
     * Method find collector for collection if collector exist.
     *
     * @param name - name of collector to find.
     * @return instance of collector if exist, null if not.
     */
    private AbstractCollector findCollector(String name) {
        for (AbstractCollector collector : collectors) {
            if (collector.getName().equals(name)) {
                return collector;
            }
        }
        return null;
    }
}
