package it.unimore.iot.smart.home.project.edge_application.services;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import it.unimore.iot.smart.home.project.edge_application.DataCollectorPolicyManager;
import it.unimore.iot.smart.home.project.edge_application.resources.DeviceResource;
import it.unimore.iot.smart.home.project.edge_application.resources.LocationResource;
import it.unimore.iot.smart.home.project.edge_application.resources.PolicyResource;
import it.unimore.iot.smart.home.project.edge_application.utils.LoadInitialData;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class AppService extends Application<AppConfig> {

    DataCollectorPolicyManager dataCollectorPolicyManager;

    public static void main(String[] args) throws Exception {

        new AppService().run(new String[]{"server", args.length > 0 ? args[0] : "configuration.yml"});

    }

    public void run(AppConfig appConfig, Environment environment) throws Exception {

        // Load the initial data from file JSON
        LoadInitialData.loadDataBuildingFromFileJson(appConfig.getDataCollectorPolicyManager());

        // Start the Policy Manager
        appConfig.getDataCollectorPolicyManager().init();

        // Add our defined resources
        environment.jersey().register(new LocationResource(appConfig));
        environment.jersey().register(new DeviceResource(appConfig));
        environment.jersey().register(new PolicyResource(appConfig));

        // Enable CORS headers
        final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

    }

    @Override
    public void initialize(Bootstrap<AppConfig> bootstrap) {

        bootstrap.addBundle(new SwaggerBundle<AppConfig>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(AppConfig configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });

    }
}