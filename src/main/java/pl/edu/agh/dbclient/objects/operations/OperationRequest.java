package pl.edu.agh.dbclient.objects.operations;

import pl.edu.agh.dbclient.objects.UserSession;

/**
 * @author mnowak
 */
public class OperationRequest {

    private UserSession session;
    private ReadOperation operation;

    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }

    public ReadOperation getOperation() {
        return operation;
    }

    public void setOperation(ReadOperation operation) {
        this.operation = operation;
    }
}
