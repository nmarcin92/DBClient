package pl.edu.agh.dbclient.connections.strategies;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.edu.agh.dbclient.WebAppConstants;
import pl.edu.agh.dbclient.config.root.RootContextConfig;
import pl.edu.agh.dbclient.config.servlet.ServletContextConfig;
import pl.edu.agh.dbclient.connections.DBConnectionType;
import pl.edu.agh.dbclient.connections.DBCredentials;
import pl.edu.agh.dbclient.objects.EntityAttribute;
import pl.edu.agh.dbclient.objects.EntityRow;
import pl.edu.agh.dbclient.objects.UserSession;
import pl.edu.agh.dbclient.objects.operations.*;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author mnowak
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {ServletContextConfig.class, RootContextConfig.class}, loader = AnnotationConfigWebContextLoader.class)
public class SQLConnectionTest {

    @Autowired
    private WebApplicationContext wac;

    private DBCredentials dbCredentials;
    private UserSession userSession;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        dbCredentials = new DBCredentials("postgres", "postgres", "localhost:5432", "postgres");
        userSession = new UserSession(DBConnectionType.POSTGRESQL, dbCredentials);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateReadAndDeleteRow() throws Exception {
        CreateOperation operation = new CreateOperation(Operation.OperationContext.RECORD, "clients");
        operation.addAttribute("name", "John");
        operation.addAttribute("age", "20");
        operation.addAttribute("id", "123");
        operation.setUserSession(userSession);

        mockMvc.perform(post(WebAppConstants.CREATE_RESOURCE_PATH)
                .content(objectMapper.writeValueAsString(operation))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.['success']").value(true));

        ReadOperation read = new ReadOperation(Operation.OperationContext.RECORD, "clients");
        read.setUserSession(userSession);

        mockMvc.perform(post(WebAppConstants.READ_RESOURCE_PATH)
                .content(objectMapper.writeValueAsString(read))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.['success']").value(true));


        DeleteOperation op = new DeleteOperation(Operation.OperationContext.RECORD, "clients");
        op.setUserSession(userSession);
        op.setPreconditions(Arrays.asList("id='123'"));

        mockMvc.perform(post(WebAppConstants.DELETE_RESOURCE_PATH)
                .content(objectMapper.writeValueAsString(op))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.['success']").value(true));
    }

    @Test
    public void testUpdate() throws Exception {
        UpdateOperation op = new UpdateOperation(Operation.OperationContext.RECORD, "clients");
        op.setUserSession(userSession);
        op.setPreconditions(Arrays.asList("id='123'"));
        EntityRow row = new EntityRow();
        row.getAttributes().put("name", "Rob");
        op.setUpdated(row);

        mockMvc.perform(post(WebAppConstants.UPDATE_RESOURCE_PATH)
                .content(objectMapper.writeValueAsString(op))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.['success']").value(true));
    }

    @Test
    public void testCreateReadAndDeleteTable() throws Exception {
        CreateOperation operation = new CreateOperation(Operation.OperationContext.ENTITY, "customers");
        operation.setUserSession(userSession);

        mockMvc.perform(post(WebAppConstants.CREATE_RESOURCE_PATH)
                .content(objectMapper.writeValueAsString(operation))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.['success']").value(true));

        ReadOperation read = new ReadOperation(Operation.OperationContext.ENTITY, "customers");
        read.setUserSession(userSession);

        mockMvc.perform(post(WebAppConstants.READ_RESOURCE_PATH)
                .content(objectMapper.writeValueAsString(read))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.['success']").value(true));

        UpdateOperation upOp = new UpdateOperation(Operation.OperationContext.ENTITY, "customers");
        upOp.setUserSession(userSession);
        upOp.getToAdd().add(new EntityAttribute("lastname", "varchar(20)"));

        mockMvc.perform(post(WebAppConstants.UPDATE_RESOURCE_PATH)
                .content(objectMapper.writeValueAsString(upOp))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.['success']").value(true));

        UpdateOperation upOp2 = new UpdateOperation(Operation.OperationContext.ENTITY, "customers");
        upOp2.setUserSession(userSession);
        upOp2.getToRename().add(new AttributeRename("lastname", "surname"));

        mockMvc.perform(post(WebAppConstants.UPDATE_RESOURCE_PATH)
                .content(objectMapper.writeValueAsString(upOp2))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.['success']").value(true));

        UpdateOperation upOp3 = new UpdateOperation(Operation.OperationContext.ENTITY, "customers");
        upOp3.setUserSession(userSession);
        upOp3.getToDelete().add(new EntityAttribute("surname"));

        mockMvc.perform(post(WebAppConstants.UPDATE_RESOURCE_PATH)
                .content(objectMapper.writeValueAsString(upOp3))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.['success']").value(true));


        DeleteOperation op = new DeleteOperation(Operation.OperationContext.ENTITY, "customers");
        op.setUserSession(userSession);

        mockMvc.perform(post(WebAppConstants.DELETE_RESOURCE_PATH)
                .content(objectMapper.writeValueAsString(op))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.['success']").value(true));
    }

    @Test
    public void testReadSchema() throws Exception {
        ReadOperation read = new ReadOperation(Operation.OperationContext.DATABASE, "");
        read.setUserSession(userSession);

        mockMvc.perform(post(WebAppConstants.READ_RESOURCE_PATH)
                .content(objectMapper.writeValueAsString(read))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.['success']").value(true));


    }

}
