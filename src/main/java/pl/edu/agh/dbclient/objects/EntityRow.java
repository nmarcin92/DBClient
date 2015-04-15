package pl.edu.agh.dbclient.objects;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mnowak
 */
public class EntityRow {

    private Map<String, String> attributes;

    public EntityRow() {
        this.attributes = new HashMap<String, String>();
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }
}
