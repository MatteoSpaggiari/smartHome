package it.unimore.iot.smart.home.project.server.resource.model;

import java.util.HashMap;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 * @created 15/02/2021 - 11:10
 */
public class LightControllerModel {

    private boolean isActive;

    // A value from 0 to 100
    private Double intensity;

    // RGB - each color channel from 0 to 255
    private HashMap<String, Integer> color;

    // Set default values
    {
        this.isActive = false;
        this.intensity = 100.0;
        color = new HashMap<String, Integer>();
        color.put("Red", 255);
        color.put("Green", 255);
        color.put("Blue", 255);
    }

    public LightControllerModel() {}

    public LightControllerModel(boolean isActive, Double intensity, HashMap<String, Integer> color, String locationResourceUri) {
        this.isActive = isActive;
        this.intensity = intensity;
        this.color = color;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Double getIntensity() {
        return intensity;
    }

    public void setIntensity(Double intensity) {
        this.intensity = intensity;
    }

    public HashMap<String, Integer> getColor() {
        return color;
    }

    public void setColor(HashMap<String, Integer> color) {
        this.color = color;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LightControllerModel{");
        sb.append("isActive=").append(isActive);
        sb.append(", intensity=").append(intensity);
        sb.append(", color=").append(color);
        sb.append('}');
        return sb.toString();
    }
}
