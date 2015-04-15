package pl.edu.agh.dbclient.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.dbclient.components.CreateService;

/**
 * @author mnowak
 */

@RestController
@RequestMapping(value="/create")
class CreateController {

    @Autowired
    private CreateService createService;

    @RequestMapping(method= RequestMethod.GET)
    @ResponseBody
    public int getSomeInt(){
        return createService.someMethod();
    }

}
