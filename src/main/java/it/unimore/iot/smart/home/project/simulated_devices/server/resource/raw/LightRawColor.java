package it.unimore.iot.smart.home.project.simulated_devices.server.resource.raw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.UUID;

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
