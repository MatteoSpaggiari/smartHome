package it.unimore.iot.smart.home.project.edge_application.persistence;

import it.unimore.iot.smart.home.project.edge_application.exception.IoTInventoryDataManagerConflict;
import it.unimore.iot.smart.home.project.edge_application.exception.IoTInventoryDataManagerException;
import it.unimore.iot.smart.home.project.edge_application.model.DeviceDescriptor;
import it.unimore.iot.smart.home.project.edge_application.model.LocationDescriptor;
import it.unimore.iot.smart.home.project.edge_application.model.PolicyDescriptor;

import java.util.List;
import java.util.Optional;


/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public interface IIotInventoryDataManager {

    // Location Management

    public List<LocationDescriptor> getLocationsList() throws IoTInventoryDataManagerException;

    public List<LocationDescriptor> getLocationsListByFloor(String floor) throws IoTInventoryDataManagerException;

    public List<LocationDescriptor> getLocationsListByRoom(String room) throws IoTInventoryDataManagerException;

    public Optional<LocationDescriptor> getLocation(String locationId) throws IoTInventoryDataManagerException;

    public List<LocationDescriptor> getLocationsByFloorAndRoom(String floor, String room) throws IoTInventoryDataManagerException;

    public LocationDescriptor createNewLocation(LocationDescriptor locationDescriptor) throws IoTInventoryDataManagerException, IoTInventoryDataManagerConflict;


    public LocationDescriptor updateLocation(LocationDescriptor locationDescriptor) throws IoTInventoryDataManagerException;

    public LocationDescriptor deleteLocation(String locationId) throws IoTInventoryDataManagerException;

    // Devices in Location Management

    public List<DeviceDescriptor> getDevicesList(String locationId) throws IoTInventoryDataManagerException;

    public List<DeviceDescriptor> getDevicesListByType(String locationId, String type) throws IoTInventoryDataManagerException;

    public Optional<DeviceDescriptor> getDevice(String locationId, String deviceId) throws IoTInventoryDataManagerException;

    public DeviceDescriptor createNewDevice(String locationId, DeviceDescriptor deviceDescriptor) throws IoTInventoryDataManagerException, IoTInventoryDataManagerConflict;

    public DeviceDescriptor updateDevice(String locationId, DeviceDescriptor deviceDescriptor) throws IoTInventoryDataManagerException;

    public DeviceDescriptor deleteDevice(String locationId, String deviceId) throws IoTInventoryDataManagerException;

    // Policy Manager in Location Management

    public Optional<PolicyDescriptor> getPolicy(String locationId) throws IoTInventoryDataManagerException;

    public PolicyDescriptor updatePolicy(String locationId, PolicyDescriptor policyManagerDescriptor) throws IoTInventoryDataManagerException;

}
