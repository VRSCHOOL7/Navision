package vr.school.conexion;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class conexion3 {


	
	    public static void main(String[] args) {        
	        MongoClient mongoClient = new MongoClient();
	        MongoDatabase db = mongoClient.getDatabase("test");
	    }    
	}
