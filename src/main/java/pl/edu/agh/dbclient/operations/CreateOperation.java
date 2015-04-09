package pl.edu.agh.dbclient.operations;

/**
 * @author mnowak
 */
public class CreateOperation extends Operation<CreateOperation> {

    public CreateOperation(OperationContext context, String entityName) {
        super(context, entityName);
    }

}
