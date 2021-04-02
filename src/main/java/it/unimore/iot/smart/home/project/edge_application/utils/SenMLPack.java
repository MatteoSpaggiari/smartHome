package it.unimore.iot.smart.home.project.edge_application.utils;

import java.util.ArrayList;

/**
 *
 * List of SenMLRecord
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project coap-demo-smartobject
 * @created 27/10/2020 - 22:27
 */
public class SenMLPack extends ArrayList<SenMLRecord> {

    private ArrayList<SenMLRecord> list;

    {
        this.list = new ArrayList<SenMLRecord>();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("{");
        sb.append(list);
        sb.append('}');
        return sb.toString();
    }
}
