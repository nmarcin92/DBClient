package pl.edu.agh.dbclient.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mnowak
 */
public class QueryResult {

    private boolean success;
    private Entity entity;
    private List<String> errors;
    private boolean nextPage;

    public static QueryResult createErrorResult(String errorMessage) {
        QueryResult qr = new QueryResult();
        qr.setSuccess(false);
        qr.addError(errorMessage);
        return qr;
    }

    public QueryResult() {
        this.errors = new ArrayList<>();
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
