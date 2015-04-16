package pl.edu.agh.dbclient.objects.operations;

/**
 * @author mnowak
 */
public class UpdateOperation extends Operation<UpdateOperation> {

    public UpdateOperation(OperationContext context, String entityName) {
        super(context, entityName);
    }
}
