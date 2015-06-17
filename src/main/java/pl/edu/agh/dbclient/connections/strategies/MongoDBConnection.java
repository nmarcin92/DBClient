package pl.edu.agh.dbclient.connections.strategies;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.base.Splitter;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mongodb.*;
import org.apache.log4j.Logger;
import pl.edu.agh.dbclient.WebAppConstants;
import pl.edu.agh.dbclient.connections.DBConnection;
import pl.edu.agh.dbclient.connections.DBCredentials;
import pl.edu.agh.dbclient.exceptions.ConnectionInitializationException;
import pl.edu.agh.dbclient.exceptions.DBClientException;
import pl.edu.agh.dbclient.exceptions.DatabaseException;
import pl.edu.agh.dbclient.objects.Entity;
import pl.edu.agh.dbclient.objects.EntityAttribute;
import pl.edu.agh.dbclient.objects.EntityRow;
import pl.edu.agh.dbclient.objects.QueryResult;
import pl.edu.agh.dbclient.objects.operations.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author mnowak
 */
public class MongoDBConnection implements DBConnection {

    private static final Logger LOGGER = Logger.getLogger(MongoDBConnection.class);

    private MongoClient client;
    private DB db;
    private DBCredentials credentials;

    private void initializeConnection() throws ConnectionInitializationException {
        List<String> urlParts = Splitter.on(':').splitToList(credentials.getUrl());
        try {
            MongoCredential credential = MongoCredential.createCredential(credentials.getUsername(), credentials.getDatabaseName(), credentials.getPassword().toCharArray());
            client = new MongoClient(new ServerAddress(urlParts.get(0), Integer.parseInt(urlParts.get(1))), Arrays.asList(credential));
            db = client.getDB(credentials.getDatabaseName());
        } catch (UnknownHostException e) {
            LOGGER.error("Error during initialization", e);
            throw new ConnectionInitializationException(WebAppConstants.CONNECTION_INITIALIZATION_ERROR);
        }
    }


    @Override
    public void setCredentials(DBCredentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public QueryResult performCreate(CreateOperation operation) throws DBClientException {
        initializeConnection();
        switch (operation.getContext()) {
            case ENTITY:
                return createCollection(operation);
            case RECORD:
                return insertData(operation);
            default:
                LOGGER.error("Unsupported operation context: " + operation.getContext());
                throw new DatabaseException(WebAppConstants.UNSUPPORTED_OPERATION_CONTEXT_ERROR);
        }
    }

    private QueryResult insertData(CreateOperation operation) {
        QueryResult qr = new QueryResult();
        DBCollection col = db.getCollection(operation.getEntityName());
        BasicDBObject document = new BasicDBObject();
        for (Map.Entry<String, String> entry : operation.getAttributes().entrySet()) {
            document.put(entry.getKey(), entry.getValue());
        }
        col.insert(document);

        return qr;
    }

    private QueryResult createCollection(CreateOperation operation) throws DBClientException {
        try {
            QueryResult qr = new QueryResult();
            db.createCollection(operation.getEntityName(), new BasicDBObject());
            return qr;
        } catch (MongoException e) {
            LOGGER.error("Error during creating collection", e);
            throw new DBClientException(e.getMessage());
        }
    }

    @Override
    public QueryResult performRead(ReadOperation operation) throws DBClientException {
        initializeConnection();
        switch (operation.getContext()) {
            case DATABASE:
                return getAllCollections(operation);
            case RECORD:
                return readCollection(operation);
            default:
                LOGGER.error("Unsupported operation context: " + operation.getContext());
                throw new DatabaseException(WebAppConstants.UNSUPPORTED_OPERATION_CONTEXT_ERROR);
        }
    }

    private QueryResult getAllCollections(ReadOperation operation) {
        QueryResult qr = new QueryResult();
        Entity entity = new Entity("TABLES");

        for (String colName : db.getCollectionNames()) {
            entity.getAttributes().add(new EntityAttribute(colName));
        }

        qr.setEntity(entity);
        return qr;
    }

    private QueryResult readCollection(ReadOperation operation) throws DBClientException {
        try {
            DBCursor cursor = db.getCollection(operation.getEntityName()).find();
            Entity entity = new Entity(operation.getEntityName());
            Set<String> attributeNames = Sets.newHashSet();
            while (cursor.hasNext()) {
                DBObject obj = cursor.next();
                EntityRow row = new EntityRow();
                for (String key : obj.keySet()) {
                    attributeNames.add(key);
                    row.getAttributes().put(key, obj.get(key).toString());
                }
                entity.getRows().add(row);
            }
            entity.getAttributes().addAll(Collections2.transform(attributeNames, new Function<String, EntityAttribute>() {
                @Override
                public EntityAttribute apply(String input) {
                    return new EntityAttribute(input);
                }
            }));

            QueryResult qr = new QueryResult();
            qr.setEntity(entity);
            return qr;
        } catch (MongoException e) {
            LOGGER.error("Error during reading collection", e);
            throw new DBClientException(e.getMessage());
        }
    }

    @Override
    public QueryResult performUpdate(UpdateOperation operation) throws DBClientException {
        initializeConnection();
        switch (operation.getContext()) {
            case RECORD:
                return updateDocument(operation);
            default:
                LOGGER.error("Unsupported operation context: " + operation.getContext());
                throw new DatabaseException(WebAppConstants.UNSUPPORTED_OPERATION_CONTEXT_ERROR);
        }
    }

    private QueryResult updateDocument(UpdateOperation operation) throws DBClientException {
        DBCollection col = db.getCollection(operation.getEntityName());
        BasicDBObject updated = new BasicDBObject();
        for (Map.Entry<String, String> entry : operation.getUpdated().getAttributes().entrySet()) {
            updated.append(entry.getKey(), entry.getValue());
        }


        DBObject searchQuery = new BasicDBObject();
        List<String> parts;
        List<BasicDBObject> subqueries = Lists.newArrayList();
        for (String prec : operation.getPreconditions()) {
            if (prec.contains("=")) {
                parts = Splitter.on("=").splitToList(prec);
                subqueries.add(new BasicDBObject(parts.get(0), new BasicDBObject("$eq", parts.get(1))));
            } else if (prec.contains("<")) {
                parts = Splitter.on("<").splitToList(prec);
                subqueries.add(new BasicDBObject(parts.get(0), new BasicDBObject("$lt", parts.get(1))));
            } else if (prec.contains(">")) {
                parts = Splitter.on(">").splitToList(prec);
                subqueries.add(new BasicDBObject(parts.get(0), new BasicDBObject("$gt", parts.get(1))));
            } else if (prec.contains("<>")) {
                parts = Splitter.on("<>").splitToList(prec);
                subqueries.add(new BasicDBObject(parts.get(0), new BasicDBObject("$ne", parts.get(1))));
            } else {
                throw new DBClientException("Unsupported precondition");
            }
        }
        searchQuery.put("$and", subqueries);

        col.update(searchQuery, new BasicDBObject().append("$set", updated));
        return new QueryResult();
    }

    @Override
    public QueryResult performDelete(DeleteOperation operation) throws DBClientException {
        initializeConnection();
        switch (operation.getContext()) {
            case ENTITY:
                return deleteCollection(operation);
            case RECORD:
                return deleteData(operation);
            default:
                LOGGER.error("Unsupported operation context: " + operation.getContext());
                throw new DatabaseException(WebAppConstants.UNSUPPORTED_OPERATION_CONTEXT_ERROR);
        }
    }

    private QueryResult deleteData(DeleteOperation operation) throws DBClientException {
        try {
            DBCollection col = db.getCollection(operation.getEntityName());
            DBObject removeQuery = new BasicDBObject();
            List<String> parts;
            List<BasicDBObject> subqueries = Lists.newArrayList();
            for (String prec : operation.getPreconditions()) {
                if (prec == null) {
                    continue;
                }
                if (prec.contains("=")) {
                    parts = Splitter.on("=").splitToList(prec);
                    subqueries.add(new BasicDBObject(parts.get(0), new BasicDBObject("$eq", parts.get(1))));
                } else if (prec.contains("<")) {
                    parts = Splitter.on("<").splitToList(prec);
                    subqueries.add(new BasicDBObject(parts.get(0), new BasicDBObject("$lt", parts.get(1))));
                } else if (prec.contains(">")) {
                    parts = Splitter.on(">").splitToList(prec);
                    subqueries.add(new BasicDBObject(parts.get(0), new BasicDBObject("$gt", parts.get(1))));
                } else if (prec.contains("<>")) {
                    parts = Splitter.on("<>").splitToList(prec);
                    subqueries.add(new BasicDBObject(parts.get(0), new BasicDBObject("$ne", parts.get(1))));
                } else {
                    throw new DBClientException("Unsupported precondition");
                }
            }
            removeQuery.put("$and", subqueries);

            col.findAndRemove(removeQuery);
            return new QueryResult();

        } catch (MongoException e) {
            LOGGER.error("Error during deleting data", e);
            throw new DBClientException(e.getMessage());
        }

    }

    private QueryResult deleteCollection(DeleteOperation operation) throws DatabaseException {
        QueryResult qr = new QueryResult();
        try {
            db.getCollection(operation.getEntityName()).drop();
        } catch (MongoException e) {
            LOGGER.error("Error during collection drop", e);
            throw new DatabaseException(e.getMessage());
        }
        return qr;
    }

    @Override
    public QueryResult executeCommand(CommandOperation command) throws DBClientException {
        initializeConnection();
        QueryResult qr = new QueryResult();
        CommandResult res = db.command(command.getQuery());
        Entity entity = new Entity("result");
        try {
            String json = new ObjectMapper().writeValueAsString(res);
            Map<String, Object> map = new ObjectMapper().readValue(json, Map.class);
            EntityRow row = new EntityRow();

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                entity.getAttributes().add(new EntityAttribute(entry.getKey()));
                row.getAttributes().put(entry.getKey(), entry.getValue().toString());
            }
            entity.getRows().add(row);
            qr.setEntity(entity);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return qr;
    }
}
