package com.edu.agh.iosr;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class Demo {

	public static void main(String[] args) {
		try {
			//connect
			System.out.println("Connecting to localhost:27017");
			MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
			
			//show dbs
			System.out.println("Existing databases:");
			List<String> dbNames = mongoClient.getDatabaseNames();
			for(String dbName : dbNames){
				System.out.println("\t" + dbName);
			}
			
			//use iosr
			System.out.println("Connecting to iosr");
			DB db = mongoClient.getDB("iosr");
			
			//show collections
			System.out.println("Exiting collections:");
			Set<String> collectionNames = db.getCollectionNames();
			for(String collectionName : collectionNames){
				System.out.println("\t" + collectionName);
			}
			
			//db.people
			System.out.println("Selecting collection: people");
			DBCollection collection = db.getCollection("people");
			
			//db.people.find()
			System.out.println("Listing documents:");
			DBCursor cursor = collection.find();
			while(cursor.hasNext()){
				System.out.println("\t" + cursor.next());
			}
			
			//db.people.insert({...})
			System.out.println("Inserting new document:");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", "Mike");
			map.put("age", 30);
			BasicDBObject document = new BasicDBObject(map);
			collection.insert(document);
			System.out.println("\t" + document);
			
			//db.people.find({age : {$gt : 25}}
			System.out.println("Finding people older than 25 years:");
			BasicDBObject query = new BasicDBObject("age", new BasicDBObject("$gt", 25));
			cursor = collection.find(query);
			while(cursor.hasNext()){
				System.out.println("\t" + cursor.next());
			}
			
			//db.people.remove({"name" : "Mike"})
			System.out.println("Removing Mike from people collection:");
			BasicDBObject removeQuery = new BasicDBObject("name", "Mike");
			collection.remove(removeQuery);
			cursor = collection.find();
			while(cursor.hasNext()){
				System.out.println("\t" + cursor.next());
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}

}
