package it.unimore.iot.smart.home.project.edge_application.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import it.unimore.iot.smart.home.project.edge_application.DataCollectorPolicyManager;
import it.unimore.iot.smart.home.project.edge_application.persistence.IIotInventoryDataManager;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class AppConfig extends Configuration {

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    private IIotInventoryDataManager inventoryDataManager = null;

    private DataCollectorPolicyManager dataCollectorPolicyManager = null;

    public  DataCollectorPolicyManager getDataCollectorPolicyManager() {

        if(this.dataCollectorPolicyManager == null)
            this.dataCollectorPolicyManager = new DataCollectorPolicyManager();

        return this.dataCollectorPolicyManager;
    }

}