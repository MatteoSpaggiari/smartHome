package it.unimore.iot.smart.home.project.edge_application.resources;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.unimore.iot.smart.home.project.edge_application.dto.LightControllerCreationRequest;
import it.unimore.iot.smart.home.project.edge_application.dto.LightControllerUpdateRequest;
import it.unimore.iot.smart.home.project.edge_application.dto.PresenceSensorCreationRequest;
import it.unimore.iot.smart.home.project.edge_application.dto.PresenceSensorUpdateRequest;
import it.unimore.iot.smart.home.project.edge_application.model.DeviceDescriptor;
import it.unimore.iot.smart.home.project.edge_application.model.LightControllerDescriptor;
import it.unimore.iot.smart.home.project.edge_application.model.PresenceSensorDescriptor;
import it.unimore.iot.smart.home.project.edge_application.services.AppConfig;
import it.unimore.iot.smart.home.project.edge_application.utils.DeviceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Optional;


@Path("/api/iot/inventory/location")
@Api("IoT Location Inventory Endpoint")
public class DeviceResource {

    final protected Logger logger = LoggerFactory.getLogger(DeviceResource.class);

    ObjectMapper objectMapper;

    @SuppressWarnings("serial")
    public static class MissingKeyException extends Exception{}
    final AppConfig conf;

    public DeviceResource(AppConfig conf) {
        this.conf = conf;
        this.objectMapper = new ObjectMapper();
    }

    @GET
    @Path("/{location_id}/device")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Get all available IoT devices or filter according to their in a location")
    public Response getDevices(@Context ContainerRequestContext req,
                               @PathParam("location_id") String locationId,
                               @QueryParam("type") String deviceType) {

        try {

            logger.info("Loading all stored IoT Inventory Devices filtered by Type: {}", deviceType);

            List<DeviceDescriptor> deviceList = null;

            //No filter applied
            if(deviceType == null)
                deviceList = this.conf.getInventoryDataManager().getDevicesList(locationId);
            else
                deviceList = this.conf.getInventoryDataManager().getDevicesListByType(locationId, deviceType);

            if(deviceList == null)
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Devices Not Found !")).build();

            return Response.ok(deviceList).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @GET
    @Path("/{location_id}/device/{device_id}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Get a Single Location")
    public Response getDevice(@Context ContainerRequestContext req,
                              @PathParam("location_id") String locationId,
                              @PathParam("device_id") String deviceId) {

        try {

            logger.info("Loading Device Info for id: {}", deviceId);

            //Check the request
            if(deviceId == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid Device Id Provided !")).build();

            Optional<DeviceDescriptor> deviceDescriptor = this.conf.getInventoryDataManager().getDevice(locationId, deviceId);

            if(!deviceDescriptor.isPresent())
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Device Not Found !")).build();

            return Response.ok(deviceDescriptor.get()).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @POST
    @Path("/{location_id}/device/{device_type}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Create a new Device")
    public Response createDevice(@Context ContainerRequestContext req,
                                 @Context UriInfo uriInfo,
                                 @PathParam("location_id") String locationId,
                                 @PathParam("device_type") String deviceType,
                                 JsonNode deviceCreationRequest) {

        try {

            logger.info("Incoming Device Creation Request: {}", deviceCreationRequest);

            DeviceDescriptor newDeviceDescriptor = new DeviceDescriptor();

            // If the device type is a Presence Sensor instantiate a Presence Sensor
            if(deviceType.equals(DeviceType.PRESENCE_SENSOR.getValue())) {
                PresenceSensorCreationRequest presenceSensorCreationRequest = objectMapper.treeToValue(deviceCreationRequest, PresenceSensorCreationRequest.class);
                PresenceSensorDescriptor presenceSensorDescriptor = (PresenceSensorDescriptor) presenceSensorCreationRequest;
                newDeviceDescriptor = this.conf.getInventoryDataManager().createNewDevice(locationId, presenceSensorDescriptor);
            // If the device type is a Light Controller instantiate a Light Controller
            } else if(deviceType.equals(DeviceType.LIGHT_CONTROLLER.getValue())) {
                LightControllerCreationRequest lightControllerCreationRequest = objectMapper.treeToValue(deviceCreationRequest, LightControllerCreationRequest.class);
                LightControllerDescriptor lightControllerDescriptor = (LightControllerDescriptor) lightControllerCreationRequest;
                newDeviceDescriptor = this.conf.getInventoryDataManager().createNewDevice(locationId, lightControllerDescriptor);
            } else {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Device type not recognized")).build();
            }

            return Response.created(new URI(String.format("%s/%s", uriInfo.getAbsolutePath(), newDeviceDescriptor.getId()))).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @PUT
    @Path("/{location_id}/device/{device_type}/{device_id}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Update an existing device")
    public Response updateDevice(@Context ContainerRequestContext req,
                                 @Context UriInfo uriInfo,
                                 @PathParam("location_id") String locationId,
                                 @PathParam("device_type") String deviceType,
                                 @PathParam("device_id") String deviceId,
                                 JsonNode deviceUpdateRequest) {

        try {

            logger.info("Incoming Device ({}) Update Request: {}", deviceId, deviceUpdateRequest);

            //Check if the request is valid
            if(deviceUpdateRequest == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid request ! Check Device Id")).build();

            //Check if the device is available and correctly registered otherwise a 404 response will be sent to the client
            if(!this.conf.getInventoryDataManager().getDevice(locationId, deviceId).isPresent())
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Device not found !")).build();

            DeviceDescriptor deviceDescriptor = new DeviceDescriptor();

            // If the device type is a Presence Sensor instantiate a Presence Sensor
            if(deviceType.equals(DeviceType.PRESENCE_SENSOR.getValue())) {
                PresenceSensorUpdateRequest presenceSensorUpdateRequest = objectMapper.treeToValue(deviceUpdateRequest, PresenceSensorUpdateRequest.class);
                PresenceSensorDescriptor presenceSensorDescriptor = (PresenceSensorDescriptor) presenceSensorUpdateRequest;
                deviceDescriptor = this.conf.getInventoryDataManager().updateDevice(locationId, presenceSensorDescriptor);
                // If the device type is a Light Controller instantiate a Light Controller
            } else if(deviceType.equals(DeviceType.LIGHT_CONTROLLER.getValue())) {
                LightControllerUpdateRequest lightControllerUpdateRequest = objectMapper.treeToValue(deviceUpdateRequest, LightControllerUpdateRequest.class);
                LightControllerDescriptor lightControllerDescriptor = (LightControllerDescriptor) lightControllerUpdateRequest;
                deviceDescriptor = this.conf.getInventoryDataManager().updateDevice(locationId, lightControllerDescriptor);
            } else {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Device type not recognized")).build();
            }

            return Response.noContent().build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @DELETE
    @Path("/{location_id}/device/{device_id}")
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="Delete a Single Device")
    public Response deleteDevice(@Context ContainerRequestContext req,
                                 @PathParam("location_id") String locationId,
                                 @PathParam("device_id") String deviceId) {

        try {

            logger.info("Deleting Device with id: {}", deviceId);

            //Check the request
            if(deviceId == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid Device Id Provided !")).build();

            //Check if the device is available or not
            Optional<DeviceDescriptor> deviceDescriptor = this.conf.getInventoryDataManager().getDevice(locationId, deviceId);
            if(!deviceDescriptor.isPresent())
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Device Not Found !")).build();

            //Delete the device
            this.conf.getInventoryDataManager().deleteDevice(locationId, deviceId);

            return Response.noContent().build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

}
