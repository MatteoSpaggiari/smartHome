package it.unimore.iot.smart.home.project.server_http.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimore.iot.smart.home.project.server_http.model.BuildingDescriptor;
import it.unimore.iot.smart.home.project.server_http.model.LocationDescriptor;
import it.unimore.iot.smart.home.project.server_http.persistence.IIotInventoryDataManager;

import java.io.File;
import java.io.IOException;

/**
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project http-iot-api-demo
 * @created 05/10/2020 - 12:04
 */
public class DummyDataGenerator {

    public static void loadDataBuildingFromFileJson(IIotInventoryDataManager inventoryDataManager){

        try{

            ObjectMapper objectMapper = new ObjectMapper();
            BuildingDescriptor buildingInfo = objectMapper.readValue(new File("src/main/java/it/unimore/iot/smart/home/project/server_http/data/home.json"), BuildingDescriptor.class);
            for(LocationDescriptor location : buildingInfo.getLocations()) {
                inventoryDataManager.createNewLocation(location);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            BuildingDescriptor buildingInfo = objectMapper.readValue(new File("src/main/java/it/unimore/iot/smart/home/project/server_http/data/home.json"), BuildingDescriptor.class);
            System.out.println(buildingInfo);
            for(LocationDescriptor location : buildingInfo.getLocations()) {
                System.out.println(location);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
