package it.unimore.iot.smart.home.project.simulated_environment.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmartHomeProcess {

    private final static Logger logger = LoggerFactory.getLogger(SmartHomeProcess.class);

    public static void main(String[] args) {

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

    }

}
