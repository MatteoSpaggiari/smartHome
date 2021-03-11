package it.unimore.iot.smart.home.project.server_http.model;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class DeviceDescriptor {

    private String id;

    private String name;

    private Double software_version;

    private String manufacturer;

    private String host;

    private String port;

    private String path;

    private String type;

    public DeviceDescriptor() {
    }

    public DeviceDescriptor(String id, String name, Double software_version, String manufacturer, String host, String port, String path, String type) {
        this.id = id;
        this.name = name;
        this.software_version = software_version;
        this.manufacturer = manufacturer;
        this.host = host;
        this.port = port;
        this.path = path;
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

    public Double getSoftware_version() {
        return software_version;
    }

    public void setSoftware_version(Double software_version) {
        this.software_version = software_version;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
        sb.append(", software_version=").append(software_version);
        sb.append(", manufacturer='").append(manufacturer).append('\'');
        sb.append(", host='").append(host).append('\'');
        sb.append(", port='").append(port).append('\'');
        sb.append(", path='").append(path).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

