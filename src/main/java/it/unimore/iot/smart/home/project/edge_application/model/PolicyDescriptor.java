package it.unimore.iot.smart.home.project.edge_application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.UUID;

public class PolicyDescriptor {

    private String id;

    private boolean active;

    @JsonProperty("light_intensity")
    private Double lightIntensity;

    @JsonProperty("light_color")
    private HashMap<String, Integer> lightColor;

    @JsonProperty("sweet_night")
    private boolean sweetNight;

    // Default Policy
    {
        this.id = UUID.randomUUID().toString();
        this.active = false;
        this.lightIntensity = 80.0;
        this.lightColor = new HashMap<String, Integer>();
        this.lightColor.put("Red", 255);
        this.lightColor.put("Green", 255);
        this.lightColor.put("Blue", 255);
        this.sweetNight = false;
    }

    public PolicyDescriptor() {
    }

    public PolicyDescriptor(String id, boolean active, Double lightIntensity, HashMap<String, Integer> lightColor, boolean sweetNight) {
        this.id = id;
        this.active = active;
        this.lightIntensity = lightIntensity;
        this.lightColor = lightColor;
        this.sweetNight = sweetNight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Double getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(Double lightIntensity) {
        this.lightIntensity = lightIntensity;
    }

    public HashMap<String, Integer> getLightColor() {
        return lightColor;
    }

    public void setLightColor(HashMap<String, Integer> lightColor) {
        this.lightColor = lightColor;
    }

    public boolean isSweetNight() {
        return sweetNight;
    }

    public void setSweetNight(boolean sweetNight) {
        this.sweetNight = sweetNight;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PolicyManagerDescriptor{");
        sb.append("id='").append(id).append('\'');
        sb.append(", active=").append(active);
        sb.append(", lightIntensity=").append(lightIntensity);
        sb.append(", lightColor=").append(lightColor);
        sb.append(", sweetNight=").append(sweetNight);
        sb.append('}');
        return sb.toString();
    }
}
