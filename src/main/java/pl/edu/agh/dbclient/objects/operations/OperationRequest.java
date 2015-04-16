package pl.edu.agh.dbclient.objects.operations;

import pl.edu.agh.dbclient.UserSession;

/**
 * @author Marcin Nowak
 */
public class OperationRequest {

	private UserSession session;
	private Operation operation;

	public UserSession getSession() {
		return session;
	}

	public Operation getOperation() {
		return operation;
	}
}
