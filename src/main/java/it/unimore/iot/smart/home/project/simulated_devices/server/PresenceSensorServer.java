package it.unimore.iot.smart.home.project.simulated_devices.server;

import it.unimore.iot.smart.home.project.simulated_devices.server.resource.coap.*;
import it.unimore.iot.smart.home.project.simulated_devices.server.resource.raw.*;
import org.eclipse.californium.core.CoapServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class PresenceSensorServer extends CoapServer {

    private final static Logger logger = LoggerFactory.getLogger(PresenceSensorServer.class);

    public PresenceSensorServer(int port, String id, Boolean active) {

        super(port);
        String deviceId = String.format("dipi:iot:%s", id);

        //INIT Emulated Physical Sensors and Actuators
        PresenceRawSensor presenceRawSensor = new PresenceRawSensor();

        // Coap Resource
        CoapPresenceSensorResource coapPresenceSensorResource = new CoapPresenceSensorResource(deviceId, "presence-sensor", presenceRawSensor);

        this.add(coapPresenceSensorResource);

    }

}
