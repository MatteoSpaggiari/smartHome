package it.unimore.iot.smart.home.project.server.resource.model;

import it.unimore.iot.smart.home.project.server.resource.raw.SmartObjectResource;

import java.util.List;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 * @created 15/02/2021 - 11:50
 */
public class LocationDescriptor {

    private String id;

    private String room;

    private String floor;

    private List<SmartObjectResource> smartObjectResourceList;

    public LocationDescriptor(String id, String room, String floor, List<SmartObjectResource> smartObjectResourceList) {
        this.id = id;
        this.room = room;
        this.floor = floor;
        this.smartObjectResourceList = smartObjectResourceList;
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

    public List<SmartObjectResource> getSmartObjectResourceList() {
        return smartObjectResourceList;
    }

    public void setSmartObjectResourceList(List<SmartObjectResource> smartObjectResourceList) {
        this.smartObjectResourceList = smartObjectResourceList;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LocationDescriptor{");
        sb.append("id='").append(id).append('\'');
        sb.append(", room='").append(room).append('\'');
        sb.append(", floor='").append(floor).append('\'');
        sb.append(", smartObjectResourceList=").append(smartObjectResourceList);
        sb.append('}');
        return sb.toString();
    }
}
