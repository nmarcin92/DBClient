package pl.edu.agh.dbclient.objects.operations;

import com.google.common.collect.Lists;
import pl.edu.agh.dbclient.objects.EntityAttribute;
import pl.edu.agh.dbclient.objects.EntityRow;

import java.util.List;

/**
 * @author mnowak
 */
public class UpdateOperation extends Operation<UpdateOperation> {

    private List<EntityAttribute> toAdd = Lists.newArrayList();
    private List<EntityAttribute> toDelete = Lists.newArrayList();
    private List<EntityAttribute> toModify = Lists.newArrayList();
    private List<AttributeRename> toRename = Lists.newArrayList();

    private List<String> preconditions;
    private EntityRow updated;


    public UpdateOperation() {
    }

    public UpdateOperation(OperationContext context, String entityName) {
        super(context, entityName);
    }

    public List<EntityAttribute> getToAdd() {
        return toAdd;
    }

    public List<EntityAttribute> getToDelete() {
        return toDelete;
    }

    public List<EntityAttribute> getToModify() {
        return toModify;
    }

    public List<AttributeRename> getToRename() {
        return toRename;
    }

    public EntityRow getUpdated() {
        return updated;
    }

    public List<String> getPreconditions() {
        return preconditions;
    }

    public void setUpdated(EntityRow updated) {
        this.updated = updated;
    }

    public void setPreconditions(List<String> preconditions) {
        this.preconditions = preconditions;
    }
}
