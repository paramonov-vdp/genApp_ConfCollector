package com.miratech.gta.collector;

import com.genesyslab.platform.applicationblocks.com.IConfService;
import com.miratech.gta.environment.EnvironmentContainer;
import com.miratech.gta.model.State;
import com.miratech.gta.services.StateManager;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

/**
 * Class for collect data from different remote components of environment. Has link on buffer,
 * interface for connecting to buffer and component. Main task of class - collect states from remote
 * component and put the state into buffer.
 */
public abstract class AbstractCollector implements Runnable {

    protected Long period;
    protected String name;
    protected int port;
    protected boolean isInterrupted;
    protected SimpleDateFormat dateFormat;
    protected IConfService confService;
    protected EnvironmentContainer environmentContainer;
    protected boolean isActive;
    StateManager stateManager;

    /**
     * Method to set period of collecting
     *
     * @param period time in milliseconds
     */
    public void setPeriod(Long period) {
        this.period = period;
    }

    /**
     * display status of this collector - active or not
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Make collector active or not
     */
    public void setActive(boolean active) {
        isActive = active;
    }


    public String getName() {
        return name;
    }

    /**
     * Set new name for collector
     *
     * @param name - new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method change collector status to not active
     */
    protected void shutdownCollector() {
        this.isInterrupted = true;
    }

    /**
     * Method for set another buffer to collector
     */
    public void setStateManager(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    /**
     * Method  to  state fetching  from component
     */
    protected abstract List<State> getStateFromComponent();

    /**
     * Interface to implement connection to different buffer types
     */
    protected abstract void connectToStateManager();

    /**
     * Method set specific buffer for writing state in.
     *
     * @param environmentContainer - concrete buffer
     */
    public void setEnvironmentContainer(EnvironmentContainer environmentContainer) {
        this.environmentContainer = environmentContainer;
    }

    /**
     * Method pass to collector specific connection to server
     *
     * @param confService - connection interface
     */
    public void setConfService(IConfService confService) {
        this.confService = confService;
    }

    /**
     * Put already collected state to buffer
     */
    protected void putStateToStateManager() {
        List<State> states = getStateFromComponent();
        for (State state : states) {
            stateManager.putToHistorical(state);
            stateManager.putToRealtime(state);
        }
    }

    /**
     * Thread sleep for time, specified in period field, then wake up, collect state,
     * put it to the buffer and go to sleep again
     */
    @Override
    public void run() {
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractCollector)) return false;
        AbstractCollector collector = (AbstractCollector) o;
        return Objects.equals(name, collector.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
