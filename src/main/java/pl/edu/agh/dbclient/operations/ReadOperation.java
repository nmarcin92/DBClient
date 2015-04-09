package pl.edu.agh.dbclient.operations;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 * @author mnowak
 */
public class ReadOperation extends Operation<ReadOperation> {

    public ReadOperation(OperationContext context, String entityName) {
        super(context, entityName);
    }


}
