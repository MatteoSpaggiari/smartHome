package it.unimore.iot.smart.home.project.simulated_devices.server.resource.model;

import it.unimore.iot.smart.home.project.simulated_devices.server.resource.raw.LightRawActuator;
import it.unimore.iot.smart.home.project.simulated_devices.server.resource.raw.LightRawColor;
import it.unimore.iot.smart.home.project.simulated_devices.server.resource.raw.LightRawIntensity;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 * @created 15/02/2021 - 11:10
 */
public class LightControllerModel {

    private LightRawActuator lightRawActuator;

    private LightRawIntensity lightRawIntensity;

    private LightRawColor lightRawColor;

    {
        this.lightRawActuator = new LightRawActuator();
        this.lightRawIntensity = new LightRawIntensity();
        this.lightRawColor = new LightRawColor();
    }

    public LightControllerModel(LightRawActuator lightRawActuator) {
        this.lightRawActuator = lightRawActuator;
    }

    public LightControllerModel(LightRawActuator lightRawActuator, LightRawIntensity lightRawIntensity) {
        this.lightRawActuator = lightRawActuator;
        this.lightRawIntensity = lightRawIntensity;
    }

    public LightControllerModel(LightRawActuator lightRawActuator, LightRawIntensity lightRawIntensity, LightRawColor lightRawColor) {
        this.lightRawActuator = lightRawActuator;
        this.lightRawIntensity = lightRawIntensity;
        this.lightRawColor = lightRawColor;
    }

    public LightRawActuator getLightRawActuator() {
        return lightRawActuator;
    }

    public void setLightRawActuator(LightRawActuator lightRawActuator) {
        this.lightRawActuator = lightRawActuator;
    }

    public LightRawIntensity getLightRawIntensity() {
        return lightRawIntensity;
    }

    public void setLightRawIntensity(LightRawIntensity lightRawIntensity) {
        this.lightRawIntensity = lightRawIntensity;
    }

    public LightRawColor getLightRawColor() {
        return lightRawColor;
    }

    public void setLightRawColor(LightRawColor lightRawColor) {
        this.lightRawColor = lightRawColor;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LightControllerModel{");
        sb.append("lightRawActuator=").append(lightRawActuator);
        sb.append(", lightRawIntensity=").append(lightRawIntensity);
        sb.append(", lightRawColor=").append(lightRawColor);
        sb.append('}');
        return sb.toString();
    }
}
