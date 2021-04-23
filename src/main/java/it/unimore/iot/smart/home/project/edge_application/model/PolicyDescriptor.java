package it.unimore.iot.smart.home.project.edge_application.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class PolicyDescriptor {

    private String id;

    private boolean active;

    @JsonProperty("light_intensity")
    private Double lightIntensity;

    @JsonProperty("light_color")
    private HashMap<String, Integer> lightColor;

    @JsonProperty("sweet_night")
    private boolean sweetNight;

    private Double lightIntensitySweetNight;

    private HashMap<String, Integer> lightColorSweetNight;

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
        this.lightIntensitySweetNight = 30.0;
        this.lightColorSweetNight = new HashMap<String, Integer>();
        // Light blue
        this.lightColorSweetNight.put("Red", 199);
        this.lightColorSweetNight.put("Green", 240);
        this.lightColorSweetNight.put("Blue", 255);
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

    public PolicyDescriptor(String id, boolean active, Double lightIntensity, HashMap<String, Integer> lightColor, boolean sweetNight, Double lightIntensitySweetNight, HashMap<String, Integer> lightColorSweetNight) {
        this.id = id;
        this.active = active;
        this.lightIntensity = lightIntensity;
        this.lightColor = lightColor;
        this.sweetNight = sweetNight;
        this.lightIntensitySweetNight = lightIntensitySweetNight;
        this.lightColorSweetNight = lightColorSweetNight;
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

    public Double getLightIntensitySweetNight() {
        return lightIntensitySweetNight;
    }

    public void setLightIntensitySweetNight(Double lightIntensitySweetNight) {
        this.lightIntensitySweetNight = lightIntensitySweetNight;
    }

    public HashMap<String, Integer> getLightColorSweetNight() {
        return lightColorSweetNight;
    }

    public void setLightColorSweetNight(HashMap<String, Integer> lightColorSweetNight) {
        this.lightColorSweetNight = lightColorSweetNight;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PolicyDescriptor{");
        sb.append("id='").append(id).append('\'');
        sb.append(", active=").append(active);
        sb.append(", lightIntensity=").append(lightIntensity);
        sb.append(", lightColor=").append(lightColor);
        sb.append(", sweetNight=").append(sweetNight);
        sb.append(", lightIntensitySweetNight=").append(lightIntensitySweetNight);
        sb.append(", lightColorSweetNight=").append(lightColorSweetNight);
        sb.append('}');
        return sb.toString();
    }
}
