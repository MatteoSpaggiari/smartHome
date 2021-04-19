package it.unimore.iot.smart.home.project.edge_application.coap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Matteo Spaggiari, 262475@studenti.unimore.it - matteo.spaggiari78@gmail.com
 * @project smart-home-project
 */
public class CoapPutRequestLightColor extends CoapPutRequest<HashMap<String, Integer>> {

    public CoapPutRequestLightColor(String coapEndpoint, HashMap<String, Integer> value) {
        super(coapEndpoint, value);
    }

    @Override
    protected String getPayload() {
        final StringBuffer sb = new StringBuffer("{");
        this.getValue().forEach((color, value) -> {
            sb.append('"'+color+'"');
            sb.append(":");
            sb.append(value);
            sb.append(",");
        });
        // Remove the last comma
        sb.deleteCharAt(sb.length()-1);
        sb.append("}");
        return sb.toString();
    }

    public static void main(String[] args) {

        HashMap<String, Integer> colorMap = new HashMap<String, Integer>();
        ObjectMapper objectMapper = new ObjectMapper();
        String colorString = "{\"red\":230,\"green\":0,\"blue\":0}";
        System.out.println(colorString);

        JsonNode node = null;
        try {
            node = objectMapper.readTree(colorString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        colorMap.put("red", node.get("red").asInt());
        colorMap.put("green", node.get("green").asInt());
        colorMap.put("blue", node.get("blue").asInt());
        colorMap.forEach((color,value) -> {
            System.out.print(color);
            System.out.print(":");
            System.out.print(value);
        });
    }
}
