package it.unimore.iot.smart.home.project.edge_application.message;

import it.unimore.iot.smart.home.project.edge_application.utils.SenMLPack;

import java.util.HashMap;

public class MessageSenMLColor extends SenMLPack {

    private HashMap<String, Integer> color;

    {
        color = new HashMap<String, Integer>();
    }

    public HashMap<String, Integer> getValue() {

        // Red
        this.forEach(senMLRecord -> {
            if(senMLRecord.getN() != null && senMLRecord.getN().equals("red")) {
                int value = (Integer) senMLRecord.getV();
                color.put(senMLRecord.getN(), value);
            }
        });

        // Green
        this.forEach(senMLRecord -> {
            if(senMLRecord.getN() != null && senMLRecord.getN().equals("green")) {
                int value = (Integer) senMLRecord.getV();
                color.put(senMLRecord.getN(), value);
            }
        });

        // Blue
        this.forEach(senMLRecord -> {
            if(senMLRecord.getN() != null && senMLRecord.getN().equals("blue")) {
                int value = (Integer) senMLRecord.getV();
                color.put(senMLRecord.getN(), value);
            }
        });

        return this.color;

    }

}
