package pl.edu.agh.dbclient.objects.operations;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author mnowak
 */
public class CreateOperation extends Operation<CreateOperation> {

    private Map<String, String> attributes = new TreeMap<>();

    public CreateOperation() {
    }

    public CreateOperation(OperationContext context, String entityName) {
        super(context, entityName);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(String name, String value) {
        attributes.put(name, value);
    }

}
