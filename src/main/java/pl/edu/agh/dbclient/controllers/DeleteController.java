package pl.edu.agh.dbclient.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.dbclient.components.CreateService;
import pl.edu.agh.dbclient.components.DeleteService;

/**
 * @author mnowak
 */
@RestController
@RequestMapping(value = "/delete")
public class DeleteController {

    private static Logger LOGGER = Logger.getLogger(DeleteController.class);

    @Autowired
    private DeleteService deleteService;

}
