package it.unimore.iot.smart.home.project.server_http.model;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class PresenceSensorDescriptor extends DeviceDescriptor {

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

    public Boolean getActive() {
        return isPresence;
    }

    public void setActive(Boolean active) {
        isPresence = active;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PresenceSensorDescriptor{");
        sb.append("isActive=").append(isPresence);
        sb.append('}');
        return sb.toString();
    }
}
