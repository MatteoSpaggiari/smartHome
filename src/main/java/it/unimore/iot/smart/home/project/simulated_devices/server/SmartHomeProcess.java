package it.unimore.iot.smart.home.project.simulated_devices.server;

import org.eclipse.californium.core.CoapServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class SmartHomeProcess {

    private final static Logger logger = LoggerFactory.getLogger(SmartHomeProcess.class);

    public static void main(String[] args) {

        ArrayList<CoapServer> listCoapResource = new ArrayList<CoapServer>();

        PresenceSensorServer presenceSensorServer1 = new PresenceSensorServer(5683, "1");
        LightControllerServer lightControllerServer1 = new LightControllerServer(5783, "2");
        PresenceSensorServer presenceSensorServer2 = new PresenceSensorServer(5883, "3");
        LightControllerServer lightControllerServer2 = new LightControllerServer(5983, "4");
        LightControllerServer lightControllerServer3 = new LightControllerServer(5984, "5");

        listCoapResource.add(presenceSensorServer1);
        listCoapResource.add(presenceSensorServer2);
        listCoapResource.add(lightControllerServer1);
        listCoapResource.add(lightControllerServer2);
        listCoapResource.add(lightControllerServer3);

        listCoapResource.forEach(coapResource -> {
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

        /*
        PresenceSensorServer presenceSensorServer1 = new PresenceSensorServer(5683, "1");
        presenceSensorServer1.start();

        logger.info("Coap Server Started ! Available resources: ");

        presenceSensorServer1.getRoot().getChildren().stream().forEach(resource -> {
            logger.info("Resource {} -> URI: {} (Observable: {})", resource.getName(), resource.getURI(), resource.isObservable());
            if(!resource.getURI().equals("/.well-known")){
                resource.getChildren().stream().forEach(childResource -> {
                    logger.info("\t Resource {} -> URI: {} (Observable: {})", childResource.getName(), childResource.getURI(), childResource.isObservable());
                });
            }
        });

        LightControllerServer lightControllerServer1 = new LightControllerServer(5783, "2");
        lightControllerServer1.start();

        logger.info("Coap Server Started ! Available resources: ");

        lightControllerServer1.getRoot().getChildren().stream().forEach(resource -> {
            logger.info("Resource {} -> URI: {} (Observable: {})", resource.getName(), resource.getURI(), resource.isObservable());
            if(!resource.getURI().equals("/.well-known")){
                resource.getChildren().stream().forEach(childResource -> {
                    logger.info("\t Resource {} -> URI: {} (Observable: {})", childResource.getName(), childResource.getURI(), childResource.isObservable());
                });
            }
        });

        PresenceSensorServer presenceSensorServer2 = new PresenceSensorServer(5883, "3");
        presenceSensorServer2.start();

        logger.info("Coap Server Started ! Available resources: ");

        presenceSensorServer2.getRoot().getChildren().stream().forEach(resource -> {
            logger.info("Resource {} -> URI: {} (Observable: {})", resource.getName(), resource.getURI(), resource.isObservable());
            if(!resource.getURI().equals("/.well-known")){
                resource.getChildren().stream().forEach(childResource -> {
                    logger.info("\t Resource {} -> URI: {} (Observable: {})", childResource.getName(), childResource.getURI(), childResource.isObservable());
                });
            }
        });

        LightControllerServer lightControllerServer2 = new LightControllerServer(5983, "4");
        lightControllerServer2.start();

        logger.info("Coap Server Started ! Available resources: ");

        lightControllerServer2.getRoot().getChildren().stream().forEach(resource -> {
            logger.info("Resource {} -> URI: {} (Observable: {})", resource.getName(), resource.getURI(), resource.isObservable());
            if(!resource.getURI().equals("/.well-known")){
                resource.getChildren().stream().forEach(childResource -> {
                    logger.info("\t Resource {} -> URI: {} (Observable: {})", childResource.getName(), childResource.getURI(), childResource.isObservable());
                });
            }
        });

        LightControllerServer lightControllerServer3 = new LightControllerServer(5984, "5");
        lightControllerServer3.start();

        logger.info("Coap Server Started ! Available resources: ");

        lightControllerServer3.getRoot().getChildren().stream().forEach(resource -> {
            logger.info("Resource {} -> URI: {} (Observable: {})", resource.getName(), resource.getURI(), resource.isObservable());
            if(!resource.getURI().equals("/.well-known")){
                resource.getChildren().stream().forEach(childResource -> {
                    logger.info("\t Resource {} -> URI: {} (Observable: {})", childResource.getName(), childResource.getURI(), childResource.isObservable());
                });
            }
        });
        */
    }

}
