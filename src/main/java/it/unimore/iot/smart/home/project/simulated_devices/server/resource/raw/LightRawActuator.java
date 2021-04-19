package it.unimore.iot.smart.home.project.simulated_devices.server.resource.raw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class LightRawActuator extends SmartObjectResource<Boolean> {

    private static Logger logger = LoggerFactory.getLogger(LightRawActuator.class);

    private static final String LOG_DISPLAY_NAME = "LightActuator";

    private static final String RESOURCE_TYPE = "iot:actuator:light";

    private Boolean isActive;

    {
        this.isActive = false;
    }

    public LightRawActuator() {
        super(UUID.randomUUID().toString(), RESOURCE_TYPE);
    }

    public LightRawActuator(Boolean active) {
        super(UUID.randomUUID().toString(), RESOURCE_TYPE);
        this.isActive = active;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
        notifyUpdate(isActive);
    }

    @Override
    public Boolean loadUpdatedValue() {
        return this.isActive;
    }

}
