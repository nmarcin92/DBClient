package pl.edu.agh.dbclient.objects.operations;

/**
 * @author mnowak
 */
public class DeleteOperation extends Operation<DeleteOperation> {

    public DeleteOperation() {}

    public DeleteOperation(OperationContext context, String entityName) {
        super(context, entityName);
    }
}
