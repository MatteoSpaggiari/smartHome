package it.unimore.iot.smart.home.project.edge_application;

import it.unimore.iot.smart.home.project.edge_application.persistence.IIotInventoryDataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataCollectorPolicyManager {

    final protected Logger logger = LoggerFactory.getLogger(DataCollectorPolicyManager.class);

    IIotInventoryDataManager inventoryDataManager;

    public DataCollectorPolicyManager(IIotInventoryDataManager inventoryDataManager) {
        this.inventoryDataManager = inventoryDataManager;
        logger.info("Instance of DataCollectorPolicyManager");
    }
}
