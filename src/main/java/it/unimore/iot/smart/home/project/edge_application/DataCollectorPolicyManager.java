package it.unimore.iot.smart.home.project.edge_application;

import it.unimore.iot.smart.home.project.edge_application.coap.CoapPutRequestLightColor;
import it.unimore.iot.smart.home.project.edge_application.coap.CoapPutRequestLightIntensity;
import it.unimore.iot.smart.home.project.edge_application.coap.CoapPutRequestLightSwitch;
import it.unimore.iot.smart.home.project.edge_application.exception.IoTInventoryDataManagerException;
import it.unimore.iot.smart.home.project.edge_application.model.DeviceDescriptor;
import it.unimore.iot.smart.home.project.edge_application.model.LocationDescriptor;
import it.unimore.iot.smart.home.project.edge_application.model.PolicyDescriptor;
import it.unimore.iot.smart.home.project.edge_application.persistence.DefaultIotInventoryDataManger;
import it.unimore.iot.smart.home.project.edge_application.utils.DeviceType;
import it.unimore.iot.smart.home.project.edge_application.utils.ResourceType;
import org.eclipse.californium.core.*;
import org.eclipse.californium.core.coap.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class DataCollectorPolicyManager extends DefaultIotInventoryDataManger {

    private final static Logger logger = LoggerFactory.getLogger(DataCollectorPolicyManager.class);

    private HashMap<String, String> targetObservableResourceMap;

    private HashMap<String, CoapObserveRelation> observingRelationMap;

    {
        this.targetObservableResourceMap = new HashMap<String, String>();
        this.observingRelationMap = new HashMap<String, CoapObserveRelation>();
    }

    public DataCollectorPolicyManager() {
        super();
    }

    public void init() {
        logger.info("List Locations {}",this.locationMap.values());
        logger.info("Instance of DataCollectorPolicyManager");
        toBeObservedDevicesControl();
    }

    private void toBeObservedDevicesControl() {

        this.locationMap.values().forEach(location -> {
            location.getDevices().forEach((id, device) -> {
                logger.info("Location name {}, device id {}, device {}", location.getRoom(), id, device);
                if(location.getPolicyDescriptor().isActive()) {
                    deviceObserving(location.getId(), device);
                }
            });
        });

    }

    private void deviceObserving(String locationId, DeviceDescriptor deviceDescriptor) {

        logger.info("Device Type {}", deviceDescriptor.getType());

        // NOTE:
        // The client.observe(Request, CoapHandler) method visibility has been changed from "private"
        // to "public" in order to get the ability to change the parameter of the observable GET
        //(e.g., to change token and MID).
        if(!this.targetObservableResourceMap.containsKey(deviceDescriptor.getId())) {
            deviceDescriptor.getResourcePaths().stream().map(resourcePath -> getURLDevice(deviceDescriptor, resourcePath)).forEach(targetCoapResourceURL -> {
                logger.info("URL {}", targetCoapResourceURL);
                CoapClient client = new CoapClient(targetCoapResourceURL);
                logger.info("OBSERVING ... {}", targetCoapResourceURL);
                Request request = Request.newGet().setURI(targetCoapResourceURL).setObserve();
                request.setConfirmable(true);
                CoapObserveRelation relation = client.observe(request, new CoapHandler() {

                    public void onLoad(CoapResponse response) {

                        if (deviceDescriptor.getType().equals(DeviceType.PRESENCE_SENSOR.getValue())) {
                            String content = response.getResponseText();
                            //logger.info("Notification Response Pretty Print: \n{}", Utils.prettyPrint(response));
                            logger.info("NOTIFICATION: the device {} with id {} has value {}", deviceDescriptor.getName(), deviceDescriptor.getId(), content);
                            if (content.equals("true")) {
                                try {
                                    getDevicesListByType(locationId, DeviceType.LIGHT_CONTROLLER.getValue()).forEach(device -> {
                                        device.getResourcePaths().forEach(path -> {
                                            logger.info("Send put request to device with URI: {}", getURLDevice(device, path));
                                            switch (path) {
                                                case ResourceType.LIGHT_SWITCH:
                                                    CoapPutRequestLightSwitch coapPutRequestLightSwitch = new CoapPutRequestLightSwitch(getURLDevice(device, path));
                                                    // If presence sensor is true turn on the light
                                                    coapPutRequestLightSwitch.setValue(true);
                                                    coapPutRequestLightSwitch.sendRequest();
                                                    try {
                                                        logger.info("Turn on the light {} in the room {} with id {}",device.getName() ,getLocation(locationId).get().getRoom(), getLocation(locationId).get().getId());
                                                    } catch (IoTInventoryDataManagerException e) {
                                                        e.printStackTrace();
                                                    }
                                                    break;
                                                case ResourceType.LIGHT_INTENSITY:
                                                    try {
                                                        CoapPutRequestLightIntensity coapPutRequestLightIntensity = new CoapPutRequestLightIntensity(getURLDevice(device, path), getLocation(locationId).get().getPolicyDescriptor().getLightIntensity());
                                                        coapPutRequestLightIntensity.sendRequest();
                                                    } catch (IoTInventoryDataManagerException e) {
                                                        e.printStackTrace();
                                                    }
                                                    break;
                                                case ResourceType.LIGHT_COLOR:
                                                    try {
                                                        CoapPutRequestLightColor coapPutRequestLightColor = new CoapPutRequestLightColor(getURLDevice(device, path), getLocation(locationId).get().getPolicyDescriptor().getLightColor());
                                                        coapPutRequestLightColor.sendRequest();
                                                    } catch (IoTInventoryDataManagerException e) {
                                                        e.printStackTrace();
                                                    }
                                                    break;
                                                default:
                                                    break;
                                            }
                                        });
                                    });
                                } catch (IoTInventoryDataManagerException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    getDevicesListByType(locationId, DeviceType.LIGHT_CONTROLLER.getValue()).forEach(device -> {
                                        device.getResourcePaths().forEach(path -> {
                                            logger.info("Path: {}", getURLDevice(device, path));
                                            switch (path) {
                                                case ResourceType.LIGHT_SWITCH:
                                                    CoapPutRequestLightSwitch coapPutRequestLightSwitch = new CoapPutRequestLightSwitch(getURLDevice(device, path));
                                                    // If presence sensor is false turn off the light
                                                    coapPutRequestLightSwitch.setValue(false);
                                                    coapPutRequestLightSwitch.sendRequest();
                                                    try {
                                                        logger.info("Turn off the light {} in the room {} with id {}",device.getName() ,getLocation(locationId).get().getRoom(), getLocation(locationId).get().getId());
                                                    } catch (IoTInventoryDataManagerException e) {
                                                        e.printStackTrace();
                                                    }
                                                    break;
                                                default:
                                                    break;
                                            }
                                        });
                                    });
                                } catch (IoTInventoryDataManagerException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else if (deviceDescriptor.getType().equals(DeviceType.LIGHT_CONTROLLER.getValue())) {
                            String content = response.getResponseText();
                            //logger.info("Notification Response Pretty Print: \n{}", Utils.prettyPrint(response));
                            logger.info("NOTIFICATION: the device {} with id {} has value {}", deviceDescriptor.getName(), deviceDescriptor.getId(), content);
                        }

                    }

                    public void onError() {
                        logger.error("OBSERVING FAILED");
                    }
                });

                this.observingRelationMap.put(targetCoapResourceURL, relation);
                this.targetObservableResourceMap.put(deviceDescriptor.getId(), targetCoapResourceURL);

            });
        }
    }

    private void deviceObservingAndRelationsMapDeleting(String deviceId) {
        // Stop Observing Device
        this.observingRelationMap.get(this.targetObservableResourceMap.get(deviceId)).proactiveCancel();
        // Delete the device from observingRelationMap
        this.observingRelationMap.remove(this.targetObservableResourceMap.get(deviceId));
        // Delete the device from targetObservableResourceMap
        this.targetObservableResourceMap.remove(deviceId);
    }

    private String getURLDevice(DeviceDescriptor device, String resourcePath) {
        return String.format("coap://%s:%s/%s",
                device.getHost(),
                device.getPort(),
                resourcePath);
    }

    @Override
    public LocationDescriptor deleteLocation(String locationId) throws IoTInventoryDataManagerException {
        // Remove the observation from all devices in the Location
        getLocation(locationId).get().getDevices().forEach((deviceId, device) -> {
            // Stop Observing Device and deleting relations map
            deviceObservingAndRelationsMapDeleting(deviceId);
        });

        return super.deleteLocation(locationId);
    }

    @Override
    public DeviceDescriptor createNewDevice(String locationId, DeviceDescriptor deviceDescriptor) throws IoTInventoryDataManagerException {
        // If the Location where the device was created has the Policy Manager active, look at the device
        if(getLocation(locationId).get().getPolicyDescriptor().isActive()) {
            deviceObserving(locationId, deviceDescriptor);
        }
        return super.createNewDevice(locationId, deviceDescriptor);
    }

    @Override
    public DeviceDescriptor deleteDevice(String locationId, String deviceId) throws IoTInventoryDataManagerException {
        // Stop Observing Device and deleting relations map
        deviceObservingAndRelationsMapDeleting(deviceId);

        return super.deleteDevice(locationId, deviceId);
    }

    @Override
    public PolicyDescriptor updatePolicy(String locationId, PolicyDescriptor policyDescriptor) throws IoTInventoryDataManagerException {
        // If the policy manager is activated
        if(policyDescriptor.isActive()) {
            getLocation(locationId).get().getDevices().forEach((deviceId, device) -> {
                deviceObserving(locationId, device);
            });
        } else {
            getLocation(locationId).get().getDevices().forEach((deviceId, device) -> {
                // Stop Observing Device and deleting relations map
                deviceObservingAndRelationsMapDeleting(deviceId);
            });
        }

        return super.updatePolicy(locationId, policyDescriptor);
    }

}
