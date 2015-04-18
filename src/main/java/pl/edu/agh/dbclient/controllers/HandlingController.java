package pl.edu.agh.dbclient.controllers;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.edu.agh.dbclient.exceptions.DBClientException;
import pl.edu.agh.dbclient.objects.QueryResult;

/**
 * @author mnowak
 */
public class HandlingController {

    @ExceptionHandler(DBClientException.class)
    public @ResponseBody QueryResult handleError(DBClientException exception) {
        QueryResult errorResult = new QueryResult();
        errorResult.setSuccess(false);
        errorResult.addError(exception.getMessage());
        return errorResult;
    }

}
