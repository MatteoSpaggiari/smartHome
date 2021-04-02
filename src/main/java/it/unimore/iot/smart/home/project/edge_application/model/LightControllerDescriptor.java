package it.unimore.iot.smart.home.project.edge_application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class LightControllerDescriptor extends DeviceDescriptor {

    @JsonProperty("active")
    private Boolean isActive;

    private Double intensity;

    private HashMap<String, Integer> color;

    {
        this.isActive = false;
        this.intensity = 100.0;
        this.color = new HashMap<String, Integer>();
        this.color.put("Red", 255);
        this.color.put("Green", 255);
        this.color.put("Blue", 255);
    }

    public LightControllerDescriptor() {
    }

    public LightControllerDescriptor(String id, String name, Double software_version, String manufacturer, String host, String port, String path, String type, Boolean isActive, Double intensity, HashMap<String, Integer> color) {
        super(id, name, software_version, manufacturer, host, port, path, type);
        this.isActive = isActive;
        this.intensity = intensity;
        this.color = color;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
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
        final StringBuffer sb = new StringBuffer("LightControllerDescriptor{");
        sb.append("isActive=").append(isActive);
        sb.append(", intensity=").append(intensity);
        sb.append(", color=").append(color);
        sb.append('}');
        return sb.toString();
    }
}
