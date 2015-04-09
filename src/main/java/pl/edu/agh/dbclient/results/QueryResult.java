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
        this.errors = new ArrayList<String>();
        setSuccess(true);
        setNextPage(false);
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public boolean isNextPage() {
        return nextPage;
    }

    public void setNextPage(boolean nextPage) {
        this.nextPage = nextPage;
    }
}
