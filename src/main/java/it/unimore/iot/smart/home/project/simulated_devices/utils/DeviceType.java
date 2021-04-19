package it.unimore.iot.smart.home.project.simulated_devices.utils;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public enum DeviceType {

    PRESENCE_SENSOR("presence_sensor"),
    LIGHT_CONTROLLER("light_controller");

    private String value;

    DeviceType() {}

    DeviceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
