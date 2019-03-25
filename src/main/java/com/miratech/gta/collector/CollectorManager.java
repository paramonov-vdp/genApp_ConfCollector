package com.miratech.gta.collector;

/**
 * Interface provide methods for managing of all collectors
 */
public interface CollectorManager {

    /**
     * Method found collector by name and stop it working.
     *
     * @param coollectorName name of collector to stop.
     */
    void shutDownCollector(String coollectorName);

    /**
     * Method to change default period of collecting states.
     *
     * @param periodOFCollecting int value in minutes.
     */
    void setPeriodOFCollecting(int periodOFCollecting);

    /**
     * Add new collector in system.
     *
     * @param collector collector - new collector object.
     */
    void addCollector(AbstractCollector collector);

    /**
     * Start working of newly or previous created collector.
     *
     * @param environmentName name of environment, on which collector will be work.
     * @param collectorName   name of previous created collector.
     */
    void startCollect(String environmentName, String collectorName);

    /**
     * Check is collector working or not.
     *
     * @param collectorName name of collector to check.
     * @return true if working, false if not.
     */
    boolean collectorWorking(String collectorName);

    /**
     * Check is collector excist in system or not.
     *
     * @param collectorName name of collector to check.
     * @return true if excist, false if not.
     */
    boolean collectorExist(String collectorName);
}
