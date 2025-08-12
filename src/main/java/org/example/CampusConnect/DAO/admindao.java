package org.example.CampusConnect.DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import io.github.cdimascio.dotenv.Dotenv;
import org.bson.*;
import org.bson.conversions.Bson;
import org.example.CampusConnect.DBConnection;

public class admindao {
    private final MongoCollection<Document> collection;

    public admindao() {
        Dotenv dotenv = Dotenv.load();
        String dbname = dotenv.get("DB_NAME");
        String collectionName = dotenv.get("ADMIN_COLLECTION");
        this.collection = DBConnection.createConnection(dbname, collectionName);
    }

    public Document loginAdmin(String email, String password) {
        Bson filter = Filters.and(
                Filters.eq("email", email),
                Filters.eq("password", password)
        );

        return collection.find(filter).first();
    }
}