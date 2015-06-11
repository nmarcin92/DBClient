package pl.edu.agh.dbclient.objects.operations;

/**
 * @author mnowak
 */
public class AttributeRename {

    private String oldName;
    private String newName;

    public AttributeRename() {
    }

    public AttributeRename(String oldName, String newName) {
        this.oldName = oldName;
        this.newName = newName;
    }

    public String getOldName() {
        return oldName;
    }

    public String getNewName() {
        return newName;
    }
}
