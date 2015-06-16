package pl.edu.agh.dbclient.objects.operations;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author mnowak
 */
public class DeleteOperation extends Operation<DeleteOperation> {

    private List<String> preconditions;

    public DeleteOperation() {

    }

    public DeleteOperation(OperationContext context, String entityName) {
        super(context, entityName);
        preconditions = Lists.newArrayList();
    }

    public List<String> getPreconditions() {
        return preconditions;
    }

    public void setPreconditions(List<String> preconditions) {
        this.preconditions = preconditions;
    }
}
