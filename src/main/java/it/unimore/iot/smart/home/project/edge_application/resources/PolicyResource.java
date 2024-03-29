package it.unimore.iot.smart.home.project.edge_application.resources;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import it.unimore.iot.smart.home.project.edge_application.dto.*;
import it.unimore.iot.smart.home.project.edge_application.exception.IoTInventoryDataManagerColorValue;
import it.unimore.iot.smart.home.project.edge_application.exception.IoTInventoryDataManagerConflict;
import it.unimore.iot.smart.home.project.edge_application.exception.IoTInventoryDataManagerException;
import it.unimore.iot.smart.home.project.edge_application.exception.IoTInventoryDataManagerIntensityValue;
import it.unimore.iot.smart.home.project.edge_application.model.PolicyDescriptor;
import it.unimore.iot.smart.home.project.edge_application.services.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Optional;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
@Path("/api/iot/inventory/location/{location_id}/policy")
@Api("IoT Policy in Location Inventory Endpoint")
@Timed
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PolicyResource {

    protected final static Logger logger = LoggerFactory.getLogger(PolicyResource.class);

    ObjectMapper objectMapper;

    @SuppressWarnings("serial")
    public static class MissingKeyException extends Exception{}
    final AppConfig conf;

    public PolicyResource(AppConfig conf) {
        this.conf = conf;
        this.objectMapper = new ObjectMapper();
    }

    @GET
    @Path("/")
    @ApiOperation(value="Get the policy in a location")
    public Response getPolicy(@Context ContainerRequestContext req,
                               @PathParam("location_id") String locationId) {

        try {

            logger.info("Loading the policy in a Location with id: {}", locationId);

            //Check PathParam in the request
            if(locationId == null || !this.conf.getDataCollectorPolicyManager().getLocation(locationId).isPresent()) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(
                        new ErrorMessage(
                                Response.Status.BAD_REQUEST.getStatusCode(),
                                controlPathParamLocationId(locationId)
                        )).build();
            }

            Optional<PolicyDescriptor> policyDescriptor = this.conf.getDataCollectorPolicyManager().getPolicy(locationId);

            if(!policyDescriptor.isPresent())
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Policy Not Found !")).build();

            return Response.ok(policyDescriptor.get()).build();

        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    @PUT
    @Path("/")
    @ApiOperation(value="Update an existing policy in a Location")
    public Response updatePolicy(@Context ContainerRequestContext req,
                                 @Context UriInfo uriInfo,
                                 @PathParam("location_id") String locationId,
                                 PolicyUpdateRequest policyUpdateRequest) {

        try {

            logger.info("Incoming Policy ({}) Update Request: {}", locationId, policyUpdateRequest);

            //Check PathParam in the request
            if(locationId == null || !this.conf.getDataCollectorPolicyManager().getLocation(locationId).isPresent()) {
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(
                        new ErrorMessage(
                                Response.Status.BAD_REQUEST.getStatusCode(),
                                controlPathParamLocationId(locationId)
                        )).build();
            }

            //Check if the request is valid
            if(policyUpdateRequest == null)
                return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"Invalid request ! Check Location Id")).build();

            //Check if the device is available and correctly registered otherwise a 404 response will be sent to the client
            if(!this.conf.getDataCollectorPolicyManager().getPolicy(locationId).isPresent())
                return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(),"Policy not found !")).build();

            PolicyDescriptor policyDescriptor = (PolicyDescriptor) policyUpdateRequest;

            this.conf.getDataCollectorPolicyManager().updatePolicy(locationId, policyDescriptor);

            return Response.noContent().build();

        } catch (IoTInventoryDataManagerIntensityValue e){
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"The intensity value must be between 0.0 and 100.0!")).build();
        } catch (IoTInventoryDataManagerColorValue e){
            return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(),"The color value must be between 0 and 255!")).build();
        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON_TYPE).entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),"Internal Server Error !")).build();
        }
    }

    private String controlPathParamLocationId(String locationId) {
        // Check if the location_id is null
        if(locationId == null) {
            return "Invalid Location Id Provided !";
            // Check if the location is available or not
        } else {
            try {
                if(!this.conf.getDataCollectorPolicyManager().getLocation(locationId).isPresent()) {
                    return "Location Not Found !";
                }
            } catch (IoTInventoryDataManagerException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
