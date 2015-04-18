package pl.edu.agh.dbclient;

/**
 * @author mnowak
 */
public class WebAppConstants {

    // Rest service resources
    public static final String CREATE_RESOURCE_PATH = "/create";
    public static final String READ_RESOURCE_PATH = "/read";
    public static final String UPDATE_RESOURCE_PATH = "/update";
    public static final String DELETE_RESOURCE_PATH = "/delete";
    public static final String COMMAND_RESOURCE_PATH = "/command";

    // Error messages
    public static final String CONNECTION_INITIALIZATION_ERROR = "Connection initialization failed. Contact with administrators.";
    public static final String BAD_CREDENTIALS_ERROR = "Invalid username/password or database URL.";
    public static final java.lang.String UNSUPPORTED_OPERATION_CONTEXT_ERROR = "Operation context is not supported.";

    private WebAppConstants(){}

}
