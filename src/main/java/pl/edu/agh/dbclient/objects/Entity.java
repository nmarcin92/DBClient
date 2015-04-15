package pl.edu.agh.dbclient.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mnowak
 */
public class Entity {

    private final String entityName;
    private List<EntityAttribute> attributes = new ArrayList<EntityAttribute>();
    private List<EntityRow> rows = new ArrayList<EntityRow>();

    public Entity(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityName() {
        return entityName;
    }

    public List<EntityAttribute> getAttributes() {
        return attributes;
    }

    public List<EntityRow> getRows() {
        return rows;
    }
}
