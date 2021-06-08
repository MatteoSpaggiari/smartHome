package it.unimore.iot.smart.home.project.edge_application.message;

import it.unimore.iot.smart.home.project.edge_application.utils.SenMLPack;

public class MessageSenMLSwitch extends SenMLPack {

    public boolean getValue() {
        return this.get(0).getVb();
    }

}
