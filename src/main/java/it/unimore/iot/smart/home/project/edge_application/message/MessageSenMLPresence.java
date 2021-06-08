package it.unimore.iot.smart.home.project.edge_application.message;

import it.unimore.iot.smart.home.project.edge_application.utils.SenMLPack;

public class MessageSenMLPresence extends SenMLPack {

    public boolean getValue() {
        return this.get(0).getVb();
    }

}
