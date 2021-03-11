package it.unimore.iot.smart.home.project.server_http.persistence;


import it.unimore.iot.smart.home.project.server_http.exception.IoTInventoryDataManagerConflict;
import it.unimore.iot.smart.home.project.server_http.exception.IoTInventoryDataManagerException;
import it.unimore.iot.smart.home.project.server_http.model.DeviceDescriptor;
import it.unimore.iot.smart.home.project.server_http.model.LocationDescriptor;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * Demo IoT Inventory Data Manager handling all data in a local cache implemented through Maps and Lists
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project http-iot-api-demo
 * @created 05/10/2020 - 11:48
 */
public class DefaultIotInventoryDataManger implements IIotInventoryDataManager {

    private HashMap<String, LocationDescriptor> locationMap;

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
    public Optional<LocationDescriptor> getLocationByRoom(String room) throws IoTInventoryDataManagerException {
        return this.locationMap.values().stream()
                .filter(locationDescriptor -> locationDescriptor.getRoom().equals(room))
                .findAny();
    }

    @Override
    public Optional<LocationDescriptor> getLocation(String locationId) throws IoTInventoryDataManagerException {
        return Optional.ofNullable(this.locationMap.get(locationId));
    }

    @Override
    public LocationDescriptor createNewLocation(LocationDescriptor locationDescriptor) throws IoTInventoryDataManagerException, IoTInventoryDataManagerConflict {

        if(locationDescriptor.getRoom() != null && this.getLocationByRoom(locationDescriptor.getRoom()).isPresent())
            throw new IoTInventoryDataManagerConflict("Location with the same name already available!");

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
    public List<DeviceDescriptor> getDevicesList() throws IoTInventoryDataManagerException {
        return null;
    }

    @Override
    public List<DeviceDescriptor> getDevicesListByType(String type) throws IoTInventoryDataManagerException {
        return null;
    }

    @Override
    public Optional<DeviceDescriptor> getDeviceByName(String name) throws IoTInventoryDataManagerException {
        return Optional.empty();
    }

    @Override
    public Optional<DeviceDescriptor> getDevice(String deviceId) throws IoTInventoryDataManagerException {
        return Optional.empty();
    }

    @Override
    public DeviceDescriptor createNewDevice(DeviceDescriptor deviceDescriptor) throws IoTInventoryDataManagerException, IoTInventoryDataManagerConflict {
        return null;
    }

    @Override
    public DeviceDescriptor updateDevice(DeviceDescriptor deviceDescriptor) throws IoTInventoryDataManagerException {
        return null;
    }

    @Override
    public DeviceDescriptor deleteDevice(String deviceId) throws IoTInventoryDataManagerException {
        return null;
    }
}
