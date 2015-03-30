package pl.edu.agh.dbclient.operations;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mnowak
 */
public abstract class Operation<T> {

    private final Map<String, String> attributes = new HashMap<String, String>();

    public Operation<T> addParameter(String name, String value) {
        this.attributes.put(name, value);
        return this;
    }

    public boolean hasAttribute(String name) { return attributes.containsKey(name); }

    public String getAttribute(String name) { return attributes.get(name); }

    public enum OperationContext {
        ENTITY, RECORD; // .. CONSTRAINT, INDEX etc
    }

}
