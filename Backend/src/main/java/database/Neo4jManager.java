package database;
import java.util.ArrayList;
import java.util.List;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

import models.Restaurant;

/**
 * Handles the connection between Java and Neo4j.
 * Also manages dataset loading and graph generation.
 */
public class Neo4jManager implements AutoCloseable {

    /**
     * Neo4j driver instance.
     */
    private final Driver driver;

    /**
     * Name of the Neo4j database.
     */
    private final String databaseName = "restaurants";

    /**
     * Creates a Neo4jManager object using
     * the provided connection credentials.
     *
     * @param uri Neo4j connection URI.
     * @param user Database username.
     * @param password Database password.
     */
    public Neo4jManager(String uri, String user, String password) {

        driver = GraphDatabase.driver(
                uri,
                AuthTokens.basic(user, password)
        );
    }

    /**
     * Tests if the connection to Neo4j works correctly.
     *
     * @return true if the connection succeeds,
     * false otherwise.
     */
    public boolean testConnection() {

        try (Session session =
                     driver.session(
                             SessionConfig.forDatabase(databaseName))) {

            String result = session.run(
                            "RETURN 'OK' AS msg")
                    .single()
                    .get("msg")
                    .asString();

            return result.equals("OK");

        } catch (Exception e) {

            System.out.println(
                    "Error de conexión: "
                            + e.getMessage());

            return false;
        }
    }

    /**
     * Loads restaurant data from the CSV dataset
     * and creates graph nodes and weighted relationships.
     *
     * Only the first 20 rows are loaded.
     */
    public void loadDataset() {

        try (Session session =
                     driver.session(
                             SessionConfig.forDatabase(databaseName))) {

            /*
             * Deletes all existing nodes and relationships.
             */
            session.run("""
                MATCH (n)
                DETACH DELETE n
            """);

            /*
             * Loads restaurant data from the CSV file.
             */
            session.run("""
                LOAD CSV WITH HEADERS
                FROM 'file:///restaurant.csv' AS row

                WITH row LIMIT 20

                CREATE (r:Restaurant {
                    id: row.`Restaurant ID`,
                    nombre: row.`Restaurant Name`,
                    ciudad: row.City,
                    cocina: row.Cuisines,
                    rating: toFloat(row.`Aggregate rating`)
                })
            """);

            /*
             * Creates a sample user node and
             * weighted recommendation relationships.
             */
            session.run("""
                MERGE (u:Usuario {nombre:'Mateo'})

                WITH u

                MATCH (r:Restaurant)

                CREATE (u)-[:CALIFICO {
                    peso: r.rating
                }]->(r)
            """);
        }
    }

    public List<Restaurant> getRestaurants() {

        List<Restaurant> restaurants = new ArrayList<>();

        try (Session session =
                driver.session(
                SessionConfig.forDatabase(databaseName))) {

            var result = session.run("""

                MATCH (r:Restaurant)

                RETURN
                r.id AS id,
                r.nombre AS name,
                r.cocina AS category,
                r.rating AS rating,
                r.ciudad AS city

            """);

            while(result.hasNext()) {

                var row = result.next();

                restaurants.add(
                    new Restaurant(
                        row.get("id").asString(),
                        row.get("name").asString(),
                        row.get("category").asString(),
                        row.get("rating").asDouble(),
                        row.get("city").asString(),
                        "",
                        0
                    )
                );
            }
        }

        return restaurants;
    }

    /**
     * Closes the Neo4j driver connection.
     */
    @Override
    public void close() {

        driver.close();
    }
}