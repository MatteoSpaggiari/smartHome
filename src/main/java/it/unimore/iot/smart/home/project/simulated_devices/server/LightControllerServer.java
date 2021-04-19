package it.unimore.iot.smart.home.project.simulated_devices.server;

import it.unimore.iot.smart.home.project.simulated_devices.server.resource.coap.*;
import it.unimore.iot.smart.home.project.simulated_devices.server.resource.raw.*;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class LightControllerServer extends CoapServer {

    private final static Logger logger = LoggerFactory.getLogger(LightControllerServer.class);

    public LightControllerServer(int port, String id, Boolean active, Double intensity, Integer red, Integer green, Integer blue) {

        super(port);
        String deviceId = String.format("dipi:iot:%s", id);

        //INIT Emulated Physical Sensors and Actuators
        LightRawActuator lightRawActuator = new LightRawActuator(active);
        LightRawIntensity lightRawIntensity = new LightRawIntensity(intensity);
        LightRawColor lightRawColor = new LightRawColor(red, green, blue);

        // Coap Resources
        CoapLightActuatorResource coapLightActuatorResource = new CoapLightActuatorResource(deviceId,"switch", lightRawActuator);
        CoapLightIntensityParameterResource coapLightIntensityParameterResource = new CoapLightIntensityParameterResource(deviceId, "intensity", lightRawIntensity);
        CoapLightColorParameterResource coapLightColorParameterResource = new CoapLightColorParameterResource(deviceId, "color", lightRawColor);

        CoapResource coapLightControllerResource = new CoapResource("light-controller");
        coapLightControllerResource.add(coapLightActuatorResource);
        coapLightControllerResource.add(coapLightIntensityParameterResource);
        coapLightControllerResource.add(coapLightColorParameterResource);

        this.add(coapLightControllerResource);

    }

}
