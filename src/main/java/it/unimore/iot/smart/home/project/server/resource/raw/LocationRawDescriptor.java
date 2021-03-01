package it.unimore.iot.smart.home.project.server.resource.raw;

import it.unimore.iot.smart.home.project.server.resource.model.LocationDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocationRawDescriptor {

    private static Logger logger = LoggerFactory.getLogger(LocationRawDescriptor.class);

    private static final String LOG_DISPLAY_NAME = "Location";

    private static final String RESOURCE_TYPE = "iot:location";

    private String type;

    private LocationDescriptor locationDescriptor;

    {
        this.type = RESOURCE_TYPE;
    }

    public LocationRawDescriptor(LocationDescriptor locationDescriptor) {

        this.locationDescriptor = locationDescriptor;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocationDescriptor getLocationDescriptor() {
        return locationDescriptor;
    }

    public void setLocationDescriptor(LocationDescriptor locationDescriptor) {
        this.locationDescriptor = locationDescriptor;
    }


}
