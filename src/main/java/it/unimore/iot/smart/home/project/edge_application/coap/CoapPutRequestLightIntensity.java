package it.unimore.iot.smart.home.project.edge_application.coap;

public class CoapPutRequestLightIntensity extends CoapPutRequest<Double> {

    public CoapPutRequestLightIntensity(String coapEndpoint, Double value) {
        super(coapEndpoint, value);
    }

    @Override
    protected String getPayload() {
        return Double.toString(this.getValue());
    }
}
