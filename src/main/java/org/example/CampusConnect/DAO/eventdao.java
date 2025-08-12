package org.example.CampusConnect.DAO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import com.mongodb.client.model.Updates;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.*;
import org.bson.conversions.Bson;
import org.example.CampusConnect.DBConnection;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;


public class eventdao {
    private static MongoCollection<Document> collection = null;
    public eventdao(){
        Dotenv dotenv = Dotenv.load();
        String dbname = dotenv.get("DB_NAME");
        String collectionName = dotenv.get("EVENT_COLLECTION");
        this.collection =   DBConnection.createConnection(dbname,collectionName);
    }

    public void registerEvent(String title, Date date, String time, String clubName) {
        try {
            Document eventDocument = new Document()
                    .append("Title", title)
                    .append("Date", date)
                    .append("Time", time)
                    .append("Club_Name", clubName)
                    .append("Attendees", new ArrayList<>())
                    .append("Status", "pending");

            collection.insertOne(eventDocument);
            System.out.println("Event registered successfully!");
        } catch (Exception e) {
            System.out.println("Error registering event: " + e.getMessage());
        }
    }


    public boolean checkEventTitle(String eventTitle){
       try {
           Bson titleFilter = eq("Title");
           Document result = collection.find(titleFilter).first();
           return result != null;
       }catch (Exception e){
           System.out.println(e.getMessage());
           return false;
       }
    }


    public void approveEvent(String eventTitle) {
        collection.updateOne(eq("Title", eventTitle), Updates.set("Status", "approved"));
    }

    public List<Document> getPendingEvents(){
        List<Document> pendingEvents  = new ArrayList<>();
        try {
            Bson pendingFilter = Filters.eq("Status","pending");
            MongoCursor<Document> cursor = collection.find(pendingFilter).iterator();
            while (cursor.hasNext()){
                pendingEvents.add(cursor.next());
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return pendingEvents;
    }

    public List<Document> getApprovedEvents(){
        List<Document> approvedEvents  = new ArrayList<>();
        try {
            Bson approvedFilter = Filters.eq("Status","approved");
            MongoCursor<Document> cursor = collection.find(approvedFilter).iterator();
            while (cursor.hasNext()){
                approvedEvents.add(cursor.next());
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return approvedEvents;
    }

    public void addStudentToAttendees(String eventTitle, String studentName){
        collection.updateOne(
                eq("Title", eventTitle),
                Updates.set("Attendees",studentName)
        );
    }
}
