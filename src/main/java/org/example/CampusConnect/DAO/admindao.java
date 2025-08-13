package org.example.CampusConnect.DAO;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.*;
import org.bson.conversions.Bson;
import org.example.CampusConnect.DBConnection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;

public class admindao {
    private final MongoCollection<Document> collection;
    private final MongoCollection<Document> clubCollection;

    private final MongoCollection<Document> studentCollection;
    private final MongoCollection<Document> eventCollection;

    public admindao() {
        Dotenv dotenv = Dotenv.load();
        String dbname = dotenv.get("DB_NAME");
        String collectionName = dotenv.get("ADMIN_COLLECTION");
        String clubCollectionName = dotenv.get("CLUB_COLLECTION");
        String studentCollectionName = dotenv.get("STUDENT_COLLECTION");
        String eventCollectionName = dotenv.get("EVENT_COLLECTION");
        this.collection = DBConnection.createConnection(dbname, collectionName);
        this.clubCollection = DBConnection.createConnection(dbname,clubCollectionName);
        this.studentCollection = DBConnection.createConnection(dbname,studentCollectionName);
        this.eventCollection = DBConnection.createConnection(dbname,eventCollectionName);
    }

    public Document loginAdmin(String email, String password) {
        Bson filter = Filters.and(
                Filters.eq("email", email),
                Filters.eq("password", password)
        );

        return collection.find(filter).first();
    }

    public long getTotalClubs() {
        return clubCollection.countDocuments();
    }

    public long getTotalEvents() {
        return eventCollection.countDocuments();
    }

    public long getTotalStudents(){
        return studentCollection.countDocuments();
    }



    public List<Document> getMembersPerClub() {
        AggregateIterable<Document> result = clubCollection.aggregate(Arrays.asList(
                project(new Document("Club_name", 1)
                        .append("memberCount", new Document("$size",
                                new Document("$ifNull", Arrays.asList("$Members", new ArrayList<>())))))
        ));
        return result.into(new java.util.ArrayList<>());
    }


    public List<Document> getEventsPerMonth() {
        AggregateIterable<Document> result = eventCollection.aggregate(Arrays.asList(
                project(new Document("month", new Document("$month", "$Date"))),
                group("$month", sum("count", 1)),
                sort(new Document("_id", 1))
        ));
        return result.into(new java.util.ArrayList<>());
    }
}