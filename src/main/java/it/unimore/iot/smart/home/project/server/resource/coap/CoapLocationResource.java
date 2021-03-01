package it.unimore.iot.smart.home.project.server.resource.coap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimore.iot.smart.home.project.server.resource.model.LocationDescriptor;
import it.unimore.iot.smart.home.project.server.resource.raw.LocationRawDescriptor;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import it.unimore.iot.smart.home.project.utils.CoreInterfaces;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class CoapLocationResource extends CoapResource {

    private final static Logger logger = LoggerFactory.getLogger(CoapLocationResource.class);

    private static final String OBJECT_TITLE = "Location";

    private static final Number VERSION = 0.1;

    private LocationRawDescriptor locationRawDescriptor;

    private LocationDescriptor locationDescriptorValue;

    private ObjectMapper objectMapper;

    private String deviceId;

    public CoapLocationResource(String deviceId, String name, LocationRawDescriptor locationRawDescriptor) {

        super(name);

        if(locationRawDescriptor != null && deviceId != null){

            this.deviceId = deviceId;

            this.locationRawDescriptor = locationRawDescriptor;
            this.locationDescriptorValue = locationRawDescriptor.getLocationDescriptor();

            //Jackson Object Mapper + Ignore Null Fields in order to properly generate the SenML Payload
            this.objectMapper = new ObjectMapper();
            this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            setObservable(true); // enable observing
            setObserveType(CoAP.Type.CON); // configure the notification type to CONs

            getAttributes().setTitle(OBJECT_TITLE);
            getAttributes().setObservable();
            getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_LINK_FORMAT));
        }
        else
            logger.error("Error -> NULL Raw Reference !");

    }

    private Optional<String> getDataLinkFormatResponse() {

        try {

            String path = this.getURI();
            return Optional.of(String.format("<%s>;ct=\"%s\"",path,Integer.toString(MediaTypeRegistry.APPLICATION_LINK_FORMAT)));

        } catch (Exception e){
            return Optional.empty();
        }

    }

    @Override
    public void handleGET(CoapExchange exchange) {

        //If the request specify the MediaType as JSON or JSON+SenML
        if(exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_LINK_FORMAT){

            Optional<String> payload = getDataLinkFormatResponse();

            if(payload.isPresent())
                exchange.respond(CoAP.ResponseCode.CONTENT, payload.get(), exchange.getRequestOptions().getAccept());
            else
                exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
        //Otherwise respond with the default text/plain payload
        else {
            Optional<String> payload = getDataLinkFormatResponse();
            exchange.respond(CoAP.ResponseCode.CONTENT, payload.get(), MediaTypeRegistry.TEXT_PLAIN);
        }

    }

}
