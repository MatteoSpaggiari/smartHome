package it.unimore.iot.smart.home.project.edge_application.coap;

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
