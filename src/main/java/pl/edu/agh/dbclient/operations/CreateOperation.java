package pl.edu.agh.dbclient.operations;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mnowak
 */
public class CreateOperation extends Operation {

    private final OperationContext type;
    private final String entityName;

    public CreateOperation(OperationContext type, String entityName) {
        this.type = type;
        this.entityName = entityName;
    }

    public OperationContext getType() {
        return type;
    }

    public String getEntityName() {
        return entityName;
    }

}
