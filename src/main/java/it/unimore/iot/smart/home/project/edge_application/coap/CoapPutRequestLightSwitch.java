package it.unimore.iot.smart.home.project.edge_application.coap;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class CoapPutRequestLightSwitch extends CoapPutRequest<Boolean> {

    public CoapPutRequestLightSwitch(String coapEndpoint) {
        super(coapEndpoint);
    }

    public CoapPutRequestLightSwitch(String coapEndpoint, Boolean value) {
        super(coapEndpoint, value);
    }

    @Override
    protected String getPayload() {
        return Boolean.toString(this.getValue());
    }
}
