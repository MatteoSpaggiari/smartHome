package it.unimore.iot.smart.home.project.simulated_environment.server.resource.coap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimore.iot.smart.home.project.simulated_environment.server.resource.raw.PresenceRawSensor;
import it.unimore.iot.smart.home.project.simulated_environment.server.resource.raw.ResourceDataListener;
import it.unimore.iot.smart.home.project.simulated_environment.server.resource.raw.SmartObjectResource;
import it.unimore.iot.smart.home.project.utils.CoreInterfaces;
import it.unimore.iot.smart.home.project.utils.SenMLPack;
import it.unimore.iot.smart.home.project.utils.SenMLRecord;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class CoapPresenceSensorResource extends CoapResource {

    private final static Logger logger = LoggerFactory.getLogger(CoapPresenceSensorResource.class);

    private static final String OBJECT_TITLE = "PresenceSensor";

    private static final Number VERSION = 0.1;

    private ObjectMapper objectMapper;

    private PresenceRawSensor presenceRawSensor;

    private Boolean isPresence;

    private String deviceId;

    {
        this.isPresence = false;
    }


    public CoapPresenceSensorResource(String deviceId, String name, PresenceRawSensor presenceRawSensor) {
        super(name);

        if(presenceRawSensor != null && deviceId != null){

            this.deviceId = deviceId;

            this.presenceRawSensor = presenceRawSensor;

            //Jackson Object Mapper + Ignore Null Fields in order to properly generate the SenML Payload
            this.objectMapper = new ObjectMapper();
            this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            setObservable(true); // enable observing
            setObserveType(CoAP.Type.CON); // configure the notification type to CONs

            getAttributes().setTitle(OBJECT_TITLE);
            getAttributes().setObservable();
            getAttributes().addAttribute("rt", presenceRawSensor.getType());
            getAttributes().addAttribute("if", CoreInterfaces.CORE_S.getValue());
            getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
            getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));

            presenceRawSensor.addDataListener(new ResourceDataListener<Boolean>() {
                @Override
                public void onDataChanged(SmartObjectResource<Boolean> resource, Boolean updatedValue) {
                    logger.info("Raw Resource Notification Callback ! New Value: {}", updatedValue);
                    isPresence = updatedValue;
                    changed();
                }
            });
        }
        else
            logger.error("Error -> NULL Raw Reference !");

    }

    /**
     * Create the SenML Response with the updated value and the resource information
     * @return
     */
    private Optional<String> getJsonSenmlResponse(){

        try{

            SenMLPack senMLPack = new SenMLPack();

            SenMLRecord senMLRecord = new SenMLRecord();
            senMLRecord.setBn(String.format("%s:%s", this.deviceId, this.getName()));
            senMLRecord.setBver(VERSION);
            senMLRecord.setVb(isPresence);
            senMLRecord.setT(System.currentTimeMillis());

            senMLPack.add(senMLRecord);

            return Optional.of(this.objectMapper.writeValueAsString(senMLPack));

        }catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public void handleGET(CoapExchange exchange) {

        //If the request specify the MediaType as JSON or JSON+SenML
        if(exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_SENML_JSON ||
                exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_JSON){

            Optional<String> senmlPayload = getJsonSenmlResponse();

            if(senmlPayload.isPresent())
                exchange.respond(CoAP.ResponseCode.CONTENT, senmlPayload.get(), exchange.getRequestOptions().getAccept());
            else
                exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
        //Otherwise respond with the default textplain payload
        else
            exchange.respond(CoAP.ResponseCode.CONTENT, String.valueOf(isPresence), MediaTypeRegistry.TEXT_PLAIN);

    }
}
