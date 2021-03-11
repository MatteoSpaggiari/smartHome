package it.unimore.iot.smart.home.project.server_http.model;

import java.util.List;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class LocationDescriptor {

    private String id;

    private String room;

    private String floor;

    private List<DeviceDescriptor> devices;

    public LocationDescriptor() {
    }

    public LocationDescriptor(String id, String room, String floor, List<DeviceDescriptor> devices) {
        this.id = id;
        this.room = room;
        this.floor = floor;
        this.devices = devices;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public List<DeviceDescriptor> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceDescriptor> devices) {
        this.devices = devices;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LocationDescriptor{");
        sb.append("id='").append(id).append('\'');
        sb.append(", room='").append(room).append('\'');
        sb.append(", floor='").append(floor).append('\'');
        sb.append(", devices=").append(devices);
        sb.append('}');
        return sb.toString();
    }
}
