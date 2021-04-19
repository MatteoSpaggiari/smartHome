package it.unimore.iot.smart.home.project.edge_application.coap;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class CoapPutRequestLightIntensity extends CoapPutRequest<Double> {

    public CoapPutRequestLightIntensity(String coapEndpoint, Double value) {
        super(coapEndpoint, value);
    }

    @Override
    protected String getPayload() {
        return Double.toString(this.getValue());
    }
}
