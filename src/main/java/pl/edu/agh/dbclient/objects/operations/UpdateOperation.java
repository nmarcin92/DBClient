package pl.edu.agh.dbclient.objects.operations;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import pl.edu.agh.dbclient.objects.EntityAttribute;
import pl.edu.agh.dbclient.objects.EntityRow;

import java.util.List;
import java.util.Map;

/**
 * @author mnowak
 */
public class UpdateOperation extends Operation<UpdateOperation> {

    private List<EntityAttribute> toAdd = Lists.newArrayList();
    private List<EntityAttribute> toDelete = Lists.newArrayList();
    private List<EntityAttribute> toModify = Lists.newArrayList();
    private Map<String, String> toRename = Maps.newHashMap();

    private String id;
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

    public Map<String, String> getToRename() {
        return toRename;
    }

    public EntityRow getUpdated() {
        return updated;
    }

    public String getId() {
        return id;
    }

    public void setUpdated(EntityRow updated) {
        this.updated = updated;
    }

    public void setId(String id) {
        this.id = id;
    }
}
