package it.unimore.iot.smart.home.project.edge_application.persistence;

import it.unimore.iot.smart.home.project.edge_application.dto.LocationUpdateRequest;
import it.unimore.iot.smart.home.project.edge_application.exception.IoTInventoryDataManagerColorValue;
import it.unimore.iot.smart.home.project.edge_application.exception.IoTInventoryDataManagerConflict;
import it.unimore.iot.smart.home.project.edge_application.exception.IoTInventoryDataManagerException;
import it.unimore.iot.smart.home.project.edge_application.exception.IoTInventoryDataManagerIntensityValue;
import it.unimore.iot.smart.home.project.edge_application.model.*;
import it.unimore.iot.smart.home.project.edge_application.resources.DeviceResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class DefaultIotInventoryDataManger implements IIotInventoryDataManager {

    protected final static Logger logger = LoggerFactory.getLogger(DefaultIotInventoryDataManger.class);

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
            throw new IoTInventoryDataManagerConflict("Location on the same floor and with the same name already available!");

        //Set the locationId to a random UUID value
        if(locationDescriptor.getId() == null)
            locationDescriptor.setId(UUID.randomUUID().toString());

        this.locationMap.put(locationDescriptor.getId(), locationDescriptor);
        return locationDescriptor;

    }

    @Override
    public LocationDescriptor updateLocation(LocationUpdateRequest locationUpdateRequest) throws IoTInventoryDataManagerException, IoTInventoryDataManagerConflict {

        if(locationUpdateRequest.getRoom() != null && locationUpdateRequest.getFloor() != null && this.getLocationsByFloorAndRoom(locationUpdateRequest.getFloor(), locationUpdateRequest.getRoom()).size() > 0)
            throw new IoTInventoryDataManagerConflict("Location on the same floor and with the same name already available!");

        LocationDescriptor locationDescriptor = getLocation(locationUpdateRequest.getId()).get();
        locationDescriptor.setRoom(locationUpdateRequest.getRoom());
        locationDescriptor.setFloor(locationUpdateRequest.getFloor());
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
    public DeviceDescriptor createNewDevice(String locationId, DeviceDescriptor deviceDescriptor) throws IoTInventoryDataManagerException, IoTInventoryDataManagerIntensityValue, IoTInventoryDataManagerColorValue {

        if(deviceDescriptor instanceof LightControllerDescriptor) {
            if(!controlIntensityValue(((LightControllerDescriptor) deviceDescriptor).getIntensity()))
                throw new IoTInventoryDataManagerIntensityValue("The intensity value must be between 0.0 and 100.0!");

            if(!controlColorValue(((LightControllerDescriptor) deviceDescriptor).getColor()))
                throw new IoTInventoryDataManagerColorValue("The color value must be between 0 and 255!");
        }

        this.locationMap.get(locationId).getDevices().put(deviceDescriptor.getId(), deviceDescriptor);

        return deviceDescriptor;
    }

    @Override
    public DeviceDescriptor updateDevice(String locationId, DeviceDescriptor deviceDescriptor) throws IoTInventoryDataManagerException, IoTInventoryDataManagerIntensityValue, IoTInventoryDataManagerColorValue {

        if(deviceDescriptor instanceof LightControllerDescriptor) {
            if(!controlIntensityValue(((LightControllerDescriptor) deviceDescriptor).getIntensity()))
                throw new IoTInventoryDataManagerIntensityValue("The intensity value must be between 0.0 and 100.0!");

            if(!controlColorValue(((LightControllerDescriptor) deviceDescriptor).getColor()))
                throw new IoTInventoryDataManagerColorValue("The color value must be between 0 and 255!");
        }

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
    public PolicyDescriptor updatePolicy(String locationId, PolicyDescriptor policyDescriptor) throws IoTInventoryDataManagerException, IoTInventoryDataManagerIntensityValue, IoTInventoryDataManagerColorValue {

        if(!controlIntensityValue(policyDescriptor.getLightIntensity()) || !controlIntensityValue(policyDescriptor.getLightIntensitySweetNight())) {
            logger.info("The intensity value must be between 0.0 and 100.0!");
            throw new IoTInventoryDataManagerIntensityValue("The intensity value must be between 0.0 and 100.0!");
        }

        if(!controlColorValue(policyDescriptor.getLightColor()) || !controlColorValue(policyDescriptor.getLightColorSweetNight())) {
            logger.info("The color value must be between 0 and 255!");
            throw new IoTInventoryDataManagerColorValue("The color value must be between 0 and 255!");
        }

        this.locationMap.get(locationId).setPolicyDescriptor(policyDescriptor);
        return policyDescriptor;
    }

    private boolean controlIntensityValue(Double value) {
        if(value >= 0.0 && value <= 100.0) {
            return true;
        }
        return false;
    }

    private boolean controlColorValue(HashMap<String, Integer> value) {
        if((value.get("red") >= 0 && value.get("red") <= 255) &&
            (value.get("green") >= 0 && value.get("green") <= 255) &&
            (value.get("blue") >= 0 && value.get("blue") <= 255)) {
            return true;
        }
        return false;
    }
}
