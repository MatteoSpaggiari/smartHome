package it.unimore.iot.smart.home.project.simulated_devices.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unimore.iot.smart.home.project.simulated_devices.message.MessageSenMLIntensity;
import it.unimore.iot.smart.home.project.simulated_devices.message.MessageSenMLSwitch;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.OptionSet;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * A simple CoAP Synchronous Client implemented using Californium Java Library
 * The simple client send a GET request to a target CoAP Resource with some custom request parameters
 * Use SenML+JSON as request Media Type Option
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project coap-demo-smartobject
 * @created 20/10/2020 - 09:19
 */
public class CoapGetSenmlClientProcess {

	private final static Logger logger = LoggerFactory.getLogger(CoapGetSenmlClientProcess.class);

	private static final String COAP_ENDPOINT = "coap://127.0.0.1:5783/light-controller/switch";

	public static void main(String[] args) {

		ObjectMapper objectMapper = new ObjectMapper();

		//Initialize coapClient
		CoapClient coapClient = new CoapClient(COAP_ENDPOINT);

		//Request Class is a generic CoAP message: in this case we want a GET.
		//"Message ID", "Token" and other header's fields can be set 
		Request request = new Request(Code.GET);

		//Set Request as Confirmable
		request.setConfirmable(true);

		//Set Options to receive the response as JSON+SenML MediaType
		request.setOptions(new OptionSet().setAccept(MediaTypeRegistry.APPLICATION_SENML_JSON));

		logger.info("Request Pretty Print: \n{}", Utils.prettyPrint(request));

		//Synchronously send the GET message (blocking call)
		CoapResponse coapResp = null;

		try {

			coapResp = coapClient.advanced(request);

			//Pretty print for the received response
			logger.info("Response Pretty Print: \n{}", Utils.prettyPrint(coapResp));

			//The "CoapResponse" message contains the response.
			String text = coapResp.getResponseText();
			logger.info("Payload: {}", text);

			MessageSenMLSwitch message = objectMapper.readValue(text, MessageSenMLSwitch.class);

			logger.info("Message receveid {}", message);
			logger.info("Message receveid {}", message.getValue());

			/*
			text = text.substring(1,(text.length()-1));

			logger.info("Message format {}", text);

			MessageSenMLIntensity message = objectMapper.readValue(text, MessageSenMLIntensity.class);

			logger.info("Message receveid {}", message);
			*/

			/*
			logger.info("Message ID: " + coapResp.advanced().getMID());
			logger.info("Token: " + coapResp.advanced().getTokenString());
			*/

		} catch (ConnectorException | IOException e) {
			e.printStackTrace();
		}
	}
}