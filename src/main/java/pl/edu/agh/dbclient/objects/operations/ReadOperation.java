package pl.edu.agh.dbclient.objects.operations;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mnowak
 */
public class ReadOperation extends Operation<ReadOperation> {

    private List<String> attributeNames = new ArrayList<>();

    public ReadOperation() {
    }

    public ReadOperation(OperationContext context, String entityName) {
        super(context, entityName);
    }

    public List<String> getAttributeNames() {
        return attributeNames;
    }

    public void setAttributeNames(List<String> attributeNames) {
        this.attributeNames = attributeNames;
    }

    public void addAttributeName(String name) {
        attributeNames.add(name);
    }

}
