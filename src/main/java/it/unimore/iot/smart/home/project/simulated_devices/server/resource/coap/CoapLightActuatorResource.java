package it.unimore.iot.smart.home.project.simulated_devices.server.resource.coap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimore.iot.smart.home.project.simulated_devices.server.resource.raw.LightRawActuator;
import it.unimore.iot.smart.home.project.simulated_devices.server.resource.raw.ResourceDataListener;
import it.unimore.iot.smart.home.project.simulated_devices.server.resource.raw.SmartObjectResource;
import it.unimore.iot.smart.home.project.simulated_devices.utils.CoreInterfaces;
import it.unimore.iot.smart.home.project.simulated_devices.utils.SenMLPack;
import it.unimore.iot.smart.home.project.simulated_devices.utils.SenMLRecord;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class CoapLightActuatorResource extends CoapResource {

    private final static Logger logger = LoggerFactory.getLogger(CoapLightActuatorResource.class);

    private static final String OBJECT_TITLE = "LightSwitchActuator";

    private static final Number SENSOR_VERSION = 0.1;

    private ObjectMapper objectMapper;

    private LightRawActuator lightRawActuator;

    private Boolean isOn;

    private String deviceId;

    {
        this.isOn = true;
    }

    public CoapLightActuatorResource(String deviceId, String name, LightRawActuator lightRawActuator) {
        super(name);

        if(lightRawActuator != null && deviceId != null){

            this.deviceId = deviceId;

            this.lightRawActuator = lightRawActuator;
            this.isOn = lightRawActuator.loadUpdatedValue();

            //Jackson Object Mapper + Ignore Null Fields in order to properly generate the SenML Payload
            this.objectMapper = new ObjectMapper();
            this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            setObservable(true); // enable observing
            setObserveType(CoAP.Type.CON); // configure the notification type to CONs

            getAttributes().setTitle(OBJECT_TITLE);
            getAttributes().setObservable();
            getAttributes().addAttribute("rt", lightRawActuator.getType());
            getAttributes().addAttribute("if", CoreInterfaces.CORE_A.getValue());
            getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
            getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));

            lightRawActuator.addDataListener(new ResourceDataListener<Boolean>() {
                @Override
                public void onDataChanged(SmartObjectResource<Boolean> resource, Boolean updatedValue) {
                    logger.info("Raw Resource Notification Callback ! New Value: {}", updatedValue);
                    isOn = updatedValue;
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
            senMLRecord.setBver(SENSOR_VERSION);
            senMLRecord.setVb(this.isOn);
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
        //Otherwise respond with the default text/plain payload
        else
            exchange.respond(CoAP.ResponseCode.CONTENT, String.valueOf(this.isOn), MediaTypeRegistry.TEXT_PLAIN);


    }

    @Override
    public void handlePOST(CoapExchange exchange) {

        try{
            //Empty request
            if(exchange.getRequestPayload() == null){

                //Update internal status
                this.isOn = !this.isOn;
                this.lightRawActuator.setActive(this.isOn);

                logger.info("Resource Status Updated: {}", this.isOn);

                exchange.respond(CoAP.ResponseCode.CHANGED);
            }
            else
                exchange.respond(CoAP.ResponseCode.BAD_REQUEST);

        }catch (Exception e){
            logger.error("Error Handling POST -> {}", e.getLocalizedMessage());
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public void handlePUT(CoapExchange exchange) {

        try{

            //If the request body is available
            if(exchange.getRequestPayload() != null){

                boolean submittedValue = Boolean.parseBoolean(new String(exchange.getRequestPayload()));

                logger.info("Submitted value: {}", submittedValue);

                //Update internal status
                this.isOn = submittedValue;
                this.lightRawActuator.setActive(this.isOn);

                logger.info("Resource Status Updated: {}", this.lightRawActuator.getActive());

                exchange.respond(CoAP.ResponseCode.CHANGED);
            }
            else
                exchange.respond(CoAP.ResponseCode.BAD_REQUEST);

        }catch (Exception e){
            logger.error("Error Handling PUT -> {}", e.getLocalizedMessage());
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }

    }
    
}
