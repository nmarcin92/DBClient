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
import pl.edu.agh.dbclient.objects.EntityRow;
import pl.edu.agh.dbclient.objects.UserSession;
import pl.edu.agh.dbclient.objects.operations.CreateOperation;
import pl.edu.agh.dbclient.objects.operations.Operation;
import pl.edu.agh.dbclient.objects.operations.ReadOperation;
import pl.edu.agh.dbclient.objects.operations.UpdateOperation;

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
    public void testCreateAndReadRow() throws Exception {
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
    }

    @Test
    public void testUpdate() throws Exception {
        UpdateOperation op = new UpdateOperation(Operation.OperationContext.RECORD, "clients");
        op.setUserSession(userSession);
        op.setId("123");
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

}
