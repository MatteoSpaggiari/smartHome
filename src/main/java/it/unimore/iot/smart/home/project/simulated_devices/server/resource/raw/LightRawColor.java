package it.unimore.iot.smart.home.project.simulated_devices.server.resource.raw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class LightRawColor extends SmartObjectResource<HashMap<String, Integer>> {

    private static Logger logger = LoggerFactory.getLogger(LightRawColor.class);

    private static final String LOG_DISPLAY_NAME = "LightColor";

    private static final String RESOURCE_TYPE = "iot:light:color";

    private HashMap<String, Integer> updatedValue;

    {
        this.updatedValue = new HashMap<String, Integer>();
        this.updatedValue.put("Red", 255);
        this.updatedValue.put("Green", 255);
        this.updatedValue.put("Blue", 255);
    }

    public LightRawColor() {
        super(UUID.randomUUID().toString(), RESOURCE_TYPE);
    }

    public LightRawColor(Integer red, Integer green, Integer blue) {
        super(UUID.randomUUID().toString(), RESOURCE_TYPE);
        this.updatedValue = new HashMap<String, Integer>();
        this.updatedValue.put("Red", red);
        this.updatedValue.put("Green", green);
        this.updatedValue.put("Blue", blue);
    }

    public HashMap<String, Integer> getUpdatedValue() {
        return updatedValue;
    }

    public void setUpdatedValue(HashMap<String, Integer> updatedValue) {
        this.updatedValue = updatedValue;
        notifyUpdate(updatedValue);
    }

    @Override
    public HashMap<String, Integer> loadUpdatedValue() {
        return this.updatedValue;
    }
}
