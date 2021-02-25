package it.unimore.iot.smart.home.project.server;

import it.unimore.iot.smart.home.project.server.resource.coap.CoapLocationResource;
import it.unimore.iot.smart.home.project.server.resource.coap.CoapPresenceSensorResource;
import it.unimore.iot.smart.home.project.server.resource.model.LightControllerModel;
import it.unimore.iot.smart.home.project.server.resource.model.LocationDescriptor;
import it.unimore.iot.smart.home.project.server.resource.raw.LightControllerRawSmartObject;
import it.unimore.iot.smart.home.project.server.resource.raw.LocationRawDescriptor;
import it.unimore.iot.smart.home.project.server.resource.raw.PresenceRawSensor;
import it.unimore.iot.smart.home.project.server.resource.raw.SmartObjectResource;
import org.eclipse.californium.core.CoapServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SmartHomeProcess extends CoapServer {

    private final static Logger logger = LoggerFactory.getLogger(SmartHomeProcess.class);

    public SmartHomeProcess() {

        super();
        String deviceId = String.format("dipi:iot:%s", UUID.randomUUID().toString());

        //INIT Emulated Physical Sensors and Actuators
        PresenceRawSensor presenceRawSensorLivingRoom = new PresenceRawSensor();
        LightControllerRawSmartObject lightControllerRawSmartObjectLivingRoom = new LightControllerRawSmartObject(new LightControllerModel());
        List<SmartObjectResource> smartObjectResourcesLivingRoom = new ArrayList<>();
        smartObjectResourcesLivingRoom.add(presenceRawSensorLivingRoom);
        smartObjectResourcesLivingRoom.add(lightControllerRawSmartObjectLivingRoom);
        LocationRawDescriptor locationRawDescriptor = new LocationRawDescriptor(new LocationDescriptor(UUID.randomUUID().toString(), "living-room", "one", smartObjectResourcesLivingRoom));

        CoapPresenceSensorResource coapPresenceSensorResource = new CoapPresenceSensorResource(deviceId, "presence_sensor", presenceRawSensorLivingRoom);
        CoapLocationResource coapLocationResource = new CoapLocationResource(deviceId, "living-room", locationRawDescriptor);

        coapLocationResource.add(coapPresenceSensorResource);

    }

    public static void main(String[] args) {

        SmartHomeProcess smartHomeProcess = new SmartHomeProcess();
        smartHomeProcess.start();

        logger.info("Coap Server Started ! Available resources: ");

        smartHomeProcess.getRoot().getChildren().stream().forEach(resource -> {
            logger.info("Resource {} -> URI: {} (Observable: {})", resource.getName(), resource.getURI(), resource.isObservable());
            if(!resource.getURI().equals("/.well-known")){
                resource.getChildren().stream().forEach(childResource -> {
                    logger.info("\t Resource {} -> URI: {} (Observable: {})", childResource.getName(), childResource.getURI(), childResource.isObservable());
                });
            }
        });

    }
}
