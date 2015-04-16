package pl.edu.agh.dbclient.objects.operations;

/**
 * @author mnowak
 */
public class CreateOperation extends Operation<CreateOperation> {

    public CreateOperation(OperationContext context, String entityName) {
        super(context, entityName);
    }

    public enum CreateParameter implements Parameter {
        ENTIRE_RECORD, ATTRIBUTE_NAMES, ATTRIBUTE_VALUES;
    }
 }
