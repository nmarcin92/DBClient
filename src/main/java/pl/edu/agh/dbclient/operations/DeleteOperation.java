package pl.edu.agh.dbclient.operations;

/**
 * @author mnowak
 */
public class DeleteOperation extends Operation<DeleteOperation> {

    public DeleteOperation(OperationContext context, String entityName) {
        super(context, entityName);
    }
}
