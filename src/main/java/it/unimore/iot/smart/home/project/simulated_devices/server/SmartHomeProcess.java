package it.unimore.iot.smart.home.project.simulated_devices.server;

import it.unimore.iot.smart.home.project.simulated_devices.utils.LoadInitialData;
import org.eclipse.californium.core.CoapServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class SmartHomeProcess {

    private final static Logger logger = LoggerFactory.getLogger(SmartHomeProcess.class);

    public static void main(String[] args) {

        List<CoapServer> listCoapServers;

        listCoapServers = LoadInitialData.loadDataBuildingFromFileJson();

        // Additional Devices that will be added later in the edge application through HTTP requests
        PresenceSensorServer presenceSensorServerId6 = new PresenceSensorServer(5985, "6", false);
        LightControllerServer lightControllerServerId7 = new LightControllerServer(5986, "7", false, 180.0, 200, 100, 90);

        listCoapServers.add(presenceSensorServerId6);
        listCoapServers.add(lightControllerServerId7);

        listCoapServers.forEach(coapResource -> {
            coapResource.start();
            coapResource.getRoot().getChildren().stream().forEach(resource -> {
                logger.info("Resource {} -> URI: {} (Observable: {})", resource.getName(), resource.getURI(), resource.isObservable());
                if(!resource.getURI().equals("/.well-known")){
                    resource.getChildren().stream().forEach(childResource -> {
                        logger.info("\t Resource {} -> URI: {} (Observable: {})", childResource.getName(), childResource.getURI(), childResource.isObservable());
                    });
                }
            });
        });

    }

}
