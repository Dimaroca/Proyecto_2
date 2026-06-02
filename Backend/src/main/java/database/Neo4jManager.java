package database;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.Values;

import models.Restaurant;
import models.User;


public class Neo4jManager implements AutoCloseable {

    private final Driver driver;
    private final String databaseName = "restaurants";

    public Neo4jManager(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public boolean testConnection() {
        try (Session session = session()) {
            String result = session.run("RETURN 'OK' AS msg").single().get("msg").asString();
            return result.equals("OK");
        } catch (Exception e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return false;
        }
    }

    //  Usuarios


    public User registerUser(User user) {
        try (Session session = session()) {
            // Verificar si ya existe
            var exists = session.run("MATCH (u:Usuario {email: $email}) RETURN u", Values.parameters("email", user.getEmail()) );
            if (exists.hasNext()) return null;

            String newId = UUID.randomUUID().toString();
            session.run("""
                CREATE (u:Usuario {
                    id:       $id,
                    nombre:   $name,
                    email:    $email,
                    password: $password,
                    ciudad:   $city
                })
                """,
                Values.parameters(
                    "id",       newId,
                    "name",     user.getName(),
                    "email",    user.getEmail(),
                    "password", user.getPassword(),
                    "city",     user.getCity()
                )
            );
            user.setId(newId);
            return user;
        }
    }

    
    public User loginUser(String email, String password) {
        try (Session session = session()) {
            var result = session.run(
                "MATCH (u:Usuario {email: $email, password: $password}) RETURN u",
                Values.parameters("email", email, "password", password)
            );
            if (!result.hasNext()) return null;

            var node = result.single().get("u").asNode();
            User user = new User(node.get("nombre").asString(), node.get("email").asString(), node.get("password").asString(), node.get("ciudad").asString());
            user.setId(node.get("id").asString());
            return user;
        }
    }

    
    public boolean savePreferences(String userId, String favoriteFood,
                                   String budget, String environment) {
        try (Session session = session()) {
            session.run("""
                MATCH (u:Usuario {id: $id})
                SET u.favoriteFood = $food,
                    u.budget       = $budget,
                    u.environment  = $env
                """,
                Values.parameters(
                    "id",     userId,
                    "food",   favoriteFood,
                    "budget", budget,
                    "env",    environment
                )
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    
    public List<Restaurant> getAllRestaurants() {
        try (Session session = session()) {
            var result = session.run("MATCH (r:Restaurant) RETURN r");
            List<Restaurant> list = new ArrayList<>();
            while (result.hasNext()) {
                list.add(mapRestaurant(result.next()));
            }
            return list;
        }
    }

    
    public List<Restaurant> getRestaurantsByCity(String city) {
        try (Session session = session()) {
            var result = session.run(
                "MATCH (r:Restaurant {ciudad: $city}) RETURN r",
                Values.parameters("city", city)
            );
            List<Restaurant> list = new ArrayList<>();
            while (result.hasNext()) {
                list.add(mapRestaurant(result.next()));
            }
            return list;
        }
    }

    
    public void loadDataset() {
        try (Session session = session()) {
            session.run("MATCH (n) DETACH DELETE n");
            session.run("""
                LOAD CSV WITH HEADERS
                FROM 'file:///restaurant.csv' AS row
                WITH row LIMIT 20
                CREATE (r:Restaurant {
                    id:          row.`Restaurant ID`,
                    nombre:      row.`Restaurant Name`,
                    ciudad:      row.City,
                    cocina:      row.Cuisines,
                    rating:      toFloat(row.`Aggregate rating`),
                    precio:      toInteger(row.`Price range`),
                    zona:        row.Locality,
                    imagen:      '',
                    descripcion: '',
                    ambiente:    ''
                })
                """);
        }
    }


    private Session session() {
        return driver.session(SessionConfig.forDatabase(databaseName));
    }

    private Restaurant mapRestaurant(Record record) {
        var node = record.get("r").asNode();
        return new Restaurant(
            node.get("id").asString(""),
            node.get("nombre").asString(""),
            node.get("cocina").asString(""),
            node.get("rating").asDouble(0),
            node.get("precio").asInt(0),
            node.get("zona").asString(""),
            node.get("ciudad").asString(""),
            node.get("imagen").asString(""),
            node.get("descripcion").asString(""),
            node.get("ambiente").asString("")
        );
    }

    public Restaurant getRestaurantById(String id) {

        try (Session session = session()) {

            var result = session.run(
                "MATCH (r:Restaurant {id:$id}) RETURN r",
                Values.parameters("id", id)
            );

            if (!result.hasNext()) {
                return null;
            }

            return mapRestaurant(result.single());
        }
    }

    public List<String> getCities() {

        try (Session session = session()) {

            var result = session.run(
                """
                MATCH (r:Restaurant)
                RETURN DISTINCT r.ciudad AS ciudad
                ORDER BY ciudad
                """
            );

            List<String> cities = new ArrayList<>();

            while (result.hasNext()) {

                cities.add(
                    result.next()
                        .get("ciudad")
                        .asString()
                );
            }

            return cities;
        }
    }

    @Override
    public void close() {
        driver.close();
    }
}
