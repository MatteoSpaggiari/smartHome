package it.unimore.iot.smart.home.project.simulated_devices.server.resource.raw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;
import java.util.UUID;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 * @created 15/02/2021 - 11:02
 */
public class PresenceRawSensor extends SmartObjectResource<Boolean> {

    private static Logger logger = LoggerFactory.getLogger(PresenceRawSensor.class);

    private static final String LOG_DISPLAY_NAME = "PresenceSensor";

    private static final String RESOURCE_TYPE = "iot:sensor:presence";

    private Boolean isPresence;

    // Properties for simulation
    // milliseconds
    private static final long PRESENCE_DURATION_TIME_MIN = 2000;

    private static final long PRESENCE_DURATION_TIME_MAX = 10000;

    private static final long PRESENCE_DETECTION_TIME_MIN = 2000;

    private static final long PRESENCE_DETECTION_TIME_MAX = 5000;

    {
        this.isPresence = false;
    }

    // Constructor
    public PresenceRawSensor() {
        super(UUID.randomUUID().toString(), RESOURCE_TYPE);
        this.isPresence = false;
        behaviorSimulation();
    }

    // Getters and Setters
    public Boolean getPresence() {
        return isPresence;
    }

    public void setPresence(Boolean isPresence) {
        this.isPresence = isPresence;
        notifyUpdate(this.isPresence);
    }

    // Override Method SmartObjectResource
    @Override
    public Boolean loadUpdatedValue() {
        return this.isPresence;
    }

    /**
     * Method containing the code used to simulate the behavior of the presence sensor
     */
    private void behaviorSimulation() {

        Thread thread = new Thread() {
            public void run(){
                try {

                    while(true) {

                        Random random = new Random(System.currentTimeMillis());
                        long presenceDetectionEventTime = (long) (PRESENCE_DETECTION_TIME_MIN + ((PRESENCE_DETECTION_TIME_MAX - PRESENCE_DETECTION_TIME_MIN) * random.nextDouble()));
                        long presenceDuration = (long) (PRESENCE_DURATION_TIME_MIN + ((PRESENCE_DURATION_TIME_MAX - PRESENCE_DURATION_TIME_MIN) * random.nextDouble()));

                        // Presence detection true
                        logger.info("Presence Detection Event Time {}", presenceDetectionEventTime);
                        this.sleep(presenceDetectionEventTime);
                        logger.info("Presence Detection");
                        setPresence(true);


                        logger.info("Presence Duration {}", presenceDuration);
                        this.sleep(presenceDuration);
                        logger.info("No presence");
                        // Presence detection false
                        setPresence(false);

                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();

    }

    public static void main(String[] args) {
        PresenceRawSensor rawResource = new PresenceRawSensor();
        logger.info("New {} Resource Created with Id: {} ! {} New Value: {}",
                rawResource.getType(),
                rawResource.getId(),
                LOG_DISPLAY_NAME,
                rawResource.loadUpdatedValue());

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
