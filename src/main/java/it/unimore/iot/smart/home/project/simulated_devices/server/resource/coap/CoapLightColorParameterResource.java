package it.unimore.iot.smart.home.project.simulated_devices.server.resource.coap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimore.iot.smart.home.project.simulated_devices.message.MessageSenMLColor;
import it.unimore.iot.smart.home.project.simulated_devices.server.resource.raw.LightRawColor;
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
import java.util.HashMap;
import java.util.Optional;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class CoapLightColorParameterResource extends CoapResource {

    private final static Logger logger = LoggerFactory.getLogger(CoapLightColorParameterResource.class);

    private static final String OBJECT_TITLE = "LightColorParameter";

    private static final Number VERSION = 0.1;

    private LightRawColor lightRawColor;

    private HashMap<String, Integer> updatedColorValue;

    private ObjectMapper objectMapper;

    private String deviceId;

    {
        this.updatedColorValue = new HashMap<String, Integer>();
        this.updatedColorValue.put("Red", 255);
        this.updatedColorValue.put("Green", 255);
        this.updatedColorValue.put("Blue", 255);
    }

    public CoapLightColorParameterResource(String deviceId, String name, LightRawColor lightRawColor) {

        super(name);

        if(lightRawColor != null && deviceId != null){

            this.deviceId = deviceId;

            this.lightRawColor = lightRawColor;
            this.updatedColorValue = lightRawColor.loadUpdatedValue();

            //Jackson Object Mapper + Ignore Null Fields in order to properly generate the SenML Payload
            this.objectMapper = new ObjectMapper();
            this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            setObservable(true); // enable observing
            setObserveType(CoAP.Type.CON); // configure the notification type to CONs

            getAttributes().setTitle(OBJECT_TITLE);
            getAttributes().setObservable();
            getAttributes().addAttribute("rt", lightRawColor.getType());
            getAttributes().addAttribute("if", CoreInterfaces.CORE_P.getValue());
            getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
            getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));

            this.lightRawColor.addDataListener(new ResourceDataListener<HashMap<String, Integer>>() {
                @Override
                public void onDataChanged(SmartObjectResource<HashMap<String, Integer>> resource, HashMap<String, Integer> updatedValue) {
                    updatedColorValue = updatedValue;
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
            SenMLRecord baseRecord = new SenMLRecord();
            baseRecord.setBn(String.format("%s:%s:", this.deviceId, this.getName()));
            baseRecord.setBu("/");
            baseRecord.setBver(VERSION);
            baseRecord.setBt(System.currentTimeMillis());
            senMLPack.add(baseRecord);
            this.updatedColorValue.forEach((color, value) -> {
                SenMLRecord senMLRecord = new SenMLRecord();
                senMLRecord.setN(color);
                senMLRecord.setV(value);
                senMLPack.add(senMLRecord);
            });

            return Optional.of(this.objectMapper.writeValueAsString(senMLPack));

        }catch (Exception e){
            return Optional.empty();
        }
    }

    @Override
    public void handleGET(CoapExchange exchange) {

        try{

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
                exchange.respond(CoAP.ResponseCode.CONTENT, this.objectMapper.writeValueAsBytes(this.updatedColorValue), MediaTypeRegistry.TEXT_PLAIN);

        }catch (Exception e){
            e.printStackTrace();
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void handlePUT(CoapExchange exchange) {

        try{

            // TODO: Grab put request in senML + JSON
            /*
            //If the request specify the MediaType as JSON or JSON+SenML
            if ((exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_SENML_JSON ||
                    exchange.getRequestOptions().getAccept() == MediaTypeRegistry.APPLICATION_JSON)
                    && exchange.getRequestPayload() != null) {

                String content = new String(exchange.getRequestPayload());

                MessageSenMLColor message = objectMapper.readValue(content, MessageSenMLColor.class);

                logger.info("Submitted value APPLICATION_SENML_JSON: {}", message);

                //Update internal status
                this.updatedColorValue = message.getValue();
                this.lightRawColor.setUpdatedValue(this.updatedColorValue);

                logger.info("Resource Status Updated APPLICATION_SENML_JSON: {}", this.updatedColorValue);

                exchange.respond(CoAP.ResponseCode.CHANGED);


            //If the request body is available
            } else if(exchange.getRequestOptions().getAccept() == MediaTypeRegistry.TEXT_PLAIN && exchange.getRequestPayload() != null){

                String submittedValue = new String(exchange.getRequestPayload());

                logger.info("Submitted value: {}", submittedValue);

                JsonNode node = objectMapper.readTree(new String(exchange.getRequestPayload()));
                HashMap<String, Integer> colorMap = new HashMap<String, Integer>();
                colorMap.put("red", node.get("red").asInt());
                colorMap.put("green", node.get("green").asInt());
                colorMap.put("blue", node.get("blue").asInt());

                //Update internal status
                this.updatedColorValue = colorMap;
                this.lightRawColor.setUpdatedValue(this.updatedColorValue);

                logger.info("Resource Status Updated: {}", this.updatedColorValue);

                exchange.respond(CoAP.ResponseCode.CHANGED);
            }
            */
            if(exchange.getRequestPayload() != null){

                String submittedValue = new String(exchange.getRequestPayload());

                logger.info("Submitted value: {}", submittedValue);

                JsonNode node = objectMapper.readTree(new String(exchange.getRequestPayload()));
                HashMap<String, Integer> colorMap = new HashMap<String, Integer>();
                colorMap.put("red", node.get("red").asInt());
                colorMap.put("green", node.get("green").asInt());
                colorMap.put("blue", node.get("blue").asInt());

                //Update internal status
                this.updatedColorValue = colorMap;
                this.lightRawColor.setUpdatedValue(this.updatedColorValue);

                logger.info("Resource Status Updated: {}", this.updatedColorValue);

                exchange.respond(CoAP.ResponseCode.CHANGED);
            }
            else
                exchange.respond(CoAP.ResponseCode.BAD_REQUEST);

        }catch (Exception e){
            logger.error("Error Handling POST -> {}", e.getLocalizedMessage());
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }

    }

}
