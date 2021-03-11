package it.unimore.iot.smart.home.project.server_http.model;

import java.util.List;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class BuildingDescriptor {

    private String id;

    private String nation;

    private String city;

    private String address;

    private List<LocationDescriptor> locations;

    public BuildingDescriptor() {
    }

    public BuildingDescriptor(String id, String nation, String city, String address, List<LocationDescriptor> locations) {
        this.id = id;
        this.nation = nation;
        this.city = city;
        this.address = address;
        this.locations = locations;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<LocationDescriptor> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationDescriptor> locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BuildingDescriptor{");
        sb.append("id='").append(id).append('\'');
        sb.append(", nation='").append(nation).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", locations=").append(locations);
        sb.append('}');
        return sb.toString();
    }
}
