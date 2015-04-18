package pl.edu.agh.dbclient.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.dbclient.components.ReadService;
import pl.edu.agh.dbclient.components.UpdateService;

/**
 * @author mnowak
 */
@RestController
@RequestMapping(value = "/update")
public class UpdateController {

    private static Logger LOGGER = Logger.getLogger(UpdateController.class);

    @Autowired
    private UpdateService updateService;

}
