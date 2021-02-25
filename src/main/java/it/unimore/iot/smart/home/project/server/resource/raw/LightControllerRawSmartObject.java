package it.unimore.iot.smart.home.project.server.resource.raw;

import it.unimore.iot.smart.home.project.server.resource.model.LightControllerModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 * @created 15/02/2021 - 11:30
 */
public class LightControllerRawSmartObject extends SmartObjectResource<LightControllerModel> {

    private static Logger logger = LoggerFactory.getLogger(LightControllerRawSmartObject.class);

    private LightControllerModel lightControllerModel;

    private static final String RESOURCE_TYPE = "iot:light:controller";

    public LightControllerRawSmartObject(LightControllerModel lightControllerModel) {
        super(UUID.randomUUID().toString(), RESOURCE_TYPE);
        this.lightControllerModel = lightControllerModel;
    }

    public LightControllerModel getLightControllerModel() {
        return lightControllerModel;
    }

    public void setLightControllerModel(LightControllerModel lightControllerModel) {
        this.lightControllerModel = lightControllerModel;
    }

    @Override
    public LightControllerModel loadUpdatedValue() {
        return this.lightControllerModel;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LightControllerRawSmartObject{");
        sb.append("lightControllerModel=").append(lightControllerModel);
        sb.append('}');
        return sb.toString();
    }
}
