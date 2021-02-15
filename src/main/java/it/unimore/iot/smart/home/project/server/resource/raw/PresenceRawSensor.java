package it.unimore.iot.smart.home.project.server.resource.raw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 * @created 15/02/2021 - 11:02
 */
public class PresenceRawSensor extends SmartObjectResource<Boolean> {

    private static Logger logger = LoggerFactory.getLogger(PresenceRawSensor.class);

    private static final String LOG_DISPLAY_NAME = "PresenceSensor";

    private static final String RESOURCE_TYPE = "iot.sensor.presence";

    private Boolean isPresence;

    public PresenceRawSensor() {
        super(UUID.randomUUID().toString(), RESOURCE_TYPE);
        this.isPresence = false;
    }

    public Boolean getPresence() {
        return isPresence;
    }

    public void setPresence(Boolean isPresence) {
        this.isPresence = isPresence;
        notifyUpdate(this.isPresence);
    }

    @Override
    public Boolean loadUpdatedValue() {
        return this.isPresence;
    }

    public static void main(String[] args) {
        PresenceRawSensor rawResource = new PresenceRawSensor();
        logger.info("New {} Resource Created with Id: {} ! {} New Value: {}",
                rawResource.getType(),
                rawResource.getId(),
                LOG_DISPLAY_NAME,
                rawResource.loadUpdatedValue());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    for(int i=0; i<100; i++){
                        rawResource.setPresence(!rawResource.loadUpdatedValue());
                        Thread.sleep(1000);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();

        rawResource.addDataListener(new ResourceDataListener<Boolean>() {
            @Override
            public void onDataChanged(SmartObjectResource<Boolean> resource, Boolean updatedValue) {

                if(resource != null && updatedValue != null)
                    logger.info("Device: {} -> New Value Received: {}", resource.getId(), updatedValue);
                else
                    logger.error("onDataChanged Callback -> Null Resource or Updated Value !");
            }
        });
    }
}
