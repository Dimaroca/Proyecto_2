package dataset;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;

public class Neo4jManager implements AutoCloseable {

    private final Driver driver;
    private final String databaseName = "restaurants";

    public Neo4jManager(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public boolean testConnection() {
        try (Session session = driver.session(SessionConfig.forDatabase(databaseName))) {

            String result = session.run("RETURN 'OK' AS msg")
                    .single()
                    .get("msg")
                    .asString();

            return result.equals("OK");

        } catch (Exception e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return false;
        }
    }

    public void loadDataset() {
        try (Session session = driver.session(SessionConfig.forDatabase(databaseName))) {

            session.run("""
                MATCH (n)
                DETACH DELETE n
            """);

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

    @Override
    public void close() {
        driver.close();
    }
}