package pl.edu.agh.dbclient.objects;

/**
 * @author mnowak
 */
public class EntityAttribute {

    private final String attributeName;

    private String dataType;

    public EntityAttribute(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
