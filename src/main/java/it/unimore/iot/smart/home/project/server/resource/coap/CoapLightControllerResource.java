package it.unimore.iot.smart.home.project.server.resource.coap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimore.iot.smart.home.project.server.resource.model.LightControllerModel;
import it.unimore.iot.smart.home.project.server.resource.raw.LightControllerRawSmartObject;
import it.unimore.iot.smart.home.project.server.resource.raw.ResourceDataListener;
import it.unimore.iot.smart.home.project.server.resource.raw.SmartObjectResource;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import it.unimore.iot.smart.home.project.utils.CoreInterfaces;
import it.unimore.iot.smart.home.project.utils.SenMLPack;
import it.unimore.iot.smart.home.project.utils.SenMLRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class CoapLightControllerResource extends CoapResource {

    private final static Logger logger = LoggerFactory.getLogger(CoapLightControllerResource.class);

    private static final String OBJECT_TITLE = "LightController";

    private static final Number VERSION = 0.1;

    private LightControllerRawSmartObject lightControllerRawSmartObject;

    private LightControllerModel lightControllerModel;

    private ObjectMapper objectMapper;

    private String deviceId;

    public CoapLightControllerResource(String deviceId, String name, LightControllerRawSmartObject lightControllerRawSmartObject) {

        super(name);

        if(lightControllerRawSmartObject != null && deviceId != null){

            this.deviceId = deviceId;

            this.lightControllerRawSmartObject = lightControllerRawSmartObject;

            //Jackson Object Mapper + Ignore Null Fields in order to properly generate the SenML Payload
            this.objectMapper = new ObjectMapper();
            this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            setObservable(true); // enable observing
            setObserveType(CoAP.Type.CON); // configure the notification type to CONs

            getAttributes().setTitle(OBJECT_TITLE);
            getAttributes().setObservable();
            getAttributes().addAttribute("rt", lightControllerRawSmartObject.getType());
            getAttributes().addAttribute("if", CoreInterfaces.CORE_P.getValue());
            getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
            getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));
        }
        else
            logger.error("Error -> NULL Raw Reference !");

        this.lightControllerRawSmartObject.addDataListener(new ResourceDataListener<LightControllerModel>() {
            @Override
            public void onDataChanged(SmartObjectResource<LightControllerModel> resource, LightControllerModel updatedValue) {
                lightControllerModel = updatedValue;
                changed();
            }
        });

    }

    /**
     * Create the SenML Response with the updated value and the resource information
     * @return
     */
    private Optional<String> getJsonSenmlResponse(){
        /*
        try{

            SenMLPack senMLPack = new SenMLPack();

            SenMLRecord baseRecord = new SenMLRecord();
            baseRecord.setBn(String.format("%s:%s", this.deviceId, this.getName()));
            baseRecord.setBver(VERSION);

            SenMLRecord minTempRecord = new SenMLRecord();
            minTempRecord.setN("min_temperature");
            minTempRecord.setV(configurationModelValue.getMinTemperature());

            SenMLRecord maxTempRecord = new SenMLRecord();
            maxTempRecord.setN("max_temperature");
            maxTempRecord.setV(configurationModelValue.getMaxTemperature());

            SenMLRecord hvacUriRecord = new SenMLRecord();
            hvacUriRecord.setN("hvac_res_uri");
            hvacUriRecord.setVs(configurationModelValue.getHvacUnitResourceUri());

            SenMLRecord operationalModeRecord = new SenMLRecord();
            operationalModeRecord.setN("operational_mode");
            operationalModeRecord.setVs(configurationModelValue.getOperationalMode());

            senMLPack.add(baseRecord);
            senMLPack.add(minTempRecord);
            senMLPack.add(maxTempRecord);
            senMLPack.add(hvacUriRecord);
            senMLPack.add(operationalModeRecord);

            return Optional.of(this.objectMapper.writeValueAsString(senMLPack));

        }catch (Exception e){
            return Optional.empty();
        }
        */
        return null;
    }
}
