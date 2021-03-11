package it.unimore.iot.smart.home.project.simulated_environment.server;

import it.unimore.iot.smart.home.project.simulated_environment.server.resource.coap.*;
import it.unimore.iot.smart.home.project.simulated_environment.server.resource.model.LightControllerModel;
import it.unimore.iot.smart.home.project.simulated_environment.server.resource.raw.*;
import org.eclipse.californium.core.CoapServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightControllerServer extends CoapServer {

    private final static Logger logger = LoggerFactory.getLogger(LightControllerServer.class);

    public LightControllerServer(int port, String id) {

        super(port);
        String deviceId = String.format("dipi:iot:%s", id);

        //INIT Emulated Physical Sensors and Actuators
        LightRawActuator lightRawActuator = new LightRawActuator();
        LightRawIntensity lightRawIntensity = new LightRawIntensity();
        LightRawColor lightRawColor = new LightRawColor();
        LightControllerRawSmartObject lightControllerRawSmartObject = new LightControllerRawSmartObject(new LightControllerModel(lightRawActuator, lightRawIntensity, lightRawColor));

        // Coap Resources
        CoapLightActuatorResource coapLightActuatorResource = new CoapLightActuatorResource(deviceId,"switch", lightRawActuator);
        CoapLightIntensityParameterResource coapLightIntensityParameterResource = new CoapLightIntensityParameterResource(deviceId, "intensity", lightRawIntensity);
        CoapLightColorParameterResource coapLightColorParameterResource = new CoapLightColorParameterResource(deviceId, "color", lightRawColor);

        CoapLightControllerResource coapLightControllerResource = new CoapLightControllerResource(deviceId,"ligth-controller", lightControllerRawSmartObject);
        coapLightControllerResource.add(coapLightActuatorResource);
        coapLightControllerResource.add(coapLightIntensityParameterResource);
        coapLightControllerResource.add(coapLightColorParameterResource);

        this.add(coapLightControllerResource);

    }

}
