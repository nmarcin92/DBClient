package pl.edu.agh.dbclient.objects.operations;

/**
 * @author mnowak
 */
public class CommandOperation extends Operation<CommandOperation> {

    private String query;
    private boolean hasResult;

    public CommandOperation() {}

    public CommandOperation(OperationContext context, String entityName) {
        super(context, entityName);
    }

    public String getQuery() {
        return query;
    }

    public boolean isHasResult() {
        return hasResult;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setHasResult(boolean hasResult) {
        this.hasResult = hasResult;
    }
}
