package it.unimore.iot.smart.home.project.edge_application.coap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public abstract class CoapPutRequest<T> {

	private final static Logger logger = LoggerFactory.getLogger(CoapPutRequest.class);

	private String coapEndpoint;

	private T value;

	public CoapPutRequest(String coapEndpoint) {
		this.coapEndpoint = coapEndpoint;
	}

	public CoapPutRequest(String coapEndpoint, T value) {
		this.coapEndpoint = coapEndpoint;
		this.value = value;
	}

	public String getCoapEndpoint() {
		return coapEndpoint;
	}

	public void setCoapEndpoint(String coapEndpoint) {
		this.coapEndpoint = coapEndpoint;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public void sendRequest() {

		//Initialize coapClient
		CoapClient coapClient = new CoapClient(this.coapEndpoint);

		//Request Class is a generic CoAP message: in this case we want a PUT.
		//"Message ID", "Token" and other header's fields can be set 
		Request request = new Request(Code.PUT);
		//Set Request as Confirmable
		request.setConfirmable(true);

		//Set PUT request's payload
		String myPayload = getPayload();
		logger.info("PUT Request Random Payload: {}", myPayload);
		request.setPayload(myPayload);

		//logger.info("Request Pretty Print: \n{}", Utils.prettyPrint(request));

		CoapHandler handler = new CoapHandler() {

			public void onLoad(CoapResponse coapResp) {

				//logger.info("PrettyPrint Response: \n{}", Utils.prettyPrint(coapResp));
			}

			public void onError() {
				logger.error("Failed");
			}
		};

		coapClient.advanced(handler, request);
	}

	protected abstract String getPayload();

}