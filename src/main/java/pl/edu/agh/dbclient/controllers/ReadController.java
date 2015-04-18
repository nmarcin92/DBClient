package pl.edu.agh.dbclient.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.dbclient.components.ReadService;
import pl.edu.agh.dbclient.connections.DBConnection;
import pl.edu.agh.dbclient.connections.DBConnectionFactory;
import pl.edu.agh.dbclient.objects.QueryResult;
import pl.edu.agh.dbclient.objects.operations.OperationRequest;

/**
 * @author mnowak
 */

@RestController
@RequestMapping(value = "/read")
class ReadController {

    private static Logger LOGGER = Logger.getLogger(ReadController.class);

    @Autowired
    private ReadService readService;


    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    QueryResult executeCreateOperation(@RequestBody OperationRequest operationRequest) {
        try {
            DBConnection connection = DBConnectionFactory.getOrCreateConnection(operationRequest.getSession());
            return readService.executeRead(connection, operationRequest.getOperation());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        QueryResult errorResult = new QueryResult();
        errorResult.setSuccess(false);
        return errorResult;

    }

}
