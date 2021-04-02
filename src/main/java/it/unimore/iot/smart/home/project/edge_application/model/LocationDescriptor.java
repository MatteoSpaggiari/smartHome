package it.unimore.iot.smart.home.project.edge_application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class LocationDescriptor {

    private String id;

    private String room;

    private String floor;

    private HashMap<String, DeviceDescriptor> devices;

    @JsonProperty("policy")
    private PolicyDescriptor policyDescriptor;

    {
        this.devices = new HashMap<String, DeviceDescriptor>();
    }

    public LocationDescriptor() {
        this.policyDescriptor = new PolicyDescriptor();
    }

    public LocationDescriptor(String id, String room, String floor, PolicyDescriptor policyDescriptor) {
        this.id = id;
        this.room = room;
        this.floor = floor;
        this.policyDescriptor = policyDescriptor;
    }

    public LocationDescriptor(String id, String room, String floor, HashMap<String, DeviceDescriptor> devices, PolicyDescriptor policyDescriptor) {
        this.id = id;
        this.room = room;
        this.floor = floor;
        this.devices = devices;
        this.policyDescriptor = policyDescriptor;
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

    public HashMap<String, DeviceDescriptor> getDevices() {
        return devices;
    }

    public void setDevices(HashMap<String, DeviceDescriptor> devices) {
        this.devices = devices;
    }

    public PolicyDescriptor getPolicyDescriptor() {
        return policyDescriptor;
    }

    public void setPolicyDescriptor(PolicyDescriptor policyDescriptor) {
        this.policyDescriptor = policyDescriptor;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LocationDescriptor{");
        sb.append("id='").append(id).append('\'');
        sb.append(", room='").append(room).append('\'');
        sb.append(", floor='").append(floor).append('\'');
        sb.append(", devices=").append(devices);
        sb.append(", policyManagerDescriptor=").append(policyDescriptor);
        sb.append('}');
        return sb.toString();
    }

    public static void main(String[] args) {
        PresenceSensorDescriptor presenceSensorDescriptor = new PresenceSensorDescriptor();
        LightControllerDescriptor lightControllerDescriptor = new LightControllerDescriptor();
        System.out.println(presenceSensorDescriptor);
        System.out.println(lightControllerDescriptor);
        LocationDescriptor locationDescriptor = new LocationDescriptor();
        System.out.println(locationDescriptor);
        locationDescriptor.getDevices().put(UUID.randomUUID().toString(), presenceSensorDescriptor);
        locationDescriptor.getDevices().put(UUID.randomUUID().toString(), lightControllerDescriptor);
        System.out.println(locationDescriptor);
    }
}
