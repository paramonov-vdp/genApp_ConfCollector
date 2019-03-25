package com.miratech.gta.services;

import com.miratech.gta.model.State;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class represent storage in RAM and implements methods to work with this storage.
 */
@Service
public class InMemoryStateManager implements StateManager {

    private List<State> realtimeState;
    private List<State> historicalState;
    private Integer stateSize = 5_500_500;
    private List<State> systemRealTimeState;
    private List<State> systemHistoricalState;


    public InMemoryStateManager() {
        this.systemHistoricalState = new CopyOnWriteArrayList<>();
        this.systemRealTimeState = new CopyOnWriteArrayList<>();
        this.realtimeState = new CopyOnWriteArrayList<>();
        this.historicalState = new CopyOnWriteArrayList<>();
    }


    /**
     * Method receive new state. If state for component already exists, it override previous state for
     * this component. State container has fixed size, represent by stateSize field. If state container
     * size comes to stateSize value,  new state container will be created, old will  be removed.
     * By default with this method works only collectors, so that method is synchronized.
     * In  class State implements compare of states by ID of component.
     *
     * @param state - new state, received from collectors. If received state is applies to system state, it will be
     *              put to system state container.
     */
    public synchronized void putToRealtime(State state) {
        if (realtimeState.size() >= stateSize) {
            realtimeState = new CopyOnWriteArrayList<>();
        }

        if (state.isSystem() && !systemRealTimeState.contains(state)) {
            systemRealTimeState.add(state);
            return;
        }

        if (state.isSystem()) {
            systemRealTimeState.remove(state);
            systemRealTimeState.add(state);
            return;
        }

        if (!realtimeState.contains(state)) {
            realtimeState.add(state);
        } else {
            realtimeState.remove(state);
            realtimeState.add(state);
            this.notifyAll();
        }
    }


    /**
     * Method find state for specific component, which founded by componentID param.
     * Uses private method findstates to find state for component in real time state container.
     *
     * @param componentID - unique ID of component
     * @return
     */
    public synchronized List<State> getRealtimeStateByName(String componentID) {
        return findStates(componentID, realtimeState);
    }

    /**
     * Method receive new state, and collect it to historical state container. If historical state container
     * comes to stateSize value, new stateContainer will be crated, and old will be removed. BBy default with this
     * method works only collectors, so that method is synchronized.
     *
     * @param state
     */
    public synchronized void putToHistorical(State state) {
        if (historicalState.size() >= stateSize) {
            historicalState = new CopyOnWriteArrayList<>();
        }
        if (state.isSystem()) {
            systemHistoricalState.add(state);
            return;
        }
        historicalState.add(state);
        this.notifyAll();
    }

    /**
     * Method find state for specific component, which founded by componentID param.
     * Uses private method findstates to find state for component in historical state container.
     *
     * @param componentID - unique ID of component
     * @return
     */
    public synchronized List<State> getHistoryOfStates(String componentID) {
        return findStates(componentID, historicalState);
    }

    /**
     * @return all states from real time  container
     */
    @Override
    public List<State> getAllRealtimeStates() {
        return this.realtimeState;
    }

    /**
     * @return all states from historical container
     */
    @Override
    public List<State> getAllHistoryOfStates() {
        return this.historicalState;
    }

    /**
     * @return all states from system historical container.
     */
    @Override
    public List<State> getAllSystemHistoryStates() {
        return this.systemHistoricalState;
    }

    /**
     * @return all states from system real time container.
     */
    @Override
    public State getCurrentSystemState() {
        return this.systemRealTimeState.get(0);
    }

    /**
     * Method is used for find states for component.
     *
     * @param componentID - used  in component search
     * @param stateType   - type of state container
     * @return list of states for specific component.
     */
    private List<State> findStates(String componentID, List<State> stateType) {
        List<State> toReturn = new ArrayList<>();
        for (State state : stateType) {
            if (state.getComponentID().equals(componentID))
                toReturn.add(state);
        }
        return toReturn;
    }

}
