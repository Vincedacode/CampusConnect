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

public class studentdao {
    private final MongoCollection<Document> collection;

    public studentdao() {
        Dotenv dotenv =Dotenv.load();
        String dbname = dotenv.get("DB_NAME");
        String collectionName = dotenv.get("STUDENT_COLLECTION");
        this.collection = DBConnection.createConnection(dbname,collectionName);
    }

    public void registerStudent(String fullName, int age, String email, String gender, String password, String department){
      try {
          Document studentDocument = new Document()
                  .append("Fullname", fullName)
                  .append("Age", age)
                  .append("Email", email)
                  .append("Gender", gender)
                  .append("Password", password)
                  .append("Department", department)
                  .append("Joined_Clubs", new ArrayList<>())
                  .append("Registered_Events", new ArrayList<>())
                  ;
          collection.insertOne(studentDocument);
          System.out.println("Student saved successfully!");
      }catch (Exception e){
          System.out.println(e.getMessage());
      }
    }

    public boolean checkStudentEmail(String email) {
        try {
            Bson emailFilter = Filters.eq("Email", email);
            Document result = collection.find(emailFilter).first();

            return result != null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Document loginStudent(String email, String password) {
      try {
          Bson filter = Filters.and(
                  Filters.eq("Email", email),
                  Filters.eq("Password", password)
          );

          return collection.find(filter).first();
      }catch (Exception e){
          System.out.println(e.getMessage());
          return null;
      }
    }

    public List<Document> getAllStudents(){
        List<Document> students = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                students.add(cursor.next());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return students;
    }

    public void addClubToStudent(String studentFullName, String clubName) {

        collection.updateOne(
                eq("Fullname", studentFullName),
                Updates.addToSet("Joined_Clubs", clubName)
        );
    }

    public void addEventToStudent(String studentFullName, String eventTitle){
        collection.updateOne(
                eq("Fullname",studentFullName),
                Updates.addToSet("Registered_Events",eventTitle)
        );
    }




}
