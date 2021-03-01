package it.unimore.iot.smart.home.project.server;

import it.unimore.iot.smart.home.project.server.resource.coap.*;
import it.unimore.iot.smart.home.project.server.resource.model.LightControllerModel;
import it.unimore.iot.smart.home.project.server.resource.model.LocationDescriptor;
import it.unimore.iot.smart.home.project.server.resource.raw.*;
import org.eclipse.californium.core.CoapResource;
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
        LightRawActuator lightRawActuatorLivingRoom = new LightRawActuator();
        LightRawIntensity lightRawIntensityLivingRoom = new LightRawIntensity();
        LightRawColor lightRawColorLivingRoom = new LightRawColor();
        LightControllerRawSmartObject lightControllerRawSmartObjectLivingRoom = new LightControllerRawSmartObject(new LightControllerModel(lightRawActuatorLivingRoom, lightRawIntensityLivingRoom, lightRawColorLivingRoom));

        // List Smart Object in a Location
        List<SmartObjectResource> listSmartObjectLivingRoom = new ArrayList<SmartObjectResource>();
        listSmartObjectLivingRoom.add(presenceRawSensorLivingRoom);
        listSmartObjectLivingRoom.add(lightControllerRawSmartObjectLivingRoom);

        LocationRawDescriptor locationRawDescriptor = new LocationRawDescriptor(new LocationDescriptor(UUID.randomUUID().toString(), "living-room", "ground-floor", listSmartObjectLivingRoom));

        // Coap Resources
        CoapLocationResource livingRoomRootResource = new CoapLocationResource(deviceId,"living-room", locationRawDescriptor);

        CoapPresenceSensorResource coapPresenceSensorResource = new CoapPresenceSensorResource(deviceId, "presence-sensor", presenceRawSensorLivingRoom);
        CoapLightActuatorResource coapLightActuatorResource = new CoapLightActuatorResource(deviceId,"switch", lightRawActuatorLivingRoom);
        CoapLightIntensityParameterResource coapLightIntensityParameterResource = new CoapLightIntensityParameterResource(deviceId, "intensity", lightRawIntensityLivingRoom);
        CoapLightColorParameterResource coapLightColorParameterResource = new CoapLightColorParameterResource(deviceId, "color", lightRawColorLivingRoom);

        CoapLightControllerResource coapLightControllerResource = new CoapLightControllerResource(deviceId,"ligth-controller-1", lightControllerRawSmartObjectLivingRoom);
        coapLightControllerResource.add(coapLightActuatorResource);
        coapLightControllerResource.add(coapLightIntensityParameterResource);
        coapLightControllerResource.add(coapLightColorParameterResource);

        livingRoomRootResource.add(coapPresenceSensorResource);
        livingRoomRootResource.add(coapLightControllerResource);

        this.add(livingRoomRootResource);

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
                    childResource.getChildren().stream().forEach(childChildResource -> {
                        logger.info("\t Resource {} -> URI: {} (Observable: {})", childChildResource.getName(), childChildResource.getURI(), childChildResource.isObservable());
                    });
                });
            }
        });

    }
}
