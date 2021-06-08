package it.unimore.iot.smart.home.project.simulated_devices.message;

import it.unimore.iot.smart.home.project.simulated_devices.utils.SenMLPack;

public class MessageSenMLSwitch extends SenMLPack {

    public boolean getValue() {
        return this.get(0).getVb();
    }

}
