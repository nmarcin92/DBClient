package pl.edu.agh.dbclient.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.dbclient.WebAppConstants;
import pl.edu.agh.dbclient.components.OperationService;
import pl.edu.agh.dbclient.exceptions.DBClientException;
import pl.edu.agh.dbclient.objects.QueryResult;
import pl.edu.agh.dbclient.objects.operations.CreateOperation;
import pl.edu.agh.dbclient.objects.operations.DeleteOperation;

/**
 * @author mnowak
 */
@RestController
@RequestMapping(value = WebAppConstants.DELETE_RESOURCE_PATH)
public class DeleteController extends HandlingController {

    private static Logger LOGGER = Logger.getLogger(DeleteController.class);

    @Autowired
    private OperationService operationService;

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody QueryResult executeDeleteOperation(@RequestBody DeleteOperation operation) throws DBClientException {
        return operationService.executeDelete(operation);
    }
}
