package it.unimore.iot.smart.home.project.edge_application.persistence;


import it.unimore.iot.smart.home.project.edge_application.exception.IoTInventoryDataManagerConflict;
import it.unimore.iot.smart.home.project.edge_application.exception.IoTInventoryDataManagerException;
import it.unimore.iot.smart.home.project.edge_application.model.DeviceDescriptor;
import it.unimore.iot.smart.home.project.edge_application.model.LocationDescriptor;
import it.unimore.iot.smart.home.project.edge_application.model.PolicyDescriptor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class DefaultIotInventoryDataManger implements IIotInventoryDataManager {

    protected HashMap<String, LocationDescriptor> locationMap;

    public DefaultIotInventoryDataManger() {
        this.locationMap = new HashMap<>();
    }

    @Override
    public List<LocationDescriptor> getLocationsList() throws IoTInventoryDataManagerException {
        return new ArrayList<>(this.locationMap.values());
    }

    @Override
    public List<LocationDescriptor> getLocationsListByFloor(String floor) throws IoTInventoryDataManagerException {
        return this.locationMap.values().stream()
                .filter(locationDescriptor -> locationDescriptor != null && locationDescriptor.getFloor().equals(floor))
                .collect(Collectors.toList());
    }

    @Override
    public List<LocationDescriptor> getLocationsListByRoom(String room) throws IoTInventoryDataManagerException {
        return this.locationMap.values().stream()
                .filter(locationDescriptor -> locationDescriptor != null && locationDescriptor.getRoom().equals(room))
                .collect(Collectors.toList());
    }

    @Override
    public List<LocationDescriptor> getLocationsByFloorAndRoom(String floor, String room) throws IoTInventoryDataManagerException {
        return this.locationMap.values().stream()
                .filter(locationDescriptor -> locationDescriptor != null && locationDescriptor.getFloor().equals(floor) && locationDescriptor.getRoom().equals(room))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LocationDescriptor> getLocation(String locationId) throws IoTInventoryDataManagerException {
        return Optional.ofNullable(this.locationMap.get(locationId));
    }

    @Override
    public LocationDescriptor createNewLocation(LocationDescriptor locationDescriptor) throws IoTInventoryDataManagerException, IoTInventoryDataManagerConflict {

        if(locationDescriptor.getRoom() != null && locationDescriptor.getFloor() != null && this.getLocationsByFloorAndRoom(locationDescriptor.getFloor(), locationDescriptor.getRoom()).size() > 0)
            throw new IoTInventoryDataManagerConflict("Location with the same name and on the same floor already available!");

        //Set the locationId to a random UUID value
        if(locationDescriptor.getId() == null)
            locationDescriptor.setId(UUID.randomUUID().toString());

        this.locationMap.put(locationDescriptor.getId(), locationDescriptor);
        return locationDescriptor;

    }

    @Override
    public LocationDescriptor updateLocation(LocationDescriptor locationDescriptor) throws IoTInventoryDataManagerException {
        this.locationMap.put(locationDescriptor.getId(), locationDescriptor);
        return locationDescriptor;
    }

    @Override
    public LocationDescriptor deleteLocation(String locationId) throws IoTInventoryDataManagerException {
        return this.locationMap.remove(locationId);
    }


    @Override
    public List<DeviceDescriptor> getDevicesList(String locationId) throws IoTInventoryDataManagerException {
        return new ArrayList<>(this.locationMap.get(locationId).getDevices().values());
    }

    @Override
    public List<DeviceDescriptor> getDevicesListByType(String locationId, String deviceType) throws IoTInventoryDataManagerException {

        List<DeviceDescriptor> resultList = this.getDevicesList(locationId);

        if(deviceType != null)
            resultList = this.locationMap.get(locationId).getDevices().values().stream()
                    .filter(deviceDescriptor -> deviceDescriptor != null && deviceDescriptor.getType().equals(deviceType))
                    .collect(Collectors.toList());

        return resultList;
    }

    @Override
    public Optional<DeviceDescriptor> getDevice(String locationId, String deviceId) throws IoTInventoryDataManagerException {
        return this.locationMap.get(locationId).getDevices().values().stream()
                .filter(deviceDescriptor -> deviceDescriptor.getId().equals(deviceId))
                .findAny();
    }

    @Override
    public DeviceDescriptor createNewDevice(String locationId, DeviceDescriptor deviceDescriptor) throws IoTInventoryDataManagerException {

        //Set the locationId to a random UUID value
        deviceDescriptor.setId(UUID.randomUUID().toString());

        this.locationMap.get(locationId).getDevices().put(deviceDescriptor.getId(), deviceDescriptor);

        return deviceDescriptor;
    }

    @Override
    public DeviceDescriptor updateDevice(String locationId, DeviceDescriptor deviceDescriptor) throws IoTInventoryDataManagerException {

        this.locationMap.get(locationId).getDevices().put(deviceDescriptor.getId(), deviceDescriptor);

        return deviceDescriptor;
    }

    @Override
    public DeviceDescriptor deleteDevice(String locationId, String deviceId) throws IoTInventoryDataManagerException {
        return this.locationMap.get(locationId).getDevices().remove(deviceId);
    }

    @Override
    public Optional<PolicyDescriptor> getPolicy(String locationId) throws IoTInventoryDataManagerException {
        return Optional.ofNullable(this.locationMap.get(locationId).getPolicyDescriptor());
    }

    @Override
    public PolicyDescriptor updatePolicy(String locationId, PolicyDescriptor policyDescriptor) throws IoTInventoryDataManagerException {
        this.locationMap.get(locationId).setPolicyDescriptor(policyDescriptor);
        return policyDescriptor;
    }
}
