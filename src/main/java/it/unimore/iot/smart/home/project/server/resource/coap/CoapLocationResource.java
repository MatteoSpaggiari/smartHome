package it.unimore.iot.smart.home.project.server.resource.coap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimore.iot.smart.home.project.server.resource.raw.LocationRawDescriptor;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import it.unimore.iot.smart.home.project.utils.CoreInterfaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoapLocationResource extends CoapResource {

    private final static Logger logger = LoggerFactory.getLogger(CoapLocationResource.class);

    private static final String OBJECT_TITLE = "Location";

    private static final Number VERSION = 0.1;

    private LocationRawDescriptor LocationRawDescriptor;

    private ObjectMapper objectMapper;

    private String deviceId;

    public CoapLocationResource(String deviceId, String name, LocationRawDescriptor locationRawDescriptor) {

        super(name);

        if(LocationRawDescriptor != null && deviceId != null){

            this.deviceId = deviceId;

            this.LocationRawDescriptor = locationRawDescriptor;

            //Jackson Object Mapper + Ignore Null Fields in order to properly generate the SenML Payload
            this.objectMapper = new ObjectMapper();
            this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            setObservable(true); // enable observing
            setObserveType(CoAP.Type.CON); // configure the notification type to CONs

            getAttributes().setTitle(OBJECT_TITLE);
            getAttributes().setObservable();
            getAttributes().addAttribute("rt", locationRawDescriptor.getType());
            getAttributes().addAttribute("if", CoreInterfaces.CORE_P.getValue());
            getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.APPLICATION_SENML_JSON));
            getAttributes().addAttribute("ct", Integer.toString(MediaTypeRegistry.TEXT_PLAIN));
        }
        else
            logger.error("Error -> NULL Raw Reference !");

        /*
        this.thermostatRawConfigurationParameter.addDataListener(new ResourceDataListener<ThermostatConfigurationModel>() {
            @Override
            public void onDataChanged(SmartObjectResource<ThermostatConfigurationModel> resource, ThermostatConfigurationModel updatedValue) {
                configurationModelValue = updatedValue;
                changed();
            }
        });
        */
    }

}
