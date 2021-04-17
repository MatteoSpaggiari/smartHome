package it.unimore.iot.smart.home.project.edge_application.coap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.CoAP.Code;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.elements.exception.ConnectorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Semaphore;

/**
 * A simple CoAP Synchronous Client implemented using Californium Java Library
 * The simple client send a PUT request to a target CoAP Resource with some custom request parameters
 * and payload
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project coap-demo-smartobject
 * @created 20/10/2020 - 09:19
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


		logger.info("Request Pretty Print: \n{}", Utils.prettyPrint(request));

		CoapHandler handler = new CoapHandler() {

			public void onLoad(CoapResponse coapResp) {

				logger.info("PrettyPrint Response: \n{}", Utils.prettyPrint(coapResp));
			}

			public void onError() {
				logger.error("Failed");
			}
		};

		coapClient.advanced(handler, request);
	}

	protected abstract String getPayload();

}