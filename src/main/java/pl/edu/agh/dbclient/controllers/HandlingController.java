package pl.edu.agh.dbclient.controllers;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.edu.agh.dbclient.WebAppConstants;
import pl.edu.agh.dbclient.exceptions.DBClientException;
import pl.edu.agh.dbclient.objects.QueryResult;

/**
 * @author mnowak
 */
public class HandlingController {
    private static final Logger LOGGER = Logger.getLogger(HandlingController.class);

    @ExceptionHandler(DBClientException.class)
    public
    @ResponseBody
    QueryResult handleError(DBClientException exception) {
        LOGGER.error("Unknown error", exception);
        return QueryResult.createErrorResult(exception.getMessage());
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public
    @ResponseBody
    QueryResult handleError(Throwable t) {
        LOGGER.error("Unexpected exception", t);
        return QueryResult.createErrorResult(WebAppConstants.UNKNOWN_SERVER_ERROR);
    }

}
