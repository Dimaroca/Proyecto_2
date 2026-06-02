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
    private final String databaseName = "neo4j";

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

    public User registerUser(User user) {
        try (Session session = session()) {
            var exists = session.run("MATCH (u:Usuario {email: $email}) RETURN u", Values.parameters("email", user.getEmail()));
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

            if (node.containsKey("reviewPreference"))
                user.setReviewPreference(node.get("reviewPreference").asInt());
            return user;
        }
    }

    public boolean savePreferences(String userId, String favoriteFood, String budget, String environment, double minRating, String distance) {
        try (Session session = session()) {
            session.run("""
                MATCH (u:Usuario {id: $id})
                SET u.favoriteFood = $food,
                    u.budget       = $budget,
                    u.environment  = $env,
                    u.minRating    = $rating,
                    u.distance     = $distance
                """,
                Values.parameters(
                    "id",       userId,
                    "food",     favoriteFood,
                    "budget",   budget,
                    "env",      environment,
                    "rating",   minRating,
                    "distance", distance
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
            while (result.hasNext()) list.add(mapRestaurant(result.next()));
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
            while (result.hasNext()) list.add(mapRestaurant(result.next()));
            return list;
        }
    }

    public void loadDataset() {
        try (Session session = session()) {
            session.run("MATCH (n) DETACH DELETE n");
            session.run("""
                LOAD CSV WITH HEADERS
                FROM 'file:///restaurants.csv' AS row
                WITH row LIMIT 300
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

    public boolean saveReviewPreference(String userId, int rating) {
        try (Session session = session()) {
            session.run(
                "MATCH (u:Usuario {id:$id}) SET u.reviewPreference = $rating",
                Values.parameters("id", userId, "rating", rating)
            );
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Session openSession() {
        return driver.session(SessionConfig.forDatabase(databaseName));
    }

    private Session session() {
        return openSession();
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
            if (!result.hasNext()) return null;
            return mapRestaurant(result.single());
        }
    }

    public List<String> getCities() {
        try (Session session = session()) {
            var result = session.run("""
                MATCH (r:Restaurant)
                RETURN DISTINCT r.ciudad AS ciudad
                ORDER BY ciudad
                """);
            List<String> cities = new ArrayList<>();
            while (result.hasNext()) cities.add(result.next().get("ciudad").asString());
            return cities;
        }
    }

    public User getUserById(String userId) {
        try (Session session = openSession()) {
            var result = session.run(
                "MATCH (u:Usuario {id: $id}) RETURN u",
                Values.parameters("id", userId)
            );
            if (!result.hasNext()) return null;

            var node = result.single().get("u").asNode();
            User user = new User(
                node.get("nombre").asString(""),
                node.get("email").asString(""),
                node.get("password").asString(""),
                node.get("ciudad").asString("")
            );
            user.setId(node.get("id").asString(""));
            if (node.containsKey("favoriteFood"))   user.setFavoriteFood(node.get("favoriteFood").asString(""));
            if (node.containsKey("budget"))          user.setBudget(node.get("budget").asString(""));
            if (node.containsKey("environment"))     user.setEnvironment(node.get("environment").asString(""));
            if (node.containsKey("minRating"))       user.setMinRating(node.get("minRating").asDouble(0));
            if (node.containsKey("distance"))        user.setDistance(node.get("distance").asString(""));
            if (node.containsKey("reviewPreference")) user.setReviewPreference(node.get("reviewPreference").asInt(0));
            return user;
        } catch (Exception e) {
            System.out.println("Error buscando usuario: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteUser(String userId) {
        try (Session session = openSession()) {
            session.run("MATCH (u:Usuario {id: $id}) DETACH DELETE u", Values.parameters("id", userId));
            return true;
        } catch (Exception e) {
            System.out.println("Error eliminando usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean createRestaurant(models.Restaurant r) {
        try (Session session = openSession()) {
            session.run("""
                CREATE (r:Restaurant {
                    id:          $id,
                    nombre:      $nombre,
                    cocina:      $cocina,
                    rating:      $rating,
                    precio:      $precio,
                    zona:        $zona,
                    ciudad:      $ciudad,
                    imagen:      $imagen,
                    descripcion: $descripcion,
                    ambiente:    $ambiente
                })
                """,
                Values.parameters(
                    "id",          r.getId() != null ? r.getId() : UUID.randomUUID().toString(),
                    "nombre",      r.getName(),
                    "cocina",      r.getCategory(),
                    "rating",      r.getRating(),
                    "precio",      r.getPrice(),
                    "zona",        r.getZone(),
                    "ciudad",      r.getCity(),
                    "imagen",      r.getImage() != null ? r.getImage() : "",
                    "descripcion", r.getDescription() != null ? r.getDescription() : "",
                    "ambiente",    r.getEnvironment() != null ? r.getEnvironment() : ""
                )
            );
            return true;
        } catch (Exception e) {
            System.out.println("Error creando restaurante: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteRestaurant(String id) {
        try (Session session = openSession()) {
            session.run("MATCH (r:Restaurant {id: $id}) DETACH DELETE r", Values.parameters("id", id));
            return true;
        } catch (Exception e) {
            System.out.println("Error eliminando restaurante: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void close() {
        driver.close();
    }
}
