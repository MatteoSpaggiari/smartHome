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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            this.lightControllerModel = lightControllerRawSmartObject.loadUpdatedValue();

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

        this.lightControllerRawSmartObject.addDataListener(new ResourceDataListener<LightControllerModel>() {
            @Override
            public void onDataChanged(SmartObjectResource<LightControllerModel> resource, LightControllerModel updatedValue) {
                lightControllerModel = updatedValue;
                changed();
            }
        });

    }

}
