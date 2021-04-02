package it.unimore.iot.smart.home.project.simulated_devices.server.resource.coap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimore.iot.smart.home.project.simulated_devices.server.resource.raw.LightRawIntensity;
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

public class CoapLightIntensityParameterResource extends CoapResource {

    private final static Logger logger = LoggerFactory.getLogger(CoapLightIntensityParameterResource.class);

    private static final String OBJECT_TITLE = "LightIntensityParameter";

    private static final Number VERSION = 0.1;

    private LightRawIntensity lightRawIntensity;

    private Double updatedIntensityValue;

    private ObjectMapper objectMapper;

    private String deviceId;

    {
        this.updatedIntensityValue = 100.0;
    }

    public CoapLightIntensityParameterResource(String deviceId, String name, LightRawIntensity lightRawIntensity) {

        super(name);

        if(lightRawIntensity != null && deviceId != null){

            this.deviceId = deviceId;

            this.lightRawIntensity = lightRawIntensity;

            //Jackson Object Mapper + Ignore Null Fields in order to properly generate the SenML Payload
            this.objectMapper = new ObjectMapper();
            this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            setObservable(true); // enable observing
            setObserveType(CoAP.Type.CON); // configure the notification type to CONs

            getAttributes().setTitle(OBJECT_TITLE);
            getAttributes().setObservable();
            getAttributes().addAttribute("rt", lightRawIntensity.getType());
            getAttributes().addAttribute("if", CoreInterfaces.CORE_P.getValue());
            getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
            getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));
        }
        else
            logger.error("Error -> NULL Raw Reference !");

        this.lightRawIntensity.addDataListener(new ResourceDataListener<Double>() {

            @Override
            public void onDataChanged(SmartObjectResource<Double> resource, Double updatedValue) {
                updatedIntensityValue = updatedValue;
                changed();
            }
        });

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
            senMLRecord.setV(this.updatedIntensityValue);
            senMLRecord.setT(System.currentTimeMillis());

            senMLPack.add(senMLRecord);

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
                exchange.respond(CoAP.ResponseCode.CONTENT, this.objectMapper.writeValueAsBytes(this.updatedIntensityValue), MediaTypeRegistry.TEXT_PLAIN);

        }catch (Exception e){
            e.printStackTrace();
            exchange.respond(CoAP.ResponseCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void handlePUT(CoapExchange exchange) {

        try{

            //If the request body is available
            if(exchange.getRequestPayload() != null){

                double submittedValue = Double.parseDouble(new String(exchange.getRequestPayload()));

                logger.info("Submitted value: {}", submittedValue);

                //Update internal status
                this.updatedIntensityValue = submittedValue;
                this.lightRawIntensity.setUpdatedValue(this.updatedIntensityValue);

                logger.info("Resource Status Updated: {}", this.updatedIntensityValue);

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
