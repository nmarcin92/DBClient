package pl.edu.agh.dbclient.operations;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mnowak
 */
public abstract class Operation<T extends Operation<T>> {

    private final Map<Parameter, String> attributes = new HashMap<Parameter, String>();
    private final OperationContext context;
    private final String entityName;

    public Operation(OperationContext context, String entityName) {
        this.context = context;
        this.entityName = entityName;
    }

    public OperationContext getContext() {
        return context;
    }

    public String getEntityName() {
        return entityName;
    }

    public T addParameter(Parameter param, String value) {
        attributes.put(param, value);
        return (T) this;
    }

    public T addParameter(Parameter param) {
        return addParameter(param, null);
    }

    public boolean hasAttribute(String name) { return attributes.containsKey(name); }

    public String getAttribute(String name) { return attributes.get(name); }

    public enum OperationContext {
        DATABASE, ENTITY, RECORD; // .. CONSTRAINT, INDEX etc
    }

    protected interface Parameter {}

}
