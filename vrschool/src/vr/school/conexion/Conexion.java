package vr.school.conexion;


import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
/**
 * Java basic MongoDB connection.
 * Conexión básica en Java a MongoDB.
 * @author xules
 */
public class Conexion {
    /**
     * Testing Java basic Mongodb connection.
     * Probando la conexión básica en Java a Mongodb.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
     try{   
        MongoClient mongoClient = new MongoClient( "oasdnraksef" , 27017 );
      
        MongoDatabase mongoDatabase = (MongoDatabase) mongoClient.getDatabase("lkjrnsfkdng");  
      System.out.println("exito rotundo");
       
     }catch(Exception e){
       System.err.println( e.getClass().getName() + ":no funciona tolai " + e.getMessage() );
    }}    
   
}
