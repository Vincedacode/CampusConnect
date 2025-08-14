package org.example.CampusConnect.DAO;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.*;
import org.bson.conversions.Bson;
import org.example.CampusConnect.DBConnection;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class clubdao {

    private final MongoCollection<Document> collection;
    public clubdao(){
        Dotenv dotenv = Dotenv.load();
        String dbname = dotenv.get("DB_NAME");
        String collectionName = dotenv.get("CLUB_COLLECTION");
        this.collection =   DBConnection.createConnection(dbname,collectionName);
    }

    public void insertClub(String clubName, String description, String adminName){
        try {
            Document clubDocument = new Document()
                    .append("Club_name", clubName)
                    .append("Description", description)
                    .append("Admin_name", adminName)
                    .append("Members", new ArrayList<>())
                    .append("Status","pending");
            collection.insertOne(clubDocument);
            System.out.println("Club saved successfully!");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public boolean checkClubName(String clubName){
        try {
            Bson nameFilter = eq("Club_name",clubName);
            Document result = collection.find(nameFilter).first();
            return result != null;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

    }

    public void approveClub(String clubName) {
        collection.updateOne(eq("Club_name", clubName), Updates.set("Status", "approved"));
    }

    public List<Document> getPendingClubs() {
        List<Document> pendingClubs = new ArrayList<>();
        try {
            Bson pendingFilter = eq("Status", "pending");
            MongoCursor<Document> cursor = collection.find(pendingFilter).iterator();
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                if (doc.containsKey("Club_name") && doc.containsKey("Admin_name")) {
                    pendingClubs.add(doc);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return pendingClubs;
    }

    public List<Document> getApprovedClubs() {
        List<Document> approvedClubs = new ArrayList<>();
        try {
            Bson approvedFilter = eq("Status", "approved");
            MongoCursor<Document> cursor = collection.find(approvedFilter).iterator();
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                if (doc.containsKey("Club_name") && doc.containsKey("Admin_name")) {
                    approvedClubs.add(doc);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return approvedClubs;
    }

    public List<Document> getClubsByStudent(String studentName) {
        List<Document> joinedClubs = new ArrayList<>();
        try {
            Bson filter = Filters.in("Members", studentName); // Checks if student is in Members array
            MongoCursor<Document> cursor = collection.find(filter).iterator();
            while (cursor.hasNext()) {
                joinedClubs.add(cursor.next());
            }
        } catch (Exception e) {
            System.out.println("Error fetching joined clubs: " + e.getMessage());
        }
        return joinedClubs;
    }


    public void addStudentToClub(String clubName, String studentName) {
        collection.updateOne(
                eq("Club_name", clubName),
                Updates.addToSet("Members", studentName)
        );
    }



}
