package it.unimore.iot.smart.home.project.server_http.persistence;

import it.unimore.iot.smart.home.project.server_http.exception.IoTInventoryDataManagerConflict;
import it.unimore.iot.smart.home.project.server_http.exception.IoTInventoryDataManagerException;
import it.unimore.iot.smart.home.project.server_http.model.DeviceDescriptor;
import it.unimore.iot.smart.home.project.server_http.model.LocationDescriptor;

import java.util.List;
import java.util.Optional;


/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public interface IIotInventoryDataManager {

    //Location Management

    public List<LocationDescriptor> getLocationsList() throws IoTInventoryDataManagerException;

    public List<LocationDescriptor> getLocationsListByFloor(String floor) throws IoTInventoryDataManagerException;

    public Optional<LocationDescriptor> getLocationByRoom(String name) throws IoTInventoryDataManagerException;

    public Optional<LocationDescriptor> getLocation(String locationId) throws IoTInventoryDataManagerException;

    public LocationDescriptor createNewLocation(LocationDescriptor locationDescriptor) throws IoTInventoryDataManagerException, IoTInventoryDataManagerConflict;

    public LocationDescriptor updateLocation(LocationDescriptor locationDescriptor) throws IoTInventoryDataManagerException;

    public LocationDescriptor deleteLocation(String locationId) throws IoTInventoryDataManagerException;

    //Devices in Location Management

    public List<DeviceDescriptor> getDevicesList() throws IoTInventoryDataManagerException;

    public List<DeviceDescriptor> getDevicesListByType(String type) throws IoTInventoryDataManagerException;

    public Optional<DeviceDescriptor> getDeviceByName(String name) throws IoTInventoryDataManagerException;

    public Optional<DeviceDescriptor> getDevice(String deviceId) throws IoTInventoryDataManagerException;

    public DeviceDescriptor createNewDevice(DeviceDescriptor deviceDescriptor) throws IoTInventoryDataManagerException, IoTInventoryDataManagerConflict;

    public DeviceDescriptor updateDevice(DeviceDescriptor deviceDescriptor) throws IoTInventoryDataManagerException;

    public DeviceDescriptor deleteDevice(String deviceId) throws IoTInventoryDataManagerException;

}
