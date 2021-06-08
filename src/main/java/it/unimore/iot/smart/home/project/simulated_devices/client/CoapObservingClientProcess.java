package it.unimore.iot.smart.home.project.simulated_devices.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimore.iot.smart.home.project.simulated_devices.message.MessageSenMLPresence;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * A simple CoAP Synchronous Client implemented using Californium Java Library
 * The client Observe a target resource for n Seconds and then cancel the request and ends the execution
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project coap-demo-smartobject
 * @created 20/10/2020 - 21:54
 */
public class CoapObservingClientProcess {

    private final static Logger logger = LoggerFactory.getLogger(CoapObservingClientProcess.class);

    public static void main(String[] args) {

        ObjectMapper objectMapper = new ObjectMapper();

        String targetCoapResourceURL = "coap://127.0.0.1:5683/presence-sensor";

        CoapClient client = new CoapClient(targetCoapResourceURL);

        logger.info("OBSERVING ... {}", targetCoapResourceURL);

        Request request = new Request(CoAP.Code.GET);
        //Request request = Request.newGet().setURI(targetCoapResourceURL).setObserve();
        request.setConfirmable(true);
        //Set Options to receive the response as JSON+SenML MediaType
        request.setOptions(new OptionSet().setObserve(0).setAccept(MediaTypeRegistry.APPLICATION_SENML_JSON));

        // NOTE:
        // The client.observe(Request, CoapHandler) method visibility has been changed from "private"
        // to "public" in order to get the ability to change the parameter of the observable GET
        //(e.g., to change token and MID).
        CoapObserveRelation relation = client.observe(request, new CoapHandler() {

            public void onLoad(CoapResponse response) {

                String content = response.getResponseText();
                //logger.info("Notification Response Pretty Print: \n{}", Utils.prettyPrint(response));
                logger.info("NOTIFICATION Body: " + content);

                try {

                    MessageSenMLPresence message = objectMapper.readValue(content, MessageSenMLPresence.class);
                    logger.info("Message receveid {}", message);
                    logger.info("Message receveid {}", message.getValue());

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            public void onError() {
                logger.error("OBSERVING FAILED");
            }
        });

        // Observes the coap resource for 30 seconds then the observing relation is deleted
        try {
            Thread.sleep(60*3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("CANCELLATION.....");
        relation.proactiveCancel();

    }

}
