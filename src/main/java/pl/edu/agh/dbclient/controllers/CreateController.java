package pl.edu.agh.dbclient.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pl.edu.agh.dbclient.components.CreateService;
import pl.edu.agh.dbclient.connections.DBConnection;
import pl.edu.agh.dbclient.connections.DBConnectionFactory;
import pl.edu.agh.dbclient.objects.QueryResult;
import pl.edu.agh.dbclient.objects.operations.OperationRequest;

/**
 * @author mnowak
 */

@RestController
@RequestMapping(value = "/create")
class CreateController {

	@Autowired
	private CreateService createService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public int getSomeInt() {
		return createService.someMethod();
	}

	@RequestMapping(method = RequestMethod.POST)
	public @ResponseBody QueryResult executeCreateOperation(@RequestBody OperationRequest operationRequest) {
		try {
			DBConnection connection = DBConnectionFactory.getOrCreateConnection(operationRequest.getSession());
			return createService.executeCreate(connection, operationRequest);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
