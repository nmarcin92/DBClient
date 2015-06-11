package pl.edu.agh.dbclient.objects;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author mnowak
 */
public class EntityRow {

    private Map<String, String> attributes;

    public EntityRow() {
        this.attributes = Maps.newLinkedHashMap();
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

}
