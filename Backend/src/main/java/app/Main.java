package app;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import database.Neo4jManager;
import models.Restaurant;
import models.User;
import services.RestaurantService;
import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.port;
import static spark.Spark.post;

public class Main {

    private static final Gson gson = new Gson();

    //Conexión Neo4j 
    private static final String NEO4J_URI = "bolt://neo4j:7687";
    private static final String NEO4J_USER = "neo4j";
    private static final String NEO4J_PASS = "12345678";

    public static void main(String[] args) {

        Neo4jManager neo4j = new Neo4jManager(NEO4J_URI, NEO4J_USER, NEO4J_PASS);
        //neo4j.loadDataset();
        System.out.println("Dataset cargado");
        RestaurantService service = new RestaurantService();

        // Servidor en puerto 4567
        port(4567);

        // CORS: permite peticiones desde el frontend 
        before((req, res) -> {
            res.header("Access-Control-Allow-Origin",  "*");
            res.header("Access-Control-Allow-Methods", "GET,POST,OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type,Authorization");
            res.type("application/json");
        });
        options("/*", (req, res) -> {
            res.status(200);
            return "OK";
        });

    
        
        post("/api/register", (req, res) -> {
            JsonObject body = gson.fromJson(req.body(), JsonObject.class);

            String name = body.get("name").getAsString();
            String email = body.get("email").getAsString();
            String password = body.get("password").getAsString();
            String city = body.get("city").getAsString();

            User newUser = neo4j.registerUser(new User(name, email, password, city));

            if (newUser == null) {
                res.status(409);
                return gson.toJson(Map.of("error", "El correo ya está registrado."));
            }

            return gson.toJson(Map.of("message", "Registro exitoso.", "userId", newUser.getId(), "name", newUser.getName() ));});

        post("/api/review", (req, res) -> {

            JsonObject body =
                gson.fromJson(
                    req.body(),
                    JsonObject.class
                );

            String userId =
                body.get("userId")
                    .getAsString();

            int rating =
                body.get("rating")
                    .getAsInt();

            boolean ok =
                neo4j.saveReviewPreference(
                    userId,
                    rating
                );

            if(!ok){

                res.status(500);

                return gson.toJson(
                    Map.of(
                        "error",
                        "No se pudo guardar"
                    )
                );
            }

            return gson.toJson(
                Map.of(
                    "message",
                    "Review guardada"
                )
            );
        });
                post("/api/login", (req, res) -> {
            JsonObject body = gson.fromJson(req.body(), JsonObject.class);

            String email = body.get("email").getAsString();
            String password = body.get("password").getAsString();

            User user = neo4j.loginUser(email, password);

            if (user == null) {
                res.status(401);
                return gson.toJson(Map.of("error", "Credenciales incorrectas."));
            }

            return gson.toJson(Map.of("message", "Login exitoso.", "userId",  user.getId(), "name",    user.getName(),"city",    user.getCity())); });

   
        post("/api/preferencias", (req, res) -> {
            JsonObject body = gson.fromJson(req.body(), JsonObject.class);

            String userId = body.get("userId").getAsString();
            String food = body.get("food").getAsString();
            String budget = body.get("budget").getAsString();
            String environment = body.get("environment").getAsString();
            double minRating =
                    body.get("minRating")
                        .getAsDouble();

            String distance =
                    body.get("distance")
                        .getAsString();

            boolean ok = neo4j.savePreferences(userId, food, budget, environment, minRating, distance);

            if (!ok) {
                res.status(500);
                return gson.toJson(Map.of("error", "No se pudieron guardar las preferencias."));
            }

            return gson.toJson(Map.of("message", "Preferencias guardadas."));
        });

        
        get("/api/restaurantes", (req, res) -> {
            String city = req.queryParams("city");

            List<Restaurant> restaurants = (city != null && !city.isBlank())
                    ? neo4j.getRestaurantsByCity(city)
                    : neo4j.getAllRestaurants();

            return gson.toJson(restaurants);
        });

        get("/api/ciudades", (req, res) -> {

            List<String> ciudades = neo4j.getCities();

            return gson.toJson(ciudades);
        });

        

        get("/api/restaurante/:id", (req, res) -> {

            String id = req.params(":id");

            Restaurant restaurant =
                    neo4j.getRestaurantById(id);

            if (restaurant == null) {

                res.status(404);

                return gson.toJson(
                        Map.of(
                                "error",
                                "Restaurante no encontrado"
                        )
                );
            }

            return gson.toJson(restaurant);
        });

        post("/api/recomendaciones", (req, res) -> {
            JsonObject body = gson.fromJson(req.body(), JsonObject.class);

            User user = new User("", "", "", "");
            user.setFavoriteFood(body.get("food").getAsString());
            user.setBudget(body.get("budget").getAsString());
            user.setEnvironment(body.get("environment").getAsString());

            List<Restaurant> all = neo4j.getAllRestaurants();
            List<Restaurant> recommended = service.recommendRestaurants(all, user);

            // Retornar top 10
            return gson.toJson(recommended.stream().limit(10).toList());
        });

        System.out.println("API corriendo en http://localhost:4567");
    }
}
