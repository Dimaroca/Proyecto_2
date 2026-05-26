package app;
import models.Restaurant;
import models.User;
import services.RestaurantService;

import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {

        String uri = "neo4j://127.0.0.1:7687";

        String user = "neo4j";
        String password = "12345678";

        try (Neo4jManager neo4j = new Neo4jManager(uri, user, password)) {

           
            if (neo4j.testConnection()) {

                neo4j.loadDataset();

                System.out.println("OK");
            }
            else {
                System.out.println("Error de conexión");
            }
        }
    }
}