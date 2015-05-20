package pl.edu.agh.dbclient.objects;

import com.google.common.base.Objects;

/**
 * @author mnowak
 */
public class EntityAttribute {

    private String attributeName;

    private String dataType;

    public EntityAttribute() {
    }

    public EntityAttribute(String attributeName) {
        this(attributeName, null);
    }

    public EntityAttribute(String attributeName, String dataType) {
        this.attributeName = attributeName;
        this.dataType = dataType;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        return attributeName.equals(((EntityAttribute) obj).getAttributeName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.attributeName);
    }
}
