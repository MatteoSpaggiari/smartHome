package it.unimore.iot.smart.home.project.edge_application.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimore.iot.smart.home.project.edge_application.model.*;
import it.unimore.iot.smart.home.project.edge_application.persistence.IIotInventoryDataManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class LoadInitialData {

    public static void loadDataBuildingFromFileJson(IIotInventoryDataManager inventoryDataManager){

        try{

            ObjectMapper objectMapper = new ObjectMapper();
            // I read JSON file and turn it into Json Node
            JsonNode dataFromJson = objectMapper.readTree(new File("src/main/java/it/unimore/iot/smart/home/project/data/home.json"));
            JsonNode locations = dataFromJson.get("locations");
            for(JsonNode location : locations) {
                // Retrieve the policy
                // Create the Location without Device
                LocationDescriptor locationDescriptor = new LocationDescriptor(location.get("id").asText(), location.get("room").asText(), location.get("floor").asText(), objectMapper.treeToValue(location.get("policy"), PolicyDescriptor.class));
                // Retrieve the devices
                JsonNode devices = location.get("devices");

                for(JsonNode device : devices) {
                    // If the device type is a Presence Sensor instantiate a Presence Sensor
                    if(device.get("type").asText().equals(DeviceType.PRESENCE_SENSOR.getValue())) {
                        locationDescriptor.getDevices().put(device.get("id").asText(), objectMapper.treeToValue(device, PresenceSensorDescriptor.class));
                    // If the device type is a Light Controller instantiate a Light Controller
                    } else if(device.get("type").asText().equals(DeviceType.LIGHT_CONTROLLER.getValue())) {
                        locationDescriptor.getDevices().put(device.get("id").asText(), objectMapper.treeToValue(device, LightControllerDescriptor.class));
                    }
                }
                inventoryDataManager.createNewLocation(locationDescriptor);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<LocationDescriptor> locationList = new ArrayList<LocationDescriptor>();
            JsonNode jsonNode = objectMapper.readTree(new File("src/main/java/it/unimore/iot/smart/home/project/edge_application/data/home.json"));
            //System.out.println(jsonNode);
            JsonNode locations = jsonNode.get("locations");
            //System.out.println(locations);
            for(JsonNode location : locations) {
                // Retrieve the policy
                PolicyDescriptor policyDescriptor = objectMapper.treeToValue(location.get("policy"), PolicyDescriptor.class);
                // Create the Location without Device
                LocationDescriptor locationDescriptor = new LocationDescriptor(location.get("id").asText(), location.get("room").asText(), location.get("floor").asText(), policyDescriptor);
                JsonNode devices = location.get("devices");
                for(JsonNode device : devices) {
                    if(device.get("type").asText().equals(DeviceType.PRESENCE_SENSOR.getValue())) {
                        locationDescriptor.getDevices().put(device.get("id").asText(), objectMapper.treeToValue(device, PresenceSensorDescriptor.class));
                    } else if(device.get("type").asText().equals(DeviceType.LIGHT_CONTROLLER.getValue())) {
                        locationDescriptor.getDevices().put(device.get("id").asText(), objectMapper.treeToValue(device, LightControllerDescriptor.class));
                    }
                }
                locationList.add(locationDescriptor);
                //System.out.println(locationDescriptor);
            }
            System.out.println("Locations");
            System.out.println(locationList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
