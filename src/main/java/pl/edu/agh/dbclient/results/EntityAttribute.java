package pl.edu.agh.dbclient.results;

/**
 * @author mnowak
 */
public class EntityAttribute {

    private final String attributeName;

    public EntityAttribute(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }
}
