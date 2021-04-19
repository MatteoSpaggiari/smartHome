package it.unimore.iot.smart.home.project.edge_application.dto;

import it.unimore.iot.smart.home.project.edge_application.model.LocationDescriptor;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class LocationUpdateRequest {

    private String id;

    private String room;

    private String floor;

    public LocationUpdateRequest() {
    }

    public LocationUpdateRequest(String id, String room, String floor) {
        this.id = id;
        this.room = room;
        this.floor = floor;
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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LocationUpdateRequest{");
        sb.append("id='").append(id).append('\'');
        sb.append(", room='").append(room).append('\'');
        sb.append(", floor='").append(floor).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
