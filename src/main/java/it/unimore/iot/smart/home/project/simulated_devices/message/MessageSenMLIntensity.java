package it.unimore.iot.smart.home.project.simulated_devices.message;

import it.unimore.iot.smart.home.project.simulated_devices.utils.SenMLPack;

public class MessageSenMLIntensity extends SenMLPack {

    public Double getValue() {
        return (Double) this.get(0).getV();
    }

}
