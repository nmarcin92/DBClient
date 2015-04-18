package pl.edu.agh.dbclient.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.dbclient.WebAppConstants;
import pl.edu.agh.dbclient.components.OperationService;
import pl.edu.agh.dbclient.exceptions.DBClientException;
import pl.edu.agh.dbclient.objects.QueryResult;
import pl.edu.agh.dbclient.objects.operations.UpdateOperation;

/**
 * @author mnowak
 */
@RestController
@RequestMapping(value = WebAppConstants.UPDATE_RESOURCE_PATH)
public class UpdateController extends HandlingController {

    private static Logger LOGGER = Logger.getLogger(UpdateController.class);

    @Autowired
    private OperationService operationService;

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody QueryResult executeUpdateOperation(@RequestBody UpdateOperation operation) throws DBClientException {
        return operationService.executeUpdate(operation);
    }

}
