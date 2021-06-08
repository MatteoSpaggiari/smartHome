package it.unimore.iot.smart.home.project.edge_application;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimore.iot.smart.home.project.edge_application.coap.CoapPutRequestLightColor;
import it.unimore.iot.smart.home.project.edge_application.coap.CoapPutRequestLightIntensity;
import it.unimore.iot.smart.home.project.edge_application.coap.CoapPutRequestLightSwitch;
import it.unimore.iot.smart.home.project.edge_application.exception.IoTInventoryDataManagerColorValue;
import it.unimore.iot.smart.home.project.edge_application.exception.IoTInventoryDataManagerException;
import it.unimore.iot.smart.home.project.edge_application.exception.IoTInventoryDataManagerIntensityValue;
import it.unimore.iot.smart.home.project.edge_application.message.MessageSenMLColor;
import it.unimore.iot.smart.home.project.edge_application.message.MessageSenMLIntensity;
import it.unimore.iot.smart.home.project.edge_application.message.MessageSenMLPresence;
import it.unimore.iot.smart.home.project.edge_application.message.MessageSenMLSwitch;
import it.unimore.iot.smart.home.project.edge_application.model.*;
import it.unimore.iot.smart.home.project.edge_application.persistence.DefaultIotInventoryDataManger;
import it.unimore.iot.smart.home.project.edge_application.utils.DeviceType;
import it.unimore.iot.smart.home.project.edge_application.utils.ResourceType;
import org.eclipse.californium.core.*;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class DataCollectorPolicyManager extends DefaultIotInventoryDataManger {

    private final static Logger logger = LoggerFactory.getLogger(DataCollectorPolicyManager.class);

    private HashMap<String, String> targetObservableResourceMap;

    private HashMap<String, CoapObserveRelation> observingRelationMap;

    ObjectMapper objectMapper;

    {
        this.targetObservableResourceMap = new HashMap<String, String>();
        this.observingRelationMap = new HashMap<String, CoapObserveRelation>();
        this.objectMapper = new ObjectMapper();
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
                deviceObserving(location.getId(), device);
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
                Request request = new Request(CoAP.Code.GET);
                //Request request = Request.newGet().setURI(targetCoapResourceURL).setObserve();
                request.setConfirmable(true);
                //Set Options to receive the response as JSON+SenML MediaType
                request.setOptions(new OptionSet().setObserve(0).setAccept(MediaTypeRegistry.APPLICATION_SENML_JSON));

                CoapObserveRelation relation = client.observe(request, new CoapHandler() {

                    public void onLoad(CoapResponse response) {

                        String content = response.getResponseText();
                        //logger.info("Notification Response Pretty Print: \n{}", Utils.prettyPrint(response));
                        logger.info("NOTIFICATION: the device {} with id {} has value {}", deviceDescriptor.getName(), deviceDescriptor.getId(), content);

                        // If Device is a PresenceSensor
                        if (deviceDescriptor.getType().equals(DeviceType.PRESENCE_SENSOR.getValue())) {

                            MessageSenMLPresence message = null;

                            // Update Value
                            try {

                                message = objectMapper.readValue(content, MessageSenMLPresence.class);
                                logger.info("Message receveid presence sensor {}", message.toString());
                                logger.info("Value presence sensor {}", message.getValue());

                                // Update Value Presence Sensor
                                //((PresenceSensorDescriptor) deviceDescriptor).setPresence(message.getValue()); // Funziona
                                ((PresenceSensorDescriptor) getDevice(locationId, deviceDescriptor.getId()).get()).setPresence(message.getValue());

                            } catch (IoTInventoryDataManagerException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            try {

                                if(getLocation(locationId).get().getPolicyDescriptor().isActive()) {
                                    // If PresenceSensor detects someone
                                    if (message.getValue()) {

                                        try {

                                            if (getLocation(locationId).get().getPolicyDescriptor().isSweetNight() && LocalTime.now().getHour() > 0 && LocalTime.now().getHour() < 6) {
                                                applySweetNightPolicyManager(locationId);
                                            } else {
                                                applyStandardPolicyManager(locationId);
                                            }

                                        } catch (IoTInventoryDataManagerException e) {
                                            e.printStackTrace();
                                        }

                                        // If PresenceSensor stops detecting someone
                                    } else {
                                        turnOffTheLights(locationId);
                                    }
                                }
                            } catch (IoTInventoryDataManagerException e) {
                                e.printStackTrace();
                            }

                        } else if (deviceDescriptor.getType().equals(DeviceType.LIGHT_CONTROLLER.getValue())) {

                            if(targetCoapResourceURL.contains(ResourceType.LIGHT_SWITCH)) {

                                try {

                                    MessageSenMLSwitch message = objectMapper.readValue(content, MessageSenMLSwitch.class);

                                    logger.info("Message receveid LIGHT_SWITCH {}", message);
                                    logger.info("Value LIGHT_SWITCH {}", message.getValue());

                                    // Update Value Light Controller
                                    //((LightControllerDescriptor) deviceDescriptor).setActive(message.getValue()); Non funziona???
                                    ((LightControllerDescriptor) getDevice(locationId, deviceDescriptor.getId()).get()).setActive(message.getValue());

                                } catch (IoTInventoryDataManagerException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } else if(targetCoapResourceURL.contains(ResourceType.LIGHT_INTENSITY)) {

                                try {

                                    MessageSenMLIntensity message = objectMapper.readValue(content, MessageSenMLIntensity.class);

                                    logger.info("Message receveid LIGHT_INTENSITY {}", message);
                                    logger.info("Value LIGHT_INTENSITY {}", message.getValue());

                                    // Update Value Light Controller
                                    //((LightControllerDescriptor) deviceDescriptor).setIntensity(message.getValue());
                                    ((LightControllerDescriptor) getDevice(locationId, deviceDescriptor.getId()).get()).setIntensity(message.getValue());

                                } catch (IoTInventoryDataManagerException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } else if(targetCoapResourceURL.contains(ResourceType.LIGHT_COLOR)) {

                                try {

                                    MessageSenMLColor message = objectMapper.readValue(content, MessageSenMLColor.class);

                                    logger.info("Message receveid LIGHT_COLOR {}", message);
                                    logger.info("Value LIGHT_COLOR {}", message.getValue());

                                    // Update Value Light Controller
                                    //((LightControllerDescriptor) deviceDescriptor).setColor(message.getValue());
                                    ((LightControllerDescriptor) getDevice(locationId, deviceDescriptor.getId()).get()).setColor(message.getValue());

                                } catch (IoTInventoryDataManagerException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                            //logger.info("Notification Response Pretty Print: \n{}", Utils.prettyPrint(response));
                            logger.info("NOTIFICATION: the device {} with id {} has value {} path {}", deviceDescriptor.getName(), deviceDescriptor.getId(), content, targetCoapResourceURL);
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

    private void applyStandardPolicyManager(String locationId) {

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
                                logger.info("Turn on the light {} in the room {} with id {}", device.getName(), getLocation(locationId).get().getRoom(), getLocation(locationId).get().getId());
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

    }

    private void applySweetNightPolicyManager(String locationId) {

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
                                logger.info("Turn on the light {} in the room {} with id {}", device.getName(), getLocation(locationId).get().getRoom(), getLocation(locationId).get().getId());
                            } catch (IoTInventoryDataManagerException e) {
                                e.printStackTrace();
                            }
                            break;
                        case ResourceType.LIGHT_INTENSITY:
                            try {
                                CoapPutRequestLightIntensity coapPutRequestLightIntensity = new CoapPutRequestLightIntensity(getURLDevice(device, path), getLocation(locationId).get().getPolicyDescriptor().getLightIntensitySweetNight());
                                coapPutRequestLightIntensity.sendRequest();
                            } catch (IoTInventoryDataManagerException e) {
                                e.printStackTrace();
                            }
                            break;
                        case ResourceType.LIGHT_COLOR:
                            try {
                                CoapPutRequestLightColor coapPutRequestLightColor = new CoapPutRequestLightColor(getURLDevice(device, path), getLocation(locationId).get().getPolicyDescriptor().getLightColorSweetNight());
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

    }

    private void turnOffTheLights(String locationId) {

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
    public DeviceDescriptor deleteDevice(String locationId, String deviceId) throws IoTInventoryDataManagerException {
        // Stop Observing Device and deleting relations map
        deviceObservingAndRelationsMapDeleting(deviceId);

        return super.deleteDevice(locationId, deviceId);
    }

    @Override
    public DeviceDescriptor createNewDevice(String locationId, DeviceDescriptor deviceDescriptor) throws IoTInventoryDataManagerException, IoTInventoryDataManagerIntensityValue, IoTInventoryDataManagerColorValue {
        // Start observing the device
        deviceObserving(locationId, deviceDescriptor);

        return super.createNewDevice(locationId, deviceDescriptor);
    }

}
