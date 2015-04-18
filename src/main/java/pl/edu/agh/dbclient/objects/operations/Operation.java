package pl.edu.agh.dbclient.objects.operations;

import pl.edu.agh.dbclient.objects.UserSession;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author mnowak
 */
public abstract class Operation<T extends Operation> {

    private Map<OperationParameter, String> parameters = new EnumMap<OperationParameter, String>(OperationParameter.class);
    private OperationContext context;
    private String entityName;
    private UserSession userSession;

    public Operation() {
    }

    ;

    public Operation(OperationContext context, String entityName) {
        this.context = context;
        this.entityName = entityName;
    }

    public OperationContext getContext() {
        return context;
    }

    public String getEntityName() {
        return entityName;
    }

    public <T extends Operation> T addParameter(OperationParameter param, String value) {
        parameters.put(param, value);
        return (T) this;
    }

    public <T extends Operation> T addParameter(OperationParameter param) {
        return addParameter(param, null);
    }

    public boolean hasParameter(OperationParameter param) {
        return getParameters().containsKey(param);
    }

    public String getParameter(OperationParameter param) {
        return getParameters().get(param);
    }

    public Map<OperationParameter, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<OperationParameter, String> parameters) {
        this.parameters = parameters;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public enum OperationContext {
        DATABASE, ENTITY, RECORD; // .. CONSTRAINT, INDEX etc
    }

    public enum OperationParameter {
        ENTIRE_RECORD;
    }


}