package pl.edu.agh.dbclient.objects.operations;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mnowak
 */
public class ReadOperation extends Operation<ReadOperation> {

    public ReadOperation() {}

    public ReadOperation(OperationContext context, String entityName) {
        super(context, entityName);
    }


}
