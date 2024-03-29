package it.unimore.iot.smart.home.project.edge_application.resources;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.unimore.iot.smart.home.project.edge_application.dto.LocationCreationRequest;
import it.unimore.iot.smart.home.project.edge_application.dto.LocationUpdateRequest;
import it.unimore.iot.smart.home.project.edge_application.exception.IoTInventoryDataManagerConflict;
import it.unimore.iot.smart.home.project.edge_application.model.LocationDescriptor;
import it.unimore.iot.smart.home.project.edge_application.services.AppConfig;
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

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
@Path("/api/iot/inventory/location")
@Api("IoT Location Inventory Endpoint")
@Timed
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LocationResource {

    protected final static Logger logger = LoggerFactory.getLogger(LocationResource.class);

    @SuppressWarnings("serial")
    public static class MissingKeyException extends Exception{}
    final AppConfig conf;

    public LocationResource(AppConfig conf) {
        this.conf = conf;
    }

    @GET
    @Path("/")
    @ApiOperation(value="Get all available Locations or filter according to floor OR room")
    public Response getLocations(@Context ContainerRequestContext req,
                                 @QueryParam("floor") String floor,
                                 @QueryParam("room") String room) {

        try {

            logger.info("Loading all stored IoT Inventory Location filtered by Floor: {} and Room: {}", floor, room);

            List<LocationDescriptor> serviceList = null;

            //No filter applied
            if(floor == null && room == null)
                serviceList = this.conf.getDataCollectorPolicyManager().getLocationsList();
            else if(floor != null && room != null)
                serviceList = this.conf.getDataCollectorPolicyManager().getLocationsByFloorAndRoom(floor, room);
            else if(floor != null)
                serviceList = this.conf.getDataCollectorPolicyManager().getLocationsListByFloor(floor);
            else if(room != null)
                serviceList = this.conf.getDataCollectorPolicyManager().getLocationsListByRoom(room);
            else
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"City and Country are both required for filtering !")).build();

            if(serviceList != null)
                return Response.ok(serviceList).build();

            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Locations Not Found !")).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @POST
    @Path("/")
    @ApiOperation(value="Create a new Location")
    public Response createLocation(@Context ContainerRequestContext req,
                                   @Context UriInfo uriInfo,
                                   LocationCreationRequest locationCreationRequest) {

        try {

            logger.info("Incoming Location Creation Request: {}", locationCreationRequest);

            //Check the request
            if(locationCreationRequest == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid request payload")).build();

            LocationDescriptor newLocationDescriptor = (LocationDescriptor) locationCreationRequest;
            //Reset Id to null. The ID can not be provided by the client. It is generated by the server
            newLocationDescriptor.setId(null);

            newLocationDescriptor = this.conf.getDataCollectorPolicyManager().createNewLocation(newLocationDescriptor);

            return Response.created(new URI(String.format("%s/%s",uriInfo.getAbsolutePath(),newLocationDescriptor.getId()))).build();

        } catch (IoTInventoryDataManagerConflict e){
            return Response.status(Response.Status.CONFLICT).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.CONFLICT.getStatusCode(),"Location with the same name already available !")).build();
        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @GET
    @Path("/{location_id}")
    @ApiOperation(value="Get a Single Location")
    public Response getLocation(@Context ContainerRequestContext req,
                                @PathParam("location_id") String locationId) {

        try {

            logger.info("Loading Location Info for id: {}", locationId);

            //Check the request
            if(locationId == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid Location Id Provided !")).build();

            Optional<LocationDescriptor> locationDescriptor = this.conf.getDataCollectorPolicyManager().getLocation(locationId);

            if(!locationDescriptor.isPresent())
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Location Not Found !")).build();

            return Response.ok(locationDescriptor.get()).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @PUT
    @Path("/{location_id}")
    @ApiOperation(value="Update an existing Location")
    public Response updateLocation(@Context ContainerRequestContext req,
                                   @Context UriInfo uriInfo,
                                   @PathParam("location_id") String locationId,
                                   LocationUpdateRequest locationUpdateRequest) {

        try {

            logger.info("Incoming Location ({}) Update Request: {}", locationId, locationUpdateRequest);

            //Check if the request is valid
            if(locationUpdateRequest == null || !locationUpdateRequest.getId().equals(locationId))
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid request ! Check Location Id")).build();

            //Check if the device is available and correctly registered otherwise a 404 response will be sent to the client
            if(!this.conf.getDataCollectorPolicyManager().getLocation(locationId).isPresent())
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Location not found !")).build();

            this.conf.getDataCollectorPolicyManager().updateLocation(locationUpdateRequest);

            return Response.noContent().build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @DELETE
    @Path("/{location_id}")
    @ApiOperation(value="Delete a Single Location")
    public Response deleteLocation(@Context ContainerRequestContext req,
                                 @PathParam("location_id") String locationId) {

        try {

            logger.info("Deleting Location with id: {}", locationId);

            //Check the request
            if(locationId == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid Location Id Provided !")).build();

            //Check if the device is available or not
            if(!this.conf.getDataCollectorPolicyManager().getLocation(locationId).isPresent())
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Location Not Found !")).build();

            //Delete the location
            this.conf.getDataCollectorPolicyManager().deleteLocation(locationId);

            return Response.noContent().build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

}

