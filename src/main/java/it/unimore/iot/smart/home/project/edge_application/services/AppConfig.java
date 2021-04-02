package it.unimore.iot.smart.home.project.edge_application.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import it.unimore.iot.smart.home.project.edge_application.persistence.DefaultIotInventoryDataManger;
import it.unimore.iot.smart.home.project.edge_application.persistence.IIotInventoryDataManager;

public class AppConfig extends Configuration {

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    private IIotInventoryDataManager inventoryDataManager = null;

    public IIotInventoryDataManager getInventoryDataManager(){

        if(this.inventoryDataManager == null)
            this.inventoryDataManager = new DefaultIotInventoryDataManger();

        return this.inventoryDataManager;
    }

}