package app.database;

import  com.mongodb.client.model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.github.cdimascio.dotenv.Dotenv;

public class MongoDBConnection {
	private final static Dotenv DOTENV = Dotenv.load();
	private static MongoClientURI uri = new MongoClientURI(DOTENV.get("DATABASE_URL"));
	private static MongoClient mongoClient = new MongoClient(uri);
	private static MongoDatabase bbdd = mongoClient.getDatabase("VRSCHOOL7");
	
	
	public static void main(String[] args) {
		
		MongoDBConnection.getAll("Users");
	}
	
	public static ArrayList<Document> getAll(String collectionName) {	
		try{
			MongoCollection<Document> collection = bbdd.getCollection(collectionName);
			FindIterable<Document> query = collection.find();
			ArrayList<Document> result = new ArrayList<Document>();
			for (Document doc : query) {
				result.add(doc);
 			};
			return result;
		}catch (Exception e) {
			System.out.println(e);
		}
		return null;
		
	}
	
	public static void insert(String collectionName, Document doc) {

		try {
			MongoCollection<Document> collection = bbdd.getCollection(collectionName);
			collection.insertOne(doc);
			System.out.println("Insertado correctamente");
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void delete(String collectionName, String ID) {

		try {
			MongoCollection<Document> collection = bbdd.getCollection(collectionName);
			collection.deleteOne(new Document("_id", new ObjectId(ID)));
			System.out.println(ID + " eliminado correctamente");
		}catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void insertCourse(String title, String description) {
        JSONParser jsonParser = new JSONParser();
        
        try (FileReader reader = new FileReader("src" + File.separator + "app" + File.separator + "database" + File.separator + "course_data.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
 
            JSONObject newCours = (JSONObject) obj;
            newCours.put("title", title);
            newCours.put("description", description);
            MongoDBConnection.insert("Courses", new Document().parse(newCours.toString()));
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    
	}
	
	public static ArrayList<Document> getNotAsignedUsers(ArrayList<Integer> usersId) {
    		MongoCollection<Document> collection = bbdd.getCollection("Users");
    		Bson firstFilter = Filters.nin("id", usersId);
    		FindIterable<Document> query = collection.find(firstFilter);
    		ArrayList<Document> result = new ArrayList<Document>();
    		for (Document doc : query) {
    			result.add(doc);
    		};
    		return result;
	}
	
	public static ArrayList<Integer> getTeacher(String courseId) {
		ArrayList<Integer> result = getSubscribers(courseId, "teachers");
		return result;
	}
	
	public static ArrayList<Integer> getStudents(String courseId) {
		ArrayList<Integer> result = getSubscribers(courseId, "students");
		return result;
	}
	
	private static ArrayList<Integer> getSubscribers(String courseId, String name) {
		MongoCollection<Document> collection = bbdd.getCollection("Courses");
		Bson firstFilter = Filters.eq("_id", new ObjectId( courseId));
		Bson projection = Projections.fields(Projections.include("subscribers."+name));
		FindIterable<Document> query = collection.find(firstFilter).projection(projection);
		ArrayList<Integer> result = new ArrayList<Integer>();
		result = query.first().get("subscribers", Document.class).get(name, result.getClass());
		return result;
	}
	 
	public static ArrayList<Document> getUserDetails(ArrayList<Integer> usersId) {
		MongoCollection<Document> collection = bbdd.getCollection("Users");
		Bson firstFilter = Filters.in("id", usersId);
		FindIterable<Document> query = collection.find(firstFilter);
		ArrayList<Document> result = new ArrayList<Document>();
		for (Document doc : query) {
			result.add(doc);
		};
		return result;
	}
	
	public static void insertUserToCourse(String listName, String courseId, int userId) {
		MongoCollection<Document> collection = bbdd.getCollection("Courses");
		Bson query = new Document().append("_id", new ObjectId(courseId));
		Bson fields = new Document().append("subscribers." + listName, userId);
		Bson update = new Document("$push",fields);
		System.out.println( "Usuario con ID: " + userId + " insertado correctamente a la array " + listName.substring(0).toUpperCase() +" al curso con ID:" + courseId);
		collection.updateOne( query, update );
	}

	public static void deleteUserFromCourse(String listName, String courseId, int userId) {
		MongoCollection<Document> collection = bbdd.getCollection("Courses");
		Bson query = new Document().append("_id", new ObjectId(courseId));
		Bson fields = new Document().append("subscribers."+listName, new Document().append( "$eq", userId));
		Bson update = new Document("$pull",fields);
		System.out.println(listName.substring(0).toUpperCase() + " con ID: " + userId + " eliminado correctamente de curso con ID:" + courseId );
		collection.updateOne( query, update );
	}
	
}
