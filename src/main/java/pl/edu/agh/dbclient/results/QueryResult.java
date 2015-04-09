package pl.edu.agh.dbclient.results;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mnowak
 */
public class QueryResult {

    private boolean success;
    private Entity entity;
    private List<String> errors;
    private boolean nextPage;

    public QueryResult() {
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }



}
