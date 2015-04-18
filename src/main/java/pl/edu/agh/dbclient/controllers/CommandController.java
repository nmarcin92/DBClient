package pl.edu.agh.dbclient.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.dbclient.WebAppConstants;
import pl.edu.agh.dbclient.components.OperationService;
import pl.edu.agh.dbclient.exceptions.DBClientException;
import pl.edu.agh.dbclient.objects.QueryResult;
import pl.edu.agh.dbclient.objects.operations.CommandOperation;

/**
 * @author mnowak
 */

@RestController
@RequestMapping(value = WebAppConstants.COMMAND_RESOURCE_PATH)
public class CommandController {

    private static Logger LOGGER = Logger.getLogger(CommandController.class);

    @Autowired
    private OperationService operationService;

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    QueryResult executeCommandOperation(@RequestBody CommandOperation operation) throws DBClientException {
        return operationService.executeCommand(operation);
    }

}
