package it.unimore.iot.smart.home.project.simulated_devices.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimore.iot.smart.home.project.simulated_devices.server.LightControllerServer;
import it.unimore.iot.smart.home.project.simulated_devices.server.PresenceSensorServer;
import org.eclipse.californium.core.CoapServer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class LoadInitialData {

    public static List<CoapServer> loadDataBuildingFromFileJson(){

        List<CoapServer> listCoapServers = new ArrayList<CoapServer>();

        try{

            ObjectMapper objectMapper = new ObjectMapper();
            // I read JSON file and turn it into Json Node
            JsonNode dataFromJson = objectMapper.readTree(new File("src/main/java/it/unimore/iot/smart/home/project/data/home.json"));
            JsonNode locations = dataFromJson.get("locations");
            for(JsonNode location : locations) {
                // Retrieve the devices
                JsonNode devices = location.get("devices");

                for(JsonNode device : devices) {
                    // If the device type is a Presence Sensor instantiate a Presence Sensor
                    if(device.get("type").asText().equals(DeviceType.PRESENCE_SENSOR.getValue())) {
                        PresenceSensorServer presenceSensorServer = new PresenceSensorServer(
                                device.get("port").asInt(),
                                device.get("id").asText(),
                                device.get("presence").asBoolean()
                        );
                        listCoapServers.add(presenceSensorServer);
                        // If the device type is a Light Controller instantiate a Light Controller
                    } else if(device.get("type").asText().equals(DeviceType.LIGHT_CONTROLLER.getValue())) {
                        JsonNode color = device.get("color");
                        LightControllerServer lightControllerServer = new LightControllerServer(
                                device.get("port").asInt(),
                                device.get("id").asText(),
                                device.get("active").asBoolean(),
                                device.get("intensity").asDouble(),
                                color.get("red").asInt(),
                                color.get("green").asInt(),
                                color.get("blue").asInt()
                        );
                        listCoapServers.add(lightControllerServer);
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return listCoapServers;
    }

    public static void main(String[] args) {

        /*
        List<CoapServer> listCoapServers = new ArrayList<CoapServer>();

        try{

            ObjectMapper objectMapper = new ObjectMapper();
            // I read JSON file and turn it into Json Node
            JsonNode dataFromJson = objectMapper.readTree(new File("src/main/java/it/unimore/iot/smart/home/project/edge_application/data/home.json"));
            JsonNode locations = dataFromJson.get("locations");
            for(JsonNode location : locations) {
                // Retrieve the devices
                JsonNode devices = location.get("devices");

                for(JsonNode device : devices) {
                    // If the device type is a Presence Sensor instantiate a Presence Sensor
                    if(device.get("type").asText().equals(DeviceType.PRESENCE_SENSOR.getValue())) {
                        PresenceSensorServer presenceSensorServer = new PresenceSensorServer(
                                device.get("port").asInt(),
                                device.get("id").asText(),
                                device.get("presence").asBoolean()
                        );
                        listCoapServers.add(presenceSensorServer);
                        // If the device type is a Light Controller instantiate a Light Controller
                    } else if(device.get("type").asText().equals(DeviceType.LIGHT_CONTROLLER.getValue())) {
                        JsonNode color = device.get("color");
                        LightControllerServer lightControllerServer = new LightControllerServer(
                                device.get("port").asInt(),
                                device.get("id").asText(),
                                device.get("active").asBoolean(),
                                device.get("intensity").asDouble(),
                                color.get("red").asInt(),
                                color.get("green").asInt(),
                                color.get("blue").asInt()
                        );
                        listCoapServers.add(lightControllerServer);
                    }
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println(listCoapServers);
        */
    }

}
