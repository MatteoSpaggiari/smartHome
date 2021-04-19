package it.unimore.iot.smart.home.project.simulated_devices.server.resource.raw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.UUID;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class LightRawIntensity extends SmartObjectResource<Double> {

    private static Logger logger = LoggerFactory.getLogger(LightRawIntensity.class);

    private static final String LOG_DISPLAY_NAME = "LightIntensity";

    private static final String RESOURCE_TYPE = "iot:light:intensity";

    private Double updatedValue;

    {
        this.updatedValue = 100.0;
    }

    public LightRawIntensity() {
        super(UUID.randomUUID().toString(), RESOURCE_TYPE);
    }

    public LightRawIntensity(Double intensity) {
        super(UUID.randomUUID().toString(), RESOURCE_TYPE);
        this.updatedValue = intensity;
    }

    public Double getUpdatedValue() {
        return updatedValue;
    }

    public void setUpdatedValue(Double updatedValue) {
        this.updatedValue = updatedValue;
        notifyUpdate(updatedValue);
    }

    @Override
    public Double loadUpdatedValue() {
        return this.updatedValue;
    }

}
