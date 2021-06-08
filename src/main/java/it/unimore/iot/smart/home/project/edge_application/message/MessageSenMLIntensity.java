package it.unimore.iot.smart.home.project.edge_application.message;

import it.unimore.iot.smart.home.project.edge_application.utils.SenMLPack;

public class MessageSenMLIntensity extends SenMLPack {

    public Double getValue() {
        return (Double) this.get(0).getV();
    }

}
