package it.unimore.iot.smart.home.project.edge_application.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class DeviceDescriptor {

    private String id;

    private String name;

    @JsonProperty("software_version")
    private Double softwareVersion;

    private String manufacturer;

    private String host;

    private String port;

    @JsonProperty("resource_paths")
    private List<String> resourcePaths;

    private String type;

    public DeviceDescriptor() {
    }

    public DeviceDescriptor(String id, String name, Double softwareVersion, String manufacturer, String host, String port, List<String> resourcePaths, String type) {
        this.id = id;
        this.name = name;
        this.softwareVersion = softwareVersion;
        this.manufacturer = manufacturer;
        this.host = host;
        this.port = port;
        this.resourcePaths = resourcePaths;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(Double softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public List<String> getResourcePaths() {
        return resourcePaths;
    }

    public void setResourcePaths(List<String> resourcePaths) {
        this.resourcePaths = resourcePaths;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DeviceDescriptor{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", softwareVersion=").append(softwareVersion);
        sb.append(", manufacturer='").append(manufacturer).append('\'');
        sb.append(", host='").append(host).append('\'');
        sb.append(", port='").append(port).append('\'');
        sb.append(", resourcePaths=").append(resourcePaths);
        sb.append(", type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

