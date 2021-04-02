package it.unimore.iot.smart.home.project.edge_application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class PresenceSensorDescriptor extends DeviceDescriptor {

    @JsonProperty("presence")
    private Boolean isPresence;

    {
        this.isPresence = false;
    }

    public PresenceSensorDescriptor() {
    }

    public PresenceSensorDescriptor(String id, String name, Double software_version, String manufacturer, String host, String port, String path, String type, Boolean isPresence) {
        super(id, name, software_version, manufacturer, host, port, path, type);
        this.isPresence = isPresence;
    }

    public Boolean getPresence() {
        return isPresence;
    }

    public void setPresence(Boolean presence) {
        isPresence = presence;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PresenceSensorDescriptor{");
        sb.append("isPresence=").append(isPresence);
        sb.append('}');
        return sb.toString();
    }
}
